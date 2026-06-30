package com.YT.MaisonBackend.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {
	@Id
	@UuidGenerator
	@Column(nullable = false, updatable = false)
	private UUID id;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "hostel_id")
	private Hostel hostel;

	@Column(name = "student_id_number", nullable = false)
	private String studentIdNumber;

	@Column(nullable = false)
	private String course;

	@Column(nullable = false)
	private String level;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Gender gender;

	@Column(name = "profile_photo_url")
	private String profilePhotoUrl;

	@Column(name = "id_card_url")
	private String idCardUrl;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "custom_data", columnDefinition = "jsonb")
	private Map<String, Object> customData = new HashMap<>();

	public enum Gender {
		MALE,
		FEMALE
	}
}
