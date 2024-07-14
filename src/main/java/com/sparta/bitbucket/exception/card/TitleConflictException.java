package com.sparta.bitbucket.exception.card;

import com.sparta.bitbucket.common.entity.StatusMessage;

public class TitleConflictException extends RuntimeException {

	public TitleConflictException(String message) {
		super(message);
	}

	public TitleConflictException(StatusMessage statusMessage) {
		super(statusMessage.getMessage());
	}
}
