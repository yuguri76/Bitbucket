package com.sparta.bitbucket.column.dto;

import lombok.Getter;

@Getter
public enum StatusMessage {

	CREATE_COLUMNS_SUCCESS("컬럼 생성에 성공"),
	DELETE_COLUMNS_SUCCESS("컬럼 삭제에 성공"),
	UPDATE_COLUMNS_SUCCESS("컬럼 수정에 성공"),
	GET_LIST_COLUMNS_SUCCESS("컬럼 목록 조회에 성공");


	private final String message;

	StatusMessage(String message) {
		this.message = message;
	}
}
