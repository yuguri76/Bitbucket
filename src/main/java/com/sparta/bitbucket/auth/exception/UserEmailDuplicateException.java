package com.sparta.bitbucket.auth.exception;

import com.sparta.bitbucket.common.entity.StatusMessage;

public class UserEmailDuplicateException extends RuntimeException {

	public UserEmailDuplicateException(String message) {
		super(message);
	}

	public UserEmailDuplicateException(StatusMessage statusMessage) {
		super(statusMessage.getMessage());
	}
}
