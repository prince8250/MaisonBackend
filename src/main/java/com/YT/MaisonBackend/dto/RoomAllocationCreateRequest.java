package com.YT.MaisonBackend.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomAllocationCreateRequest {
	private UUID studentId;
	private UUID roomId;
	private String academicYear;
}