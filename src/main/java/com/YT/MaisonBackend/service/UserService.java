package com.YT.MaisonBackend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.YT.MaisonBackend.dto.UserCreateRequest;
import com.YT.MaisonBackend.dto.UserResponse;
import com.YT.MaisonBackend.dto.UserUpdateRequest;
import com.YT.MaisonBackend.entity.User;
import com.YT.MaisonBackend.exception.ConflictException;
import com.YT.MaisonBackend.exception.NotFoundException;
import com.YT.MaisonBackend.mapper.UserMapper;
import com.YT.MaisonBackend.repository.UserRepository;

@Service
@Transactional
/**
 * Manages application users.
 * This service supports listing, lookup, creation, updates, and deletion.
 */
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Returns every user in the system.
	 */
	@Transactional(readOnly = true)
	public List<UserResponse> findAllUsers() {
		return userRepository.findAll().stream().map(UserMapper::toResponse).toList();
	}

	/**
	 * Finds a single user by id.
	 */
	@Transactional(readOnly = true)
	public UserResponse findUserById(UUID id) {
		return userRepository.findById(id)
				.map(UserMapper::toResponse)
				.orElseThrow(() -> new NotFoundException("User not found"));
	}

	/**
	 * Creates a new user after checking that the email and username are unique.
	 */
	public UserResponse createUser(UserCreateRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new ConflictException("Email already exists");
		}

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new ConflictException("Username already exists");
		}

		return UserMapper.toResponse(userRepository.save(UserMapper.toEntity(request)));
	}

	/**
	 * Updates an existing user while preserving email uniqueness.
	 */
	public UserResponse updateUser(UUID id, UserUpdateRequest request) {
		User entity = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User not found"));

		if (request.getEmail() != null) {
			if (!request.getEmail().equals(entity.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
				throw new ConflictException("Email already exists");
			}
		}

		if (request.getUsername() != null) {
			if (!request.getUsername().equals(entity.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
				throw new ConflictException("Username already exists");
			}
		}

		UserMapper.applyUpdate(entity, request);
		return UserMapper.toResponse(userRepository.save(entity));
	}

	/**
	 * Deletes a user record.
	 */
	public void deleteUser(UUID id) {
		User entity = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User not found"));
		userRepository.delete(entity);
	}

}