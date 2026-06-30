package com.YT.MaisonBackend.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.YT.MaisonBackend.entity.RoomAllocation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomAllocationResponse {
	private final UUID id;
	private final UUID studentId;
	private final UUID roomId;
	private final List<UUID> roommateIds;
	private final RoomAllocation.GenderType genderType;
	private final String academicYear;
	private final Boolean roomAvailable;
	private final Integer roommatesCount;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;
}