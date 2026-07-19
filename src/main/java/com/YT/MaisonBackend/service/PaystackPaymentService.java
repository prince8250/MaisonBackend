package com.YT.MaisonBackend.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.YT.MaisonBackend.dto.PaystackInitializeRequest;
import com.YT.MaisonBackend.dto.PaystackPaymentResponse;
import com.YT.MaisonBackend.entity.Payment;
import com.YT.MaisonBackend.entity.RoomAllocation;
import com.YT.MaisonBackend.exception.BadRequestException;
import com.YT.MaisonBackend.exception.ConflictException;
import com.YT.MaisonBackend.exception.NotFoundException;
import com.YT.MaisonBackend.mapper.PaymentMapper;
import com.YT.MaisonBackend.repository.PaymentRepository;
import com.YT.MaisonBackend.repository.RoomAllocationRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaystackPaymentService {
	private static final String DEFAULT_CURRENCY = "GHS";

	private final PaymentRepository paymentRepository;
	private final RoomAllocationRepository roomAllocationRepository;
	private final ObjectMapper objectMapper;
	private final HttpClient httpClient;

	@Value("${paystack.secret-key:}")
	private String paystackSecretKey;

	@Value("${paystack.base-url:https://api.paystack.co}")
	private String paystackBaseUrl;

	@Value("${paystack.currency:GHS}")
	private String paystackCurrency;

	public PaystackPaymentResponse initializePayment(PaystackInitializeRequest request) {
		RoomAllocation roomAllocation = roomAllocationRepository.findById(request.getRoomAllocationId())
				.orElseThrow(() -> new NotFoundException("Room allocation not found"));

		if (paymentRepository.existsByRoomAllocation_IdAndStatus(roomAllocation.getId(), Payment.Status.SUCCESS)) {
			throw new ConflictException("This room allocation has already been paid for");
		}

		String email = roomAllocation.getStudent().getUser().getEmail();
		if (email == null || email.isBlank()) {
			throw new BadRequestException("Student email is required to initialize payment");
		}

		BigDecimal amount = roomAllocation.getRoom().getPrice();
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new BadRequestException("Room price must be greater than zero before payment can be initialized");
		}

		String currency = resolveCurrency();
		Payment payment = new Payment();
		payment.setRoomAllocation(roomAllocation);
		payment.setReference(generateReference());
		payment.setAmount(amount.setScale(2, RoundingMode.HALF_UP));
		payment.setCurrency(currency);
		payment.setStatus(Payment.Status.PENDING);
		payment = paymentRepository.save(payment);

		try {
			JsonNode response = sendInitializeRequest(payment, email, request.getCallbackUrl(), currency);
			if (!response.path("status").asBoolean(false)) {
				payment.setStatus(Payment.Status.FAILED);
				payment.setGatewayResponse(response.path("message").asText("Unable to initialize payment"));
				paymentRepository.save(payment);
				throw new BadRequestException(payment.getGatewayResponse());
			}

			JsonNode data = response.path("data");
			payment.setAuthorizationUrl(data.path("authorization_url").asText(null));
			payment.setAccessCode(data.path("access_code").asText(null));
			payment.setPaystackStatus(data.path("status").asText("pending"));
			payment.setGatewayResponse(response.path("message").asText("Payment initialized"));
			payment.setStatus(Payment.Status.INITIATED);
			payment = paymentRepository.save(payment);
			return PaymentMapper.toResponse(payment);
		} catch (IOException exception) {
			payment.setStatus(Payment.Status.FAILED);
			payment.setGatewayResponse("Unable to communicate with Paystack");
			paymentRepository.save(payment);
			throw new BadRequestException("Unable to communicate with Paystack");
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
			payment.setStatus(Payment.Status.FAILED);
			payment.setGatewayResponse("Payment initialization was interrupted");
			paymentRepository.save(payment);
			throw new BadRequestException("Payment initialization was interrupted");
		}
	}

	public PaystackPaymentResponse verifyPayment(String reference) {
		Payment payment = paymentRepository.findByReference(reference)
				.orElseThrow(() -> new NotFoundException("Payment not found"));

		try {
			JsonNode response = sendVerifyRequest(reference);
			if (!response.path("status").asBoolean(false)) {
				payment.setStatus(Payment.Status.FAILED);
				payment.setPaystackStatus(response.path("message").asText("failed"));
				payment.setGatewayResponse(response.path("message").asText("Payment verification failed"));
				paymentRepository.save(payment);
				throw new BadRequestException(payment.getGatewayResponse());
			}

			JsonNode data = response.path("data");
			String transactionStatus = data.path("status").asText("failed");
			payment.setPaystackStatus(transactionStatus);
			payment.setGatewayResponse(data.path("gateway_response").asText(response.path("message").asText("Payment verified")));
			if (data.hasNonNull("id")) {
				payment.setPaystackTransactionId(data.path("id").asLong());
			}
			if ("success".equalsIgnoreCase(transactionStatus)) {
				payment.setStatus(Payment.Status.SUCCESS);
				payment.setPaidAt(LocalDateTime.now());
			} else {
				payment.setStatus(Payment.Status.FAILED);
			}
			payment = paymentRepository.save(payment);
			return PaymentMapper.toResponse(payment);
		} catch (IOException exception) {
			throw new BadRequestException("Unable to communicate with Paystack");
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
			throw new BadRequestException("Payment verification was interrupted");
		}
	}

	private JsonNode sendInitializeRequest(Payment payment, String email, String callbackUrl, String currency)
			throws IOException, InterruptedException {
		ensureConfigured();
		ObjectNode payload = objectMapper.createObjectNode();
		payload.put("email", email);
		payload.put("amount", toKobo(payment.getAmount()));
		payload.put("currency", currency);
		payload.put("reference", payment.getReference());
		if (callbackUrl != null && !callbackUrl.isBlank()) {
			payload.put("callback_url", callbackUrl);
		}

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(paystackBaseUrl + "/transaction/initialize"))
				.header("Authorization", "Bearer " + paystackSecretKey)
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload), StandardCharsets.UTF_8))
				.build();
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
		return objectMapper.readTree(response.body());
	}

	private JsonNode sendVerifyRequest(String reference) throws IOException, InterruptedException {
		ensureConfigured();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(paystackBaseUrl + "/transaction/verify/" + reference))
				.header("Authorization", "Bearer " + paystackSecretKey)
				.header("Accept", "application/json")
				.GET()
				.build();
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
		return objectMapper.readTree(response.body());
	}

	private void ensureConfigured() {
		if (paystackSecretKey == null || paystackSecretKey.isBlank()) {
			throw new BadRequestException("Paystack secret key is not configured");
		}
	}

	private String generateReference() {
		return "PAY-" + UUID.randomUUID().toString().replace("-", "").substring(0, 18).toUpperCase();
	}

	private long toKobo(BigDecimal amount) {
		return amount.multiply(BigDecimal.valueOf(100L)).setScale(0, RoundingMode.HALF_UP).longValueExact();
	}

	private String resolveCurrency() {
		if (paystackCurrency == null || paystackCurrency.isBlank()) {
			return DEFAULT_CURRENCY;
		}
		return paystackCurrency.trim().toUpperCase();
	}
}