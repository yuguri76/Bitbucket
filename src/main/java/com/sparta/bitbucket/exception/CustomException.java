package com.sparta.bitbucket.exception;

/**
 * CustomException은 사용자 정의 예외를 처리합니다.
 */
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;

	/**
	 * CustomException 생성자
	 *
	 * @param errorCode 에러 코드
	 */
	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	/**
	 * 에러 코드를 반환합니다.
	 *
	 * @return 에러 코드
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}
}