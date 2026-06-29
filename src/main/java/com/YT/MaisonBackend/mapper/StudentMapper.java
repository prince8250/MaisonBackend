package com.YT.MaisonBackend.mapper;

import java.util.HashMap;

import com.YT.MaisonBackend.dto.StudentCreateRequest;
import com.YT.MaisonBackend.dto.StudentResponse;
import com.YT.MaisonBackend.dto.StudentUpdateRequest;
import com.YT.MaisonBackend.entity.Student;
import com.YT.MaisonBackend.entity.User;

public final class StudentMapper {
	private StudentMapper() {
	}

	public static Student toEntity(StudentCreateRequest request, User user) {
		Student student = new Student();
		student.setPhoneNumber(request.getPhoneNumber());
		student.setUser(user);
		student.setStudentIdNumber(request.getStudentIdNumber());
		student.setCourse(request.getCourse());
		student.setLevel(request.getLevel());
		student.setGender(request.getGender());
		student.setProfilePhotoUrl(request.getProfilePhotoUrl());
		student.setIdCardUrl(request.getIdCardUrl());
		student.setCustomData(request.getCustomData() != null ? new HashMap<>(request.getCustomData()) : new HashMap<>());
		return student;
	}

	public static void applyUpdate(Student student, StudentUpdateRequest request, User user) {
		if (user != null) {
			student.setUser(user);
		}
		if (request.getPhoneNumber() != null) {
			student.setPhoneNumber(request.getPhoneNumber());
		}
		if (request.getStudentIdNumber() != null) {
			student.setStudentIdNumber(request.getStudentIdNumber());
		}
		if (request.getCourse() != null) {
			student.setCourse(request.getCourse());
		}
		if (request.getLevel() != null) {
			student.setLevel(request.getLevel());
		}
		if (request.getGender() != null) {
			student.setGender(request.getGender());
		}
		if (request.getProfilePhotoUrl() != null) {
			student.setProfilePhotoUrl(request.getProfilePhotoUrl());
		}
		if (request.getIdCardUrl() != null) {
			student.setIdCardUrl(request.getIdCardUrl());
		}
		if (request.getCustomData() != null) {
			student.setCustomData(new HashMap<>(request.getCustomData()));
		}
	}

	public static StudentResponse toResponse(Student student) {
		return new StudentResponse(
				student.getId(),
				student.getPhoneNumber(),
				student.getUser().getId(),
				student.getStudentIdNumber(),
				student.getCourse(),
				student.getLevel(),
				student.getGender(),
				student.getProfilePhotoUrl(),
				student.getIdCardUrl(),
				student.getCustomData());
	}
}