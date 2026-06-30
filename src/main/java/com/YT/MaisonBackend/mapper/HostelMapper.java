package com.YT.MaisonBackend.mapper;

import com.YT.MaisonBackend.dto.HostelCreateRequest;
import com.YT.MaisonBackend.dto.HostelResponse;
import com.YT.MaisonBackend.entity.Hostel;

public final class HostelMapper {
	private HostelMapper() {
	}

	public static Hostel toEntity(HostelCreateRequest request) {
		Hostel hostel = new Hostel();
		hostel.setPhoneNumber(request.getPhoneNumber());
		hostel.setName(request.getName());
		hostel.setAddress(request.getAddress());
		hostel.setCapacity(request.getCapacity());
		return hostel;
	}

	public static HostelResponse toResponse(Hostel hostel) {
		return new HostelResponse(
				hostel.getId(),
				hostel.getPhoneNumber(),
				hostel.getName(),
				hostel.getAddress(),
				hostel.getCapacity(),
				hostel.getCreatedAt(),
				hostel.getUpdatedAt());
	}
}