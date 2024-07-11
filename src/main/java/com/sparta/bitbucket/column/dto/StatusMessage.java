package com.sparta.bitbucket.column.dto;

import lombok.Getter;

@Getter
public enum StatusMessage {

	CREATE_COLUMNS_SUCCESS("컬럼을 생성하는데 성공");

	private final String message;

	StatusMessage(String message) {
		this.message = message;
	}
}
