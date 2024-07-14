package com.sparta.bitbucket.common.exception.board;

import com.sparta.bitbucket.common.entity.ErrorMessage;

public class BoardTitleDuplicateException extends RuntimeException {

	public BoardTitleDuplicateException(String message) {
		super(message);
	}

	public BoardTitleDuplicateException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
