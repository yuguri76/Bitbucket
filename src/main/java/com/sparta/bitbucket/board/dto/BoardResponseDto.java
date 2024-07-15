package com.sparta.bitbucket.board.dto;

import java.time.LocalDateTime;

import com.sparta.bitbucket.board.entity.Board;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardResponseDto {
	private final Long id;
	private final String title;
	private final String content;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;
	private final Long userId;

	@Builder
	public BoardResponseDto(Board board) {
		this.id = board.getId();
		this.title = board.getTitle();
		this.content = board.getContent();
		this.createdAt = board.getCreatedAt();
		this.updatedAt = board.getUpdatedAt();
		this.userId = board.getUser().getId();
	}
}
