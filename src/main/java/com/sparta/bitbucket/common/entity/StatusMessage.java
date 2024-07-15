package com.sparta.bitbucket.common.entity;

import lombok.Getter;

@Getter
public enum StatusMessage {

	// column Success
	CREATE_COLUMNS_SUCCESS("컬럼 생성에 성공"),
	DELETE_COLUMNS_SUCCESS("컬럼 삭제에 성공"),
	UPDATE_COLUMNS_SUCCESS("컬럼 수정에 성공"),
	GET_LIST_COLUMNS_SUCCESS("컬럼 목록 조회에 성공"),

	// board Success
	GET_LIST_BOARD_SUCCESS("보드 목록 조회가 성공적으로 완료되었습니다."),
	GET_BOARD_SUCCESS("보드 단건 조회가 성공적으로 완료되었습니다."),
	CREATE_BOARD_SUCCESS("보드 생성이 성공적으로 완료되었습니다."),
	INVITE_BOARD_SUCCESS("보드 초대가 성공적으로 완료되었습니다."),
	UPDATE_BOARD_SUCCESS("보드 수정이 성공적으로 완료되었습니다."),
	DELETE_BOARD_SUCCESS("보드 삭제가 성공적으로 완료되었습니다."),

	// card Success
	CREATE_CARD_SUCCESS("카드 작성이 성공적으로 완료되었습니다."),
	UPDATE_CARD_SUCCESS("카드 수정이 성공적으로 완료되었습니다."),
	UPDATE_CARD_ORDER_SUCCESS("카드 순서 수정이 성공적으로 완료되었습니다."),
	GET_LIST_CARD_SUCCESS("카드 목록 조회가 성공적으로 완료되었습니다.");

	private final String message;

	StatusMessage(String message) {
		this.message = message;
	}
}
