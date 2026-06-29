package com.YT.MaisonBackend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.YT.MaisonBackend.dto.UserCreateRequest;
import com.YT.MaisonBackend.dto.UserResponse;
import com.YT.MaisonBackend.dto.UserUpdateRequest;
import com.YT.MaisonBackend.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		// Returns the full user list with a 200 OK response.
		return ResponseEntity.ok(userService.findAllUsers());
	}

	@PostMapping
	public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request) {
		// Creates a new user and returns the created record with a 201 Created response.
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> findUserById(@PathVariable UUID id) {
		// Fetches a single user by id.
		return ResponseEntity.ok(userService.findUserById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequest request) {
		// Updates only the fields allowed by the request DTO.
		return ResponseEntity.ok(userService.updateUser(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
		// Deletes the user and returns 204 No Content.
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}
