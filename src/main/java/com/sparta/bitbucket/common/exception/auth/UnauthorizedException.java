package com.sparta.bitbucket.common.exception.auth;

import com.sparta.bitbucket.common.entity.ErrorMessage;

public class UnauthorizedException extends RuntimeException {

	public UnauthorizedException(String message) {
		super(message);
	}

	public UnauthorizedException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
