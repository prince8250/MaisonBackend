package com.YT.MaisonBackend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.YT.MaisonBackend.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
	private final UUID id;
	private final String firstName;
	private final String lastName;
	private final String username;
	private final String email;
	private final User.Role role;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;
}