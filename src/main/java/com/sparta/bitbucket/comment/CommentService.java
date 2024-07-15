package com.sparta.bitbucket.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.card.CardRepository;
import com.sparta.bitbucket.card.entity.Card;
import com.sparta.bitbucket.comment.dto.CommentRequestDto;
import com.sparta.bitbucket.comment.dto.CommentResponseDto;
import com.sparta.bitbucket.comment.entity.Comment;
import com.sparta.bitbucket.common.entity.ErrorMessage;
import com.sparta.bitbucket.common.exception.comment.CustomException;

import lombok.RequiredArgsConstructor;

/**
 * 댓글 서비스
 */
@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final CardRepository cardRepository;

	/**
	 * 새로운 댓글을 생성합니다.
	 *
	 * @param user 사용자 정보
	 * @param cardId 카드 ID
	 * @param requestDto 댓글 요청 DTO
	 * @return 생성된 댓글 응답 DTO
	 */
	@Transactional
	public CommentResponseDto createComment(User user, Long cardId, CommentRequestDto requestDto) {
		Card card = cardRepository.findById(cardId)
			.orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_CARD));

		Comment comment = Comment.builder()
			.user(user)
			.card(card)
			.content(requestDto.getContent())
			.build();

		Comment savedComment = commentRepository.save(comment);

		card.addComment(savedComment);

		return new CommentResponseDto(savedComment);
	}

	/**
	 * 특정 댓글을 조회합니다.
	 *
	 * @param user 사용자 정보
	 * @param commentId 댓글 ID
	 * @param cardId 카드 ID
	 * @return 조회된 댓글 응답 DTO
	 */
	@Transactional(readOnly = true)
	public CommentResponseDto getComment(User user, Long commentId, Long cardId) {
		Comment comment = commentRepository.findByIdAndCardId(commentId, cardId)
			.orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_COMMENT));
		return new CommentResponseDto(comment);
	}

	/**
	 * 카드에 달린 모든 댓글을 조회합니다.
	 *
	 * @param user 사용자 정보
	 * @param cardId 카드 ID
	 * @return 카드별 댓글 목록 응답 DTO
	 */
	@Transactional(readOnly = true)
	public List<CommentResponseDto> getComments(User user, Long cardId) {
		List<Comment> comments = commentRepository.findByCardIdOrderByCreatedAtDesc(cardId);
		return comments.stream().map(CommentResponseDto::new).collect(Collectors.toList());
	}
}