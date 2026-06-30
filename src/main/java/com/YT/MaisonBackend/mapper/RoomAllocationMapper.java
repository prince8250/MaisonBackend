package com.YT.MaisonBackend.mapper;

import java.util.List;

import com.YT.MaisonBackend.dto.RoomAllocationResponse;
import com.YT.MaisonBackend.entity.Room;
import com.YT.MaisonBackend.entity.RoomAllocation;
import com.YT.MaisonBackend.entity.Student;

public final class RoomAllocationMapper {
	private RoomAllocationMapper() {
	}

    public static RoomAllocation toEntity(Student student, Room room, String academicYear, int roommatesCount, boolean roomAvailable) {
		RoomAllocation allocation = new RoomAllocation();
		allocation.setStudent(student);
		allocation.setRoom(room);
		allocation.setGenderType(toGenderType(student.getGender()));
		allocation.setAcademicYear(academicYear);
		allocation.setRoomAvailable(roomAvailable);
		allocation.setRoommatesCount(roommatesCount);
		return allocation;
	}

	public static RoomAllocationResponse toResponse(RoomAllocation allocation) {
		return new RoomAllocationResponse(
				allocation.getId(),
				allocation.getStudent().getId(),
				allocation.getRoom().getId(),
				allocation.getRoommates().stream().map(Student::getId).toList(),
				allocation.getGenderType(),
				allocation.getAcademicYear(),
				allocation.getRoomAvailable(),
				allocation.getRoommatesCount(),
				allocation.getCreatedAt(),
				allocation.getUpdatedAt());
	}

	public static RoomAllocation.GenderType toGenderType(Student.Gender gender) {
		return gender == Student.Gender.MALE ? RoomAllocation.GenderType.MALE : RoomAllocation.GenderType.FEMALE;
	}
}