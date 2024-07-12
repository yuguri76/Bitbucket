package com.sparta.bitbucket.exception;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sparta.bitbucket.common.dto.MessageResponseDto;
import com.sparta.bitbucket.common.util.ResponseFactory;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	// validation exception handler : valid 에러 메세지 클라이언트에 전달
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MessageResponseDto> MethodArgumentNotValidExceptionHandler(
		MethodArgumentNotValidException e) {

		String errorMessages = e.getBindingResult().getAllErrors().stream()
			.map(ObjectError::getDefaultMessage)
			.collect(Collectors.joining(", "));

		return ResponseFactory.badRequest(errorMessages);
	}

	// DB exception handler : DB 저장 에러 메세지 클라이언트에 전달
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<MessageResponseDto> ConstraintViolationExceptionHandler(ConstraintViolationException e) {

		StringBuilder errorMessages = new StringBuilder();

		e.getConstraintViolations().forEach(violation -> {
			errorMessages.append(violation.getPropertyPath() + ": " + violation.getMessage() + "\n");
		});

		return ResponseFactory.badRequest(errorMessages.toString());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<MessageResponseDto> IllegalArgumentExceptionHandler(IllegalArgumentException e) {

		String errorMessage = "Exception caught: " + e.getMessage();

		return ResponseFactory.badRequest(errorMessage);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<MessageResponseDto> UsernameNotFoundExceptionHandler(UsernameNotFoundException e) {

		String errorMessage = "Exception caught: " + e.getMessage();

		return ResponseFactory.notFound(errorMessage);
	}

}
