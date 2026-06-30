package com.YT.MaisonBackend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.YT.MaisonBackend.entity.Room;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomResponse {
	private final UUID id;
	private final UUID hostelId;
	private final String roomNumber;
	private final String roomType;
	private final Integer capacity;
	private final Room.GenderRestriction genderRestriction;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;
}