package com.sparta.bitbucket.global.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DataResponseDto<T> {

	private final int statusCode;
	private final String message;
	private final T data;

	@Builder
	public DataResponseDto(int statusCode, String message, T data) {
		this.statusCode = statusCode;
		this.message = message;
		this.data = data;
	}
}
