package com.sparta.bitbucket.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sparta.bitbucket.common.dto.DataResponseDto;
import com.sparta.bitbucket.common.dto.MessageResponseDto;
import com.sparta.bitbucket.common.dto.NoContentResponseDto;

/**
 * HTTP 응답을 생성하기 위한 클래스입니다.
 * 이 클래스는 다양한 HTTP 상태 코드에 대한 응답을 생성합니다.
 * 필요에 따라 다른 HTTP 상태 코드에 대한 메서드를 추가할 수 있습니다.
 */
public class ResponseFactory {

	private static final String MSG_OK = "The request has been successfully processed."; // todo : 영어? 한글?
	private static final String MSG_BAD_REQUEST = "The request could not be understood or was missing required parameters.";
	private static final String MSG_NOT_FOUND = "The requested resource could not be found.";
	private static final String MSG_INTERNAL_SERVER_ERROR = "An unexpected condition was encountered.";
	private final static int STATUS_OK = HttpStatus.OK.value();
	private final static int STATUS_NO_CONTENT = HttpStatus.NO_CONTENT.value();
	private final static int STATUS_BAD_REQUEST = HttpStatus.BAD_REQUEST.value();
	private final static int STATUS_NOT_FOUND = HttpStatus.NOT_FOUND.value();
	private final static int STATUS_INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();

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
	 * 204 No Content 응답을 생성합니다.
	 * 이 메서드는 다른 응답 메서드와 달리 본문을 포함하지 않습니다.
	 *
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<NoContentResponseDto> noContent() {
		NoContentResponseDto responseDto = new NoContentResponseDto(STATUS_NO_CONTENT);
		return ResponseEntity.status(STATUS_NO_CONTENT).body(responseDto);
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

}
