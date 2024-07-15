package com.sparta.bitbucket.common.exception.auth;

import com.sparta.bitbucket.common.entity.ErrorMessage;

public class UsernameDuplicateException extends RuntimeException {

	public UsernameDuplicateException(String message) {
		super(message);
	}

	public UsernameDuplicateException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}