package com.YT.MaisonBackend.dto;

import java.util.UUID;

import com.YT.MaisonBackend.entity.Room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomUpdateRequest {
	private UUID hostelId;
	private String roomNumber;
	private String roomType;
	private Integer capacity;
	private Room.GenderRestriction genderRestriction;
}