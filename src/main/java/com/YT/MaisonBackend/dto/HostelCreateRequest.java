package com.YT.MaisonBackend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HostelCreateRequest {
	private String phoneNumber;
	private String name;
	private String address;
	private Integer capacity;
}