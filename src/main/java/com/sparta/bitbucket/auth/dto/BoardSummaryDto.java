package com.sparta.bitbucket.auth.dto;

import com.sparta.bitbucket.board.entity.Board;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardSummaryDto {
	private Long id;
	private String title;

	@Builder
	public BoardSummaryDto(Board board) {
		this.id = board.getId();
		this.title = board.getTitle();
	}
}
