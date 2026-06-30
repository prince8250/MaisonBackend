package com.YT.MaisonBackend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.YT.MaisonBackend.dto.RoomAllocationCreateRequest;
import com.YT.MaisonBackend.dto.RoomAllocationResponse;
import com.YT.MaisonBackend.entity.Room;
import com.YT.MaisonBackend.entity.RoomAllocation;
import com.YT.MaisonBackend.entity.Student;
import com.YT.MaisonBackend.exception.ConflictException;
import com.YT.MaisonBackend.exception.NotFoundException;
import com.YT.MaisonBackend.mapper.RoomAllocationMapper;
import com.YT.MaisonBackend.repository.RoomAllocationRepository;
import com.YT.MaisonBackend.repository.RoomRepository;
import com.YT.MaisonBackend.repository.StudentRepository;

@Service
@Transactional
/**
 * Handles room allocation rules.
 * It assigns students to rooms, enforces capacity, and checks gender restrictions.
 */
public class RoomAllocationService {
	private final RoomAllocationRepository roomAllocationRepository;
	private final RoomRepository roomRepository;
	private final StudentRepository studentRepository;

	public RoomAllocationService(
			RoomAllocationRepository roomAllocationRepository,
			RoomRepository roomRepository,
			StudentRepository studentRepository) {
		this.roomAllocationRepository = roomAllocationRepository;
		this.roomRepository = roomRepository;
		this.studentRepository = studentRepository;
	}

	/**
	 * Assigns a student to a room for a given academic year.
	 * The method rejects duplicate allocations, enforces gender restrictions, and stops at room capacity.
	 */
	public RoomAllocationResponse assignStudent(RoomAllocationCreateRequest request) {
		Student student = studentRepository.findById(request.getStudentId())
				.orElseThrow(() -> new NotFoundException("Student not found"));
		Room room = roomRepository.findById(request.getRoomId())
				.orElseThrow(() -> new NotFoundException("Room not found"));

		RoomAllocation existingAllocation = roomAllocationRepository
				.findByStudent_IdAndAcademicYear(student.getId(), request.getAcademicYear())
				.orElse(null);
		if (existingAllocation != null) {
			throw new ConflictException("Student already has a room allocation for this academic year");
		}

		RoomAllocation.GenderType studentGender = RoomAllocationMapper.toGenderType(student.getGender());
		if (room.getGenderRestriction() != null && !room.getGenderRestriction().name().equals(studentGender.name())) {
			throw new ConflictException("Student gender does not match room gender restriction");
		}

		long allocatedStudents = roomAllocationRepository.countByRoom_IdAndAcademicYear(room.getId(), request.getAcademicYear());
		if (allocatedStudents >= room.getCapacity()) {
			throw new ConflictException("Room is already full");
		}

		boolean roomAvailable = allocatedStudents + 1 < room.getCapacity();
		List<RoomAllocation> currentAllocations = roomAllocationRepository
				.findAll().stream()
				.filter(allocation -> allocation.getRoom().getId().equals(room.getId())
						&& allocation.getAcademicYear().equals(request.getAcademicYear()))
				.toList();

		RoomAllocation allocation = RoomAllocationMapper.toEntity(
				student,
				room,
				request.getAcademicYear(),
				currentAllocations.size(),
				roomAvailable);

		List<Student> roommates = new ArrayList<>();
		for (RoomAllocation currentAllocation : currentAllocations) {
			roommates.add(currentAllocation.getStudent());
			currentAllocation.getRoommates().add(student);
		}
		allocation.getRoommates().addAll(roommates);
		roomAllocationRepository.saveAll(currentAllocations);
		return RoomAllocationMapper.toResponse(roomAllocationRepository.save(allocation));
	}

	/**
	 * Removes a room allocation record.
	 */
	public void deleteAllocation(UUID allocationId) {
		RoomAllocation allocation = roomAllocationRepository.findById(allocationId)
				.orElseThrow(() -> new NotFoundException("Room allocation not found"));
		roomAllocationRepository.delete(allocation);
	}
}