package com.YT.MaisonBackend.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "room_allocations")
public class RoomAllocation {
	@Id
	@UuidGenerator
	@Column(nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "room_id", nullable = false)
	private Room room;

	@ManyToMany
	@JoinTable(
			name = "room_allocation_roommates",
			joinColumns = @JoinColumn(name = "room_allocation_id"),
			inverseJoinColumns = @JoinColumn(name = "roommate_id"))
	private List<Student> roommates = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(name = "gender_type", nullable = false)
	private GenderType genderType;

	@Column(name = "academic_year", nullable = false)
	private String academicYear;

	@Column(name = "room_available", nullable = false)
	private Boolean roomAvailable = Boolean.TRUE;

	@Column(name = "roommates_count", nullable = false)
	private Integer roommatesCount;

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

	public enum GenderType {
		MALE,
		FEMALE
	}
}