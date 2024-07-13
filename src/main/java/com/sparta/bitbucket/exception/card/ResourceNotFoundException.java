package com.sparta.bitbucket.exception.card;

import com.sparta.bitbucket.common.entity.StatusMessage;

public class ResourceNotFoundException extends RuntimeException{

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(StatusMessage statusMessage) {
		super(statusMessage.getMessage());
	}
}
