package com.YT.MaisonBackend.mapper;

import com.YT.MaisonBackend.dto.UserCreateRequest;
import com.YT.MaisonBackend.dto.UserResponse;
import com.YT.MaisonBackend.dto.UserUpdateRequest;
import com.YT.MaisonBackend.entity.User;

public final class UserMapper {
	private UserMapper() {
	}

	public static User toEntity(UserCreateRequest request) {
		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setRole(request.getRole());
		return user;
	}

	public static void applyUpdate(User user, UserUpdateRequest request) {
		if (request.getFirstName() != null) {
			user.setFirstName(request.getFirstName());
		}
		if (request.getLastName() != null) {
			user.setLastName(request.getLastName());
		}
		if (request.getUsername() != null) {
			user.setUsername(request.getUsername());
		}
		if (request.getEmail() != null) {
			user.setEmail(request.getEmail());
		}
		if (request.getPassword() != null) {
			user.setPassword(request.getPassword());
		}
		if (request.getRole() != null) {
			user.setRole(request.getRole());
		}
	}

	public static UserResponse toResponse(User user) {
		return new UserResponse(
				user.getId(),
				user.getFirstName(),
				user.getLastName(),
				user.getUsername(),
				user.getEmail(),
				user.getRole(),
				user.getCreatedAt(),
				user.getUpdatedAt());
	}
}