package com.YT.MaisonBackend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
	@Id
	@UuidGenerator
	@Column(nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "room_allocation_id", nullable = false)
	private RoomAllocation roomAllocation;

	@Column(nullable = false, unique = true)
	private String reference;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal amount;

	@Column(nullable = false, length = 10)
	private String currency;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status = Status.PENDING;

	@Column(name = "authorization_url", columnDefinition = "TEXT")
	private String authorizationUrl;

	@Column(name = "access_code")
	private String accessCode;

	@Column(name = "paystack_status")
	private String paystackStatus;

	@Column(name = "gateway_response")
	private String gatewayResponse;

	@Column(name = "paystack_transaction_id")
	private Long paystackTransactionId;

	@Column(name = "paid_at")
	private LocalDateTime paidAt;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public enum Status {
		PENDING,
		INITIATED,
		SUCCESS,
		FAILED
	}
}