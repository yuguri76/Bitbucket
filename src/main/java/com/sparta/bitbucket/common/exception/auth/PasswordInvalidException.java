package com.sparta.bitbucket.common.exception.auth;

import com.sparta.bitbucket.common.entity.ErrorMessage;

public class PasswordInvalidException extends RuntimeException {

	public PasswordInvalidException(String message) {
		super(message);
	}

	public PasswordInvalidException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}