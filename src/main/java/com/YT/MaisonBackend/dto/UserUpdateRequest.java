package com.YT.MaisonBackend.dto;

import com.YT.MaisonBackend.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String password;
	private User.Role role;
}