package com.sparta.bitbucket.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.sparta.bitbucket.board.entity.Board;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardWithMemberListResponseDto {
	private Long id;
	private String title;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Long userId;
	private List<BoardMemberResponseDto> memberList;

	@Builder
	public BoardWithMemberListResponseDto(Board board, List<BoardMemberResponseDto> memberList) {
		this.id = board.getId();
		this.title = board.getTitle();
		this.content = board.getContent();
		this.createdAt = board.getCreatedAt();
		this.updatedAt = board.getUpdatedAt();
		this.userId = board.getUser().getId();
		this.memberList = memberList;
	}

}
