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
	USER_EMAIL_DUPLICATE("이미 존재하는 이메일입니다."),

	//card Success
	CREATE_CARD_SUCCESS("카드 작성이 성공적으로 완료되었습니다."),
	UPDATE_CARD_SUCCESS("카드 수정이 성공적으로 완료되었습니다."),
	UPDATE_CARD_ORDER_SUCCESS("카드 순서 수정이 성공적으로 완료되었습니다."),
	GET_LIST_CARD_SUCCESS("카드 목록 조회가 성공적으로 완료되었습니다."),

	//card error
	MISSING_SEARCH_KEY_WORD("검색어를 입력해주세요. "),
	REASOURCE_NOT_FOUND("해당하는 정보가 없습니다"),
	NOT_FOUND_CARD("조회된 카드의 정보가 없습니다"),
	CARD_TITLE_CONFLICT("이미 존재하는 타이틀 입니다.");

	private final String message;

	StatusMessage(String message) {
		this.message = message;
	}
}
