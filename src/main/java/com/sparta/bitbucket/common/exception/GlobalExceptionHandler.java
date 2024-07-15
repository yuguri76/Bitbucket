package com.sparta.bitbucket.common.exception;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sparta.bitbucket.common.dto.MessageResponseDto;
import com.sparta.bitbucket.common.exception.auth.PasswordInvalidException;
import com.sparta.bitbucket.common.exception.auth.UnauthorizedException;
import com.sparta.bitbucket.common.exception.auth.UsernameDuplicateException;
import com.sparta.bitbucket.common.exception.board.BoardMemberDuplicateException;
import com.sparta.bitbucket.common.exception.board.BoardTitleDuplicateException;
import com.sparta.bitbucket.common.exception.card.MissingSearchKeywordException;
import com.sparta.bitbucket.common.exception.card.ResourceNotFoundException;
import com.sparta.bitbucket.common.exception.card.TitleConflictException;
import com.sparta.bitbucket.common.exception.comment.CustomException;
import com.sparta.bitbucket.common.util.ResponseFactory;

import jakarta.persistence.EntityNotFoundException;
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

	@ExceptionHandler(UsernameDuplicateException.class)
	public ResponseEntity<MessageResponseDto> UsernameDuplicateExceptionHandler(UsernameDuplicateException e) {

		String errorMessage = "Exception caught: " + e.getMessage();

		return ResponseFactory.conflictError(errorMessage);
	}

	@ExceptionHandler(PasswordInvalidException.class)
	public ResponseEntity<MessageResponseDto> PasswordInvalidExceptionHandler(PasswordInvalidException e) {

		String errorMessage = "Exception caught: " + e.getMessage();

		return ResponseFactory.authorizedError(errorMessage);
	}

	@ExceptionHandler(MissingSearchKeywordException.class)
	public ResponseEntity<MessageResponseDto> MissingSearchKeywordExceptionHandler(MissingSearchKeywordException e) {

		String errorMessage = "Exception caught: " + e.getMessage();

		return ResponseFactory.badRequest(errorMessage);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<MessageResponseDto> ResourceNotFoundExceptionHandler(ResourceNotFoundException e) {
		String errorMessage = "Exception caught: " + e.getMessage();

		return ResponseFactory.badRequest(errorMessage);
	}

	@ExceptionHandler(TitleConflictException.class)
	public ResponseEntity<MessageResponseDto> TitleConflictExceptionHandler(TitleConflictException e) {
		String errorMessage = "Exception caught: " + e.getMessage();

		return ResponseFactory.conflictError(errorMessage);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<MessageResponseDto> EntityNotFoundExceptionHandler(EntityNotFoundException e) {
		String errorMessage = "Exception caught: " + e.getMessage();

		return ResponseFactory.notFound(errorMessage);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<MessageResponseDto> CustomExceptionHandler(CustomException e) {
		String errorMessage = "Exception caught: " + e.getMessage();

		return ResponseFactory.notFound(errorMessage);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<MessageResponseDto> UnauthorizedExceptionHandler(UnauthorizedException e) {
		String errorMessage = "Exception caught: " + e.getMessage();

		return ResponseFactory.authorizedError(errorMessage);
	}

	@ExceptionHandler(BoardTitleDuplicateException.class)
	public ResponseEntity<MessageResponseDto> BoardTitleDuplicateExceptionHandler(BoardTitleDuplicateException e) {
		String errorMessage = "Exception caught: " + e.getMessage();

		return ResponseFactory.conflictError(errorMessage);
	}

	@ExceptionHandler(BoardMemberDuplicateException.class)
	public ResponseEntity<MessageResponseDto> BoardMemberDuplicateExceptionHandler(BoardMemberDuplicateException e) {
		String errorMessage = "Exception caught: " + e.getMessage();

		return ResponseFactory.conflictError(errorMessage);
	}
}
