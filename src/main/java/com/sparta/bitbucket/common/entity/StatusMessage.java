package com.sparta.bitbucket.common.entity;

import lombok.Getter;

@Getter
public enum StatusMessage {

	CREATE_COLUMNS_SUCCESS("컬럼 생성에 성공"),
	DELETE_COLUMNS_SUCCESS("컬럼 삭제에 성공"),
	UPDATE_COLUMNS_SUCCESS("컬럼 수정에 성공"),
	GET_LIST_COLUMNS_SUCCESS("컬럼 목록 조회에 성공"),

	//error
	USER_EMAIL_NOT_FOUND("해당 이메일이 존재하지 않습니다."),
	PASSWORD_INVALID("비밀번호가 일치하지 않습니다."),
	USER_EMAIL_DUPLICATE("이미 존재하는 이메일입니다.");

	private final String message;

	StatusMessage(String message) {
		this.message = message;
	}
}
