package com.sparta.bitbucket.common.dto;

import lombok.Getter;

@Getter
public class NoContentResponseDto {

	private int statusCode;

	public NoContentResponseDto(int statusCode) {
		this.statusCode = statusCode;
	}
}
