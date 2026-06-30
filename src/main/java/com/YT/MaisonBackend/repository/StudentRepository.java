package com.YT.MaisonBackend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YT.MaisonBackend.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
	List<Student> findAllByHostelId(UUID hostelId);

	boolean existsByIdAndHostelId(UUID studentId, UUID hostelId);
}