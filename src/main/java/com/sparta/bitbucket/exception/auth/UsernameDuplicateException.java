package com.sparta.bitbucket.exception.auth;

import com.sparta.bitbucket.common.entity.StatusMessage;

public class UsernameDuplicateException extends RuntimeException {

	public UsernameDuplicateException(String message) {
		super(message);
	}

	public UsernameDuplicateException(StatusMessage statusMessage) {
		super(statusMessage.getMessage());
	}
}