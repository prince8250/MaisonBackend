package com.YT.MaisonBackend.dto;

import java.util.Map;
import java.util.UUID;

import com.YT.MaisonBackend.entity.Student;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentCreateRequest {
	private String phoneNumber;
	private UUID userId;
	private String studentIdNumber;
	private String course;
	private String level;
	private Student.Gender gender;
	private String profilePhotoUrl;
	private String idCardUrl;
	private Map<String, Object> customData;
}