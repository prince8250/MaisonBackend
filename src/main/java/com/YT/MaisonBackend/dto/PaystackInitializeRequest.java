package com.YT.MaisonBackend.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaystackInitializeRequest {

	private UUID roomAllocationId;
	private String callbackUrl;
}