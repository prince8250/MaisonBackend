package com.YT.MaisonBackend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.YT.MaisonBackend.dto.StudentCreateRequest;
import com.YT.MaisonBackend.dto.StudentResponse;
import com.YT.MaisonBackend.dto.StudentUpdateRequest;
import com.YT.MaisonBackend.entity.Student;
import com.YT.MaisonBackend.entity.User;
import com.YT.MaisonBackend.exception.NotFoundException;
import com.YT.MaisonBackend.mapper.StudentMapper;
import com.YT.MaisonBackend.repository.StudentRepository;
import com.YT.MaisonBackend.repository.UserRepository;

@Service
@Transactional
public class StudentService {
	private final StudentRepository studentRepository;
	private final UserRepository userRepository;

	public StudentService(StudentRepository studentRepository, UserRepository userRepository) {
		this.studentRepository = studentRepository;
		this.userRepository = userRepository;
	}

	public StudentResponse createStudent(StudentCreateRequest request) {
		// The student profile must be linked to an existing user account.
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new NotFoundException("User not found"));

		return StudentMapper.toResponse(studentRepository.save(StudentMapper.toEntity(request, user)));
	}

	public StudentResponse updateStudent(java.util.UUID id, StudentUpdateRequest request) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Student not found"));
		User user = null;

		if (request.getUserId() != null) {
			user = userRepository.findById(request.getUserId())
					.orElseThrow(() -> new NotFoundException("User not found"));
		}

		StudentMapper.applyUpdate(student, request, user);
		return StudentMapper.toResponse(studentRepository.save(student));
	}
}