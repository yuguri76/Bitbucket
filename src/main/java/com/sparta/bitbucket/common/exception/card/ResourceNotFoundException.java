package com.sparta.bitbucket.common.exception.card;

import com.sparta.bitbucket.common.entity.ErrorMessage;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
