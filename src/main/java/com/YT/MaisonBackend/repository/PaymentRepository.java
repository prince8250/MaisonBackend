package com.YT.MaisonBackend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YT.MaisonBackend.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
	Optional<Payment> findByReference(String reference);

	boolean existsByRoomAllocation_IdAndStatus(UUID roomAllocationId, Payment.Status status);
}