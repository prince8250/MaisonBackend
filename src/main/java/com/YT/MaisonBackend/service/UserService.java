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
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional(readOnly = true)
	public List<UserResponse> findAllUsers() {
		return userRepository.findAll().stream().map(UserMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public UserResponse findUserById(UUID id) {
		return userRepository.findById(id)
				.map(UserMapper::toResponse)
				.orElseThrow(() -> new NotFoundException("User not found"));
	}

	public UserResponse createUser(UserCreateRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new ConflictException("Email already exists");
		}

		return UserMapper.toResponse(userRepository.save(UserMapper.toEntity(request)));
	}

	public UserResponse updateUser(UUID id, UserUpdateRequest request) {
		User entity = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User not found"));

		if (request.getEmail() != null) {
			if (!request.getEmail().equals(entity.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
				throw new ConflictException("Email already exists");
			}
		}

		UserMapper.applyUpdate(entity, request);
		return UserMapper.toResponse(userRepository.save(entity));
	}

	public void deleteUser(UUID id) {
		User entity = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User not found"));
		userRepository.delete(entity);
	}

	private void applyRequest(User user, String firstName, String lastName, String username, String email, String password, User.Role role) {
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password);
		user.setRole(role);
	}

	private UserResponse toResponse(User user) {
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