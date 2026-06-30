package com.YT.MaisonBackend.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.YT.MaisonBackend.dto.RoomCreateRequest;
import com.YT.MaisonBackend.dto.RoomResponse;
import com.YT.MaisonBackend.dto.RoomUpdateRequest;
import com.YT.MaisonBackend.entity.Hostel;
import com.YT.MaisonBackend.entity.Room;
import com.YT.MaisonBackend.exception.ConflictException;
import com.YT.MaisonBackend.exception.NotFoundException;
import com.YT.MaisonBackend.mapper.RoomMapper;
import com.YT.MaisonBackend.repository.HostelRepository;
import com.YT.MaisonBackend.repository.RoomRepository;

@Service
@Transactional
/**
 * Handles room registration and room metadata updates for a hostel.
 */
public class RoomService {
	private final RoomRepository roomRepository;
	private final HostelRepository hostelRepository;

	public RoomService(RoomRepository roomRepository, HostelRepository hostelRepository) {
		this.roomRepository = roomRepository;
		this.hostelRepository = hostelRepository;
	}

	/**
	 * Creates a room inside an existing hostel.
	 */
	public RoomResponse addRoom(RoomCreateRequest request) {
		Hostel hostel = hostelRepository.findById(request.getHostelId())
				.orElseThrow(() -> new NotFoundException("Hostel not found"));

		if (roomRepository.existsByHostel_IdAndRoomNumber(request.getHostelId(), request.getRoomNumber())) {
			throw new ConflictException("Room number already exists in this hostel");
		}

		return RoomMapper.toResponse(roomRepository.save(RoomMapper.toEntity(request, hostel)));
	}

	/**
	 * Updates room details such as room number, type, capacity, or hostel.
	 */
	public RoomResponse updateRoom(UUID roomId, RoomUpdateRequest request) {
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new NotFoundException("Room not found"));

		Hostel hostel = null;
		if (request.getHostelId() != null) {
			hostel = hostelRepository.findById(request.getHostelId())
					.orElseThrow(() -> new NotFoundException("Hostel not found"));

			if (request.getRoomNumber() != null
					&& roomRepository.existsByHostel_IdAndRoomNumber(request.getHostelId(), request.getRoomNumber())
					&& (!room.getHostel().getId().equals(request.getHostelId())
						|| !room.getRoomNumber().equals(request.getRoomNumber()))) {
				throw new ConflictException("Room number already exists in this hostel");
			}
		} else if (request.getRoomNumber() != null
				&& roomRepository.existsByHostel_IdAndRoomNumber(room.getHostel().getId(), request.getRoomNumber())
				&& !room.getRoomNumber().equals(request.getRoomNumber())) {
			throw new ConflictException("Room number already exists in this hostel");
		}

		RoomMapper.applyUpdate(room, request, hostel);
		return RoomMapper.toResponse(roomRepository.save(room));
	}

	/**
	 * Deletes a room record from the system.
	 */
	public void deleteRoom(UUID roomId) {
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new NotFoundException("Room not found"));
		roomRepository.delete(room);
	}
}