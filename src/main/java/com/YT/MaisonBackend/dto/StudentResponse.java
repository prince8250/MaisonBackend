package com.YT.MaisonBackend.dto;

import java.util.Map;
import java.util.UUID;

import com.YT.MaisonBackend.entity.Student;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentResponse {
	private final UUID id;
	private final String phoneNumber;
	private final UUID userId;
	private final String studentIdNumber;
	private final String course;
	private final String level;
	private final Student.Gender gender;
	private final String profilePhotoUrl;
	private final String idCardUrl;
	private final Map<String, Object> customData;
}