package com.YT.MaisonBackend.mapper;

import java.util.ArrayList;

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
		hostel.setPricing(request.getPricing());
		hostel.setLocation(request.getLocation());
		hostel.setFacilities(request.getFacilities());
		hostel.setRoomTypes(request.getRoomTypes());
		if (request.getImageUrls() != null) {
			hostel.setImageUrls(new ArrayList<>(request.getImageUrls()));
		}
		return hostel;
	}

	public static void applyUpdate(Hostel hostel, HostelCreateRequest request) {
		if (request.getPhoneNumber() != null) {
			hostel.setPhoneNumber(request.getPhoneNumber());
		}
		if (request.getName() != null) {
			hostel.setName(request.getName());
		}
		if (request.getAddress() != null) {
			hostel.setAddress(request.getAddress());
		}
		if (request.getCapacity() != null) {
			hostel.setCapacity(request.getCapacity());
		}
		if (request.getPricing() != null) {
			hostel.setPricing(request.getPricing());
		}
		if (request.getLocation() != null) {
			hostel.setLocation(request.getLocation());
		}
		if (request.getFacilities() != null) {
			hostel.setFacilities(request.getFacilities());
		}
		if (request.getRoomTypes() != null) {
			hostel.setRoomTypes(request.getRoomTypes());
		}
		if (request.getImageUrls() != null) {
			hostel.setImageUrls(new ArrayList<>(request.getImageUrls()));
		}
	}

	public static HostelResponse toResponse(Hostel hostel) {
		return new HostelResponse(
				hostel.getId(),
				hostel.getPhoneNumber(),
				hostel.getName(),
				hostel.getAddress(),
				hostel.getCapacity(),
				hostel.getPricing(),
				hostel.getLocation(),
				hostel.getFacilities(),
				hostel.getRoomTypes(),
				hostel.getImageUrls(),
				hostel.getCreatedAt(),
				hostel.getUpdatedAt());
	}
}