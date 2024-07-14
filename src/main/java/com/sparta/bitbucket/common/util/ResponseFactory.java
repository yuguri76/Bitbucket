package com.sparta.bitbucket.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sparta.bitbucket.common.dto.DataResponseDto;
import com.sparta.bitbucket.common.dto.MessageResponseDto;
import com.sparta.bitbucket.exception.ErrorCode;

/**
 * HTTP 응답을 생성하기 위한 클래스입니다.
 * 이 클래스는 다양한 HTTP 상태 코드에 대한 응답을 생성합니다.
 * 필요에 따라 다른 HTTP 상태 코드에 대한 메서드를 추가할 수 있습니다.
 */
public class ResponseFactory {

	private final static String MSG_OK = "요청이 성공적으로 완료되었습니다.";
	private final static String MSG_CREATED = "요청에 대한 데이터가 생성 되었습니다";
	private final static String MSG_BAD_REQUEST = "해당 요청을 처리할 수 없습니다.";
	private final static String MSG_NOT_FOUND = "요청한 리소스를 찾을 수 없습니다.";
	private final static String MSG_INTERNAL_SERVER_ERROR = "서버 오류가 발생했습니다.";
	private final static String MSG_CONFLICT = "이미 존재하는 항목입니다.";
	private final static String MSG_UNAUTHORIZED = "요청에 대해 인증을 실패했습니다.";

	private final static int STATUS_OK = HttpStatus.OK.value();
	private final static int STATUS_CREATED = HttpStatus.CREATED.value();
	private final static int STATUS_NO_CONTENT = HttpStatus.NO_CONTENT.value();
	private final static int STATUS_BAD_REQUEST = HttpStatus.BAD_REQUEST.value();
	private final static int STATUS_NOT_FOUND = HttpStatus.NOT_FOUND.value();
	private final static int STATUS_CONFLICT = HttpStatus.CONFLICT.value();
	private final static int STATUS_INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();
	private final static int STATUS_UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value();

	/**
	 * 주어진 메시지가 유효하지 않은지 확인합니다.
	 *
	 * @param message 확인할 메시지
	 * @return 메시지가 null이거나 비어있으면 true, 그렇지 않으면 false
	 */
	private static boolean invalidMessage(String message) {
		return message == null || message.isBlank();
	}

	/**
	 * 데이터와 메시지를 포함한 200 OK 응답을 생성합니다.
	 *
	 * @param data    응답에 포함할 데이터
	 * @param message 응답 메시지 (null이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static <T> ResponseEntity<DataResponseDto<T>> ok(T data, String message) {
		String okMessage = invalidMessage(message) ? MSG_OK : message;
		DataResponseDto<T> responseDto = new DataResponseDto<>(STATUS_OK, okMessage, data);
		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 메시지만 포함한 200 OK 응답을 생성합니다.
	 *
	 * @param message 응답 메시지 (null이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<MessageResponseDto> ok(String message) {
		String okMessage = invalidMessage(message) ? MSG_OK : message;
		MessageResponseDto responseDto = new MessageResponseDto(STATUS_OK, okMessage);
		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 데이터와 메시지를 포함한 201  CREATED 응답을 생성합니다.
	 *
	 * @param data    응답에 포함할 데이터
	 * @param message 응답 메시지 (null이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static <T> ResponseEntity<DataResponseDto<T>> created(T data, String message) {
		String okMessage = invalidMessage(message) ? MSG_CREATED : message;
		DataResponseDto<T> responseDto = new DataResponseDto<>(STATUS_CREATED, okMessage, data);
		return ResponseEntity.status(STATUS_CREATED).body(responseDto);
	}

	/**
	 * 메시지만 포함한 201 CREATED 응답을 생성합니다.
	 *
	 * @param message 응답 메시지 (null이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<MessageResponseDto> created(String message) {
		String okMessage = invalidMessage(message) ? MSG_CREATED : message;
		MessageResponseDto responseDto = new MessageResponseDto(STATUS_CREATED, okMessage);
		return ResponseEntity.status(STATUS_CREATED).body(responseDto);
	}

	/**
	 * 204 No Content 응답을 생성합니다.
	 * 이 메서드는 다른 응답 메서드와 달리 본문을 포함하지 않습니다.
	 *
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<?> noContent() {
		return ResponseEntity.status(STATUS_NO_CONTENT).build();
	}

	// 에러 응답을 위한 메서드들

	/**
	 * 400 Bad Request 응답을 생성합니다.
	 *
	 * @param message 에러 메시지 (null이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<MessageResponseDto> badRequest(String message) {
		String errorMessage = invalidMessage(message) ? MSG_BAD_REQUEST : message;
		MessageResponseDto responseDto = new MessageResponseDto(STATUS_BAD_REQUEST, errorMessage);
		return ResponseEntity.badRequest().body(responseDto);
	}

	/**
	 * 404 Not Found 응답을 생성합니다.
	 *
	 * @param message 에러 메시지 (null이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<MessageResponseDto> notFound(String message) {
		String errorMessage = invalidMessage(message) ? MSG_NOT_FOUND : message;
		MessageResponseDto responseDto = new MessageResponseDto(STATUS_NOT_FOUND, errorMessage);
		return ResponseEntity.status(STATUS_NOT_FOUND).body(responseDto);
	}

	/**
	 * 500 Internal Server Error 응답을 생성합니다.
	 *
	 * @param message 에러 메시지 (null이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<MessageResponseDto> internalServerError(String message) {
		String errorMessage = invalidMessage(message) ? MSG_INTERNAL_SERVER_ERROR : message;
		MessageResponseDto responseDto = new MessageResponseDto(STATUS_INTERNAL_SERVER_ERROR, errorMessage);
		return ResponseEntity.status(STATUS_INTERNAL_SERVER_ERROR).body(responseDto);
	}

	/**
	 * 409 Conflict 응답을 생성합니다.
	 *
	 * @param message 에러 메시지 (null이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<MessageResponseDto> conflictError(String message) {
		String errorMessage = invalidMessage(message) ? MSG_CONFLICT : message;
		MessageResponseDto responseDto = new MessageResponseDto(STATUS_CONFLICT, errorMessage);
		return ResponseEntity.status(STATUS_CONFLICT).body(responseDto);
	}

	/**
	 * 401 Unauthorized 응답을 생성합니다.
	 * @param message 에러 메시지 (null이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<MessageResponseDto> authorizedError(String message) {
		String errorMessage = invalidMessage(message) ? MSG_UNAUTHORIZED : message;
		MessageResponseDto responseDto = new MessageResponseDto(STATUS_UNAUTHORIZED, errorMessage);
		return ResponseEntity.status(STATUS_UNAUTHORIZED).body(responseDto);
	}


	public static ResponseEntity<MessageResponseDto> customError(ErrorCode errorCode) {
		String errorMessage = errorCode.getMessage();
		int status = errorCode.getStatus().value();
		return ResponseEntity.status(status).body(new MessageResponseDto(errorCode.getStatus().value(), errorMessage));
	}
}
