package com.YT.MaisonBackend.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
@Table(name = "hostels")
public class Hostel {
	@Id
	@UuidGenerator
	@Column(nullable = false, updatable = false)
	private UUID id;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private Integer capacity;

	@Column(columnDefinition = "TEXT")
	private String pricing;

	@Column(columnDefinition = "TEXT")
	private String location;

	@Column(columnDefinition = "TEXT")
	private String facilities;

	@Column(columnDefinition = "TEXT")
	private String roomTypes;

	@ElementCollection
	@CollectionTable(name = "hostel_image_urls", joinColumns = @JoinColumn(name = "hostel_id"))
	@Column(name = "image_url", nullable = false)
	private List<String> imageUrls = new ArrayList<>();

	@OneToMany(mappedBy = "hostel")
	private List<Room> rooms = new ArrayList<>();

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
}