package com.sparta.bitbucket.common.dto;

import lombok.Getter;

@Getter
public class DataResponseDto<T> extends MessageResponseDto {

	private T data;

	// 기본 생성자
	public DataResponseDto() {
	}

	// 매개변수가 있는 생성자
	public DataResponseDto(int statusCode, String message, T data) {
		super(statusCode, message);
		this.data = data;
	}
}
