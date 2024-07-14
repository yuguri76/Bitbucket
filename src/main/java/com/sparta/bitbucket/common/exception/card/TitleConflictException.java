package com.sparta.bitbucket.common.exception.card;

import com.sparta.bitbucket.common.entity.ErrorMessage;

public class TitleConflictException extends RuntimeException {

	public TitleConflictException(String message) {
		super(message);
	}

	public TitleConflictException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
