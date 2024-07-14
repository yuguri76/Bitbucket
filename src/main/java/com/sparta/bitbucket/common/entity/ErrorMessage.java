package com.sparta.bitbucket.common.entity;

import lombok.Getter;

@Getter
public enum ErrorMessage {

	// auth
	USER_EMAIL_NOT_FOUND("해당 이메일이 존재하지 않습니다."),
	PASSWORD_INVALID("비밀번호가 일치하지 않습니다."),
	USER_EMAIL_DUPLICATE("이미 존재하는 이메일입니다."),
	UNAUTHORIZED_MANAGER("매니저 권한이 없는 사용자입니다."),
	UNAUTHORIZED_BOARD_MEMBER("해당 보드의 멤버가 아닌 사용자입니다."),
	UNAUTHORIZED_CARD_OWNER("해당 카드의 작성자가 아닌 사용자입니다."),

	// board
	BOARD_TITLE_DUPLICATE("이미 존재하는 보드 제목입니다."),
	BOARD_MEMBER_DUPLICATE("해당 유저는 이미 존재하는 보드 멤버입니다."),
	NOT_FOUND_BOARD("조회된 보드의 정보가 없습니다."),

	// Columns
	COLUMN_NOT_FOUND("존재하지 않는 컬럼입니다."),
	TITLE_ALREADY_EXISTS("이미 존재하는 타이틀입니다."),
	NOT_BOARD_MEMBER("보드 멤버가 아닙니다."),
	FORBIDDEN("유저 권한이 없습니다"),

	// card
	MISSING_SEARCH_KEY_WORD("검색어를 입력해주세요. "),
	RESOURCE_NOT_FOUND("해당하는 정보가 없습니다"),
	NOT_FOUND_CARD("조회된 카드의 정보가 없습니다"),
	CARD_TITLE_CONFLICT("이미 존재하는 타이틀 입니다."),

	// comment
	NOT_FOUND_COMMENT("해당 댓글을 찾을 수 없습니다.");

	private final String message;

	ErrorMessage(String message) {
		this.message = message;
	}
}
