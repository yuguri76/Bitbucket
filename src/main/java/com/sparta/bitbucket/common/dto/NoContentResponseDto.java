package com.sparta.bitbucket.common.dto;

import lombok.Getter;

@Getter
public class NoContentResponseDto {

	private final int status;

	public NoContentResponseDto(int status) {
		this.status = status;
	}
}
