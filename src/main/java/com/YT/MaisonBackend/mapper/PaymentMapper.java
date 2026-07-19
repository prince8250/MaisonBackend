package com.YT.MaisonBackend.mapper;

import com.YT.MaisonBackend.dto.PaystackPaymentResponse;
import com.YT.MaisonBackend.entity.Payment;

public final class PaymentMapper {

	private PaymentMapper() {
	}

	public static PaystackPaymentResponse toResponse(Payment payment) {
		return new PaystackPaymentResponse(
				payment.getId(),
				payment.getRoomAllocation().getId(),
				payment.getReference(),
				payment.getAuthorizationUrl(),
				payment.getAccessCode(),
				payment.getAmount(),
				payment.getCurrency(),
				payment.getStatus(),
				payment.getPaystackStatus(),
				payment.getGatewayResponse(),
				payment.getPaystackTransactionId(),
				payment.getPaidAt(),
				payment.getCreatedAt(),
				payment.getUpdatedAt());
	}
}