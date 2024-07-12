package com.sparta.bitbucket.auth.exception;

import com.sparta.bitbucket.common.entity.StatusMessage;

public class PasswordInvalidException extends RuntimeException {
	public PasswordInvalidException(String message) {
		super(message);
	}

	public PasswordInvalidException(StatusMessage statusMessage) {
		super(statusMessage.getMessage());
	}
}
