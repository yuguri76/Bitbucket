package com.sparta.bitbucket.global.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageResponseDto {

	private final int statusCode;
	private final String message;

	@Builder
	public MessageResponseDto(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}
}
