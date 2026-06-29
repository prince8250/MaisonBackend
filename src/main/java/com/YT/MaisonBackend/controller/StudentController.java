package com.YT.MaisonBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.YT.MaisonBackend.dto.StudentCreateRequest;
import com.YT.MaisonBackend.dto.StudentResponse;
import com.YT.MaisonBackend.dto.StudentUpdateRequest;
import com.YT.MaisonBackend.service.StudentService;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@PostMapping
	public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentCreateRequest request) {
		// Creates a student profile linked to an existing user.
		return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<StudentResponse> updateStudent(@PathVariable java.util.UUID id, @RequestBody StudentUpdateRequest request) {
		// Updates the student profile and can re-point the user_id foreign key if needed.
		return ResponseEntity.ok(studentService.updateStudent(id, request));
	}
}