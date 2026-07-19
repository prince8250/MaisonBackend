package com.YT.MaisonBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.YT.MaisonBackend.dto.PaystackInitializeRequest;
import com.YT.MaisonBackend.dto.PaystackPaymentResponse;
import com.YT.MaisonBackend.service.PaystackPaymentService;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
	private final PaystackPaymentService paystackPaymentService;

	public PaymentController(PaystackPaymentService paystackPaymentService) {
		this.paystackPaymentService = paystackPaymentService;
	}

	@PostMapping("/paystack/initialize")
	public ResponseEntity<PaystackPaymentResponse> initializePaystackPayment(@RequestBody PaystackInitializeRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(paystackPaymentService.initializePayment(request));
	}

	@GetMapping("/paystack/verify/{reference}")
	public ResponseEntity<PaystackPaymentResponse> verifyPaystackPayment(@PathVariable String reference) {
		return ResponseEntity.ok(paystackPaymentService.verifyPayment(reference));
	}
}