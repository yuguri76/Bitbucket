package com.sparta.bitbucket.common.dto;

import lombok.Getter;

@Getter
public class NoContentResponseDto {

	private int status;

	public NoContentResponseDto(int status) {
		this.status = status;
	}
}
