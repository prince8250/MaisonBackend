package com.YT.MaisonBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}