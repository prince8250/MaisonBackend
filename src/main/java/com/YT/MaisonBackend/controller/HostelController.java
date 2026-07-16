package com.YT.MaisonBackend.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.YT.MaisonBackend.dto.HostelCreateRequest;
import com.YT.MaisonBackend.dto.HostelResponse;
import com.YT.MaisonBackend.service.HostelService;

@RestController
@RequestMapping("/api/v1/hostels")
public class HostelController {
	private final HostelService hostelService;

	public HostelController(HostelService hostelService) {
		this.hostelService = hostelService;
	}

	@PostMapping("/register")
	public ResponseEntity<HostelResponse> registerHostel(@RequestBody HostelCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(hostelService.registerHostel(request));
	}

	@GetMapping("/{hostelId}/profile")
	public ResponseEntity<HostelResponse> getHostelProfile(@PathVariable UUID hostelId) {
		return ResponseEntity.ok(hostelService.getHostelProfile(hostelId));
	}

	@PutMapping("/{hostelId}/profile")
	public ResponseEntity<HostelResponse> updateHostelProfile(
			@PathVariable UUID hostelId,
			@RequestBody HostelCreateRequest request) {
		return ResponseEntity.ok(hostelService.updateHostelProfile(hostelId, request));
	}
}