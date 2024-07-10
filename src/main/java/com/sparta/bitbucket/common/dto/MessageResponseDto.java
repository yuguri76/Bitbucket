package com.sparta.bitbucket.common.dto;

import lombok.Getter;

@Getter
public class MessageResponseDto {

	private int statusCode;
	private String message;

	// 기본 생성자
	public MessageResponseDto() {
	}

	// 매개변수가 있는 생성자
	public MessageResponseDto(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}
}
