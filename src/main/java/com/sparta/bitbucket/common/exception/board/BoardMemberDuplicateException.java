package com.sparta.bitbucket.common.exception.board;

import com.sparta.bitbucket.common.entity.ErrorMessage;

public class BoardMemberDuplicateException extends RuntimeException {

	public BoardMemberDuplicateException(String message) {
		super(message);
	}

	public BoardMemberDuplicateException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
