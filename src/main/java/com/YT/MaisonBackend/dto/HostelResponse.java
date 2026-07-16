package com.YT.MaisonBackend.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HostelResponse {
	private final UUID id;
	private final String phoneNumber;
	private final String name;
	private final String address;
	private final Integer capacity;
	private final String pricing;
	private final String location;
	private final String facilities;
	private final String roomTypes;
	private final List<String> imageUrls;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;
}