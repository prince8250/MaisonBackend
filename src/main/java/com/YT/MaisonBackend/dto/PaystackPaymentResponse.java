package com.YT.MaisonBackend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.YT.MaisonBackend.entity.Payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaystackPaymentResponse {

	private final UUID paymentId;
	private final UUID roomAllocationId;
	private final String reference;
	private final String authorizationUrl;
	private final String accessCode;
	private final BigDecimal amount;
	private final String currency;
	private final Payment.Status status;
	private final String paystackStatus;
	private final String gatewayResponse;
	private final Long paystackTransactionId;
	private final LocalDateTime paidAt;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;
}