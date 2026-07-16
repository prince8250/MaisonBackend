package com.YT.MaisonBackend.mapper;

import com.YT.MaisonBackend.dto.RoomCreateRequest;
import com.YT.MaisonBackend.dto.RoomResponse;
import com.YT.MaisonBackend.dto.RoomUpdateRequest;
import com.YT.MaisonBackend.entity.Hostel;
import com.YT.MaisonBackend.entity.Room;

public final class RoomMapper {
	private RoomMapper() {
	}

	public static Room toEntity(RoomCreateRequest request, Hostel hostel) {
		Room room = new Room();
		room.setHostel(hostel);
		room.setRoomNumber(request.getRoomNumber());
		room.setRoomType(request.getRoomType());
		room.setCapacity(request.getCapacity());
		room.setGenderRestriction(request.getGenderRestriction());
		return room;
	}

	public static void applyUpdate(Room room, RoomUpdateRequest request, Hostel hostel) {
		if (hostel != null) {
			room.setHostel(hostel);
		}
		if (request.getRoomNumber() != null) {
			room.setRoomNumber(request.getRoomNumber());
		}
		if (request.getRoomType() != null) {
			room.setRoomType(request.getRoomType());
		}
		if (request.getCapacity() != null) {
			room.setCapacity(request.getCapacity());
		}
		if (request.getGenderRestriction() != null) {
			room.setGenderRestriction(request.getGenderRestriction());
		}
	}

	public static RoomResponse toResponse(Room room) {
		return new RoomResponse(
				room.getId(),
				room.getHostel().getId(),
				room.getRoomNumber(),
				room.getRoomType(),
				room.getCapacity(),
				room.getOccupancy(),
				room.getGenderRestriction(),
				room.getCreatedAt(),
				room.getUpdatedAt());
	}
}