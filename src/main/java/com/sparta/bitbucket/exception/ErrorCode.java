package com.sparta.bitbucket.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ErrorCode는 애플리케이션에서 발생할 수 있는 오류 코드를 정의합니다.
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
	NOT_FOUND_CARD(HttpStatus.NOT_FOUND, "이미 삭제된 카드입니다."),
	NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인 해주세요."),

	// Columns
	COLUMN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 컬럼입니다."),
	TITLE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 타이틀입니다."),
	NOT_BOARD_MEMBER(HttpStatus.FORBIDDEN, "보드 멤버가 아닙니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "유저 권한이 없습니다");


	private final HttpStatus status;
	private final String message;
}