package com.YT.MaisonBackend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YT.MaisonBackend.entity.RoomAllocation;

@Repository
public interface RoomAllocationRepository extends JpaRepository<RoomAllocation, UUID> {
	long countByRoom_IdAndAcademicYear(UUID roomId, String academicYear);

	Optional<RoomAllocation> findByStudent_IdAndAcademicYear(UUID studentId, String academicYear);
}