package com.sparta.bitbucket.common.exception.card;

import com.sparta.bitbucket.common.entity.ErrorMessage;

public class MissingSearchKeywordException extends RuntimeException {

	public MissingSearchKeywordException(String message) {
		super(message);
	}

	public MissingSearchKeywordException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
