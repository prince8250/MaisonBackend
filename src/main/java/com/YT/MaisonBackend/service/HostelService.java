package com.YT.MaisonBackend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.YT.MaisonBackend.dto.HostelCreateRequest;
import com.YT.MaisonBackend.dto.HostelResponse;
import com.YT.MaisonBackend.mapper.HostelMapper;
import com.YT.MaisonBackend.repository.HostelRepository;

@Service
@Transactional
/**
 * Handles hostel onboarding and persistence.
 * This service currently creates new hostel records.
 */
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
}