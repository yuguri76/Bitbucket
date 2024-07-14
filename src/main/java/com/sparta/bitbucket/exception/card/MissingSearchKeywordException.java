package com.sparta.bitbucket.exception.card;

import com.sparta.bitbucket.common.entity.StatusMessage;

public class MissingSearchKeywordException extends RuntimeException {

	public MissingSearchKeywordException(String message) {
		super(message);
	}

	public MissingSearchKeywordException(StatusMessage statusMessage) {
		super(statusMessage.getMessage());
	}
}
