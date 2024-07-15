package com.sparta.bitbucket.common.exception.comment;

import com.sparta.bitbucket.common.entity.ErrorMessage;

/**
 * CustomException은 사용자 정의 예외를 처리합니다.
 */
public class CustomException extends RuntimeException {

	public CustomException(String message) {
		super(message);
	}

	/**
	 * CustomException 생성자
	 *
	 * @param errorMessage 에러 메세지
	 */
	public CustomException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}