package com.YT.MaisonBackend.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.YT.MaisonBackend.dto.RoomCreateRequest;
import com.YT.MaisonBackend.dto.RoomResponse;
import com.YT.MaisonBackend.dto.RoomUpdateRequest;
import com.YT.MaisonBackend.service.RoomService;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
	private final RoomService roomService;

	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@PostMapping
	public ResponseEntity<RoomResponse> addRoom(@RequestBody RoomCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(roomService.addRoom(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<RoomResponse> updateRoom(@PathVariable UUID id, @RequestBody RoomUpdateRequest request) {
		return ResponseEntity.ok(roomService.updateRoom(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRoom(@PathVariable UUID id) {
		roomService.deleteRoom(id);
		return ResponseEntity.noContent().build();
	}
}