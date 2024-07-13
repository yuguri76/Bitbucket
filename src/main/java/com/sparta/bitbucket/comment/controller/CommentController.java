package com.sparta.bitbucket.comment.controller;

import com.sparta.bitbucket.comment.dto.CommentRequestDto;
import com.sparta.bitbucket.comment.dto.CommentResponseDto;
import com.sparta.bitbucket.comment.service.CommentService;
import com.sparta.bitbucket.common.dto.DataResponseDto;
import com.sparta.bitbucket.common.util.ResponseFactory;
import com.sparta.bitbucket.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards/{boardId}/columns/{columnId}/cards/{cardId}/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<DataResponseDto<CommentResponseDto>> createComment(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long boardId,
		@PathVariable Long columnId,
		@PathVariable Long cardId,
		@Valid @RequestBody CommentRequestDto requestDto
	) {
		CommentResponseDto responseDto = commentService.createComment(userDetails.getUser(), cardId, requestDto);
		return ResponseFactory.created(responseDto, "댓글 작성이 성공적으로 완료되었습니다.");
	}

	@GetMapping("/{commentId}")
	public ResponseEntity<DataResponseDto<CommentResponseDto>> getComment(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long boardId,
		@PathVariable Long columnId,
		@PathVariable Long cardId,
		@PathVariable Long commentId
	) {
		CommentResponseDto responseDto = commentService.getComment(userDetails.getUser(), commentId, cardId);
		return ResponseFactory.ok(responseDto, "댓글 조회 성공");
	}

	@GetMapping
	public ResponseEntity<DataResponseDto<List<CommentResponseDto>>> getComments(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long boardId,
		@PathVariable Long columnId,
		@PathVariable Long cardId
	) {
		List<CommentResponseDto> responseDtos = commentService.getComments(userDetails.getUser(), cardId);
		return ResponseFactory.ok(responseDtos, "카드별 댓글 목록 조회가 성공적으로 완료되었습니다.");
	}
}
