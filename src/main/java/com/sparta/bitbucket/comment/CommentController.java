package com.sparta.bitbucket.comment;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.bitbucket.comment.dto.CommentRequestDto;
import com.sparta.bitbucket.comment.dto.CommentResponseDto;
import com.sparta.bitbucket.common.dto.DataResponseDto;
import com.sparta.bitbucket.common.util.ResponseFactory;
import com.sparta.bitbucket.security.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * CommentController는 댓글 관련 요청을 처리합니다.
 */
@RestController
@RequestMapping("/api/boards/{boardId}/columns/{columnId}/cards/{cardId}/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	/**
	 * 새로운 댓글을 생성합니다.
	 *
	 * @param userDetails 인증된 사용자 정보
	 * @param boardId 게시판 ID
	 * @param columnId 칼럼 ID
	 * @param cardId 카드 ID
	 * @param requestDto 생성할 댓글의 세부 정보
	 * @return 생성된 댓글을 포함한 응답 엔티티
	 */
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

	/**
	 * 특정 댓글을 조회합니다.
	 *
	 * @param userDetails 인증된 사용자 정보
	 * @param boardId 게시판 ID
	 * @param columnId 칼럼 ID
	 * @param cardId 카드 ID
	 * @param commentId 댓글 ID
	 * @return 조회된 댓글을 포함한 응답 엔티티
	 */
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

	/**
	 * 카드에 달린 모든 댓글을 조회합니다.
	 *
	 * @param userDetails 인증된 사용자 정보
	 * @param boardId 게시판 ID
	 * @param columnId 칼럼 ID
	 * @param cardId 카드 ID
	 * @return 카드별 댓글 목록을 포함한 응답 엔티티
	 */
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