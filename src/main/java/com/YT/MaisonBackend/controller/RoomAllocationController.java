package com.YT.MaisonBackend.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.YT.MaisonBackend.dto.RoomAllocationCreateRequest;
import com.YT.MaisonBackend.dto.RoomAllocationResponse;
import com.YT.MaisonBackend.service.RoomAllocationService;

@RestController
@RequestMapping("/api/v1/room-allocations")
public class RoomAllocationController {
	private final RoomAllocationService roomAllocationService;

	public RoomAllocationController(RoomAllocationService roomAllocationService) {
		this.roomAllocationService = roomAllocationService;
	}

	@PostMapping
	public ResponseEntity<RoomAllocationResponse> assignStudent(@RequestBody RoomAllocationCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(roomAllocationService.assignStudent(request));
	}
	
	@PostMapping("/book")
	public ResponseEntity<RoomAllocationResponse> bookRoom(@RequestBody RoomAllocationCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(roomAllocationService.assignStudent(request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAllocation(@PathVariable UUID id) {
		roomAllocationService.deleteAllocation(id);
		return ResponseEntity.noContent().build();
	}
}