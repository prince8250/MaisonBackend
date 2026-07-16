package com.YT.MaisonBackend.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.YT.MaisonBackend.dto.HostelCreateRequest;
import com.YT.MaisonBackend.dto.HostelResponse;
import com.YT.MaisonBackend.entity.Hostel;
import com.YT.MaisonBackend.mapper.HostelMapper;
import com.YT.MaisonBackend.repository.HostelRepository;
import com.YT.MaisonBackend.exception.NotFoundException;

/**
 * Service for creating, reading, and updating hostel profile details.
 */
@Service
@Transactional
public class HostelService {
	private final HostelRepository hostelRepository;

	public HostelService(HostelRepository hostelRepository) {
		this.hostelRepository = hostelRepository;
	}

	/**
	 * Creates and stores a new hostel record.
	 */
	public HostelResponse registerHostel(HostelCreateRequest request) {
		return HostelMapper.toResponse(hostelRepository.save(HostelMapper.toEntity(request)));
	}

	/**
	 * Returns the stored hostel profile for the given hostel id.
	 */
	@Transactional(readOnly = true)
	public HostelResponse getHostelProfile(UUID hostelId) {
		return HostelMapper.toResponse(loadHostel(hostelId));
	}

	/**
	 * Updates the stored hostel profile fields.
	 */
	public HostelResponse updateHostelProfile(UUID hostelId, HostelCreateRequest request) {
		Hostel hostel = loadHostel(hostelId);
		HostelMapper.applyUpdate(hostel, request);
		return HostelMapper.toResponse(hostelRepository.save(hostel));
	}

	private Hostel loadHostel(UUID hostelId) {
		return hostelRepository.findById(hostelId)
				.orElseThrow(() -> new NotFoundException("Hostel not found"));
	}
}