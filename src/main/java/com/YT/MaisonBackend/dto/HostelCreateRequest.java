package com.YT.MaisonBackend.dto;

import java.util.ArrayList;
import java.util.List;

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
	private String pricing;
	private String location;
	private String facilities;
	private String roomTypes;
	private List<String> imageUrls = new ArrayList<>();
}