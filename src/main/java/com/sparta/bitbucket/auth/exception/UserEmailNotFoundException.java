package com.sparta.bitbucket.auth.exception;

import com.sparta.bitbucket.common.entity.StatusMessage;

public class UserEmailNotFoundException extends RuntimeException {
	public UserEmailNotFoundException(String message) {
		super(message);
	}

	public UserEmailNotFoundException(StatusMessage statusMessage) {
		super(statusMessage.getMessage());
	}
}
