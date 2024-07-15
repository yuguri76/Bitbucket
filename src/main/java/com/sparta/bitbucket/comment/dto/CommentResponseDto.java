package com.sparta.bitbucket.comment.dto;

import java.time.LocalDateTime;
import com.sparta.bitbucket.comment.entity.Comment;
import lombok.Getter;

/**
 * 댓글 응답 DTO
 */
@Getter
public class CommentResponseDto {
	private final Long id;
	private final String content;
	private final Long userId;

	private final String username;
	private final LocalDateTime createdAt;

	public CommentResponseDto(Comment comment) {
		this.id = comment.getId();
		this.content = comment.getContent();
		this.userId = comment.getUser().getId();
		this.username = comment.getUser().getName();
		this.createdAt = comment.getCreatedAt();
	}
}