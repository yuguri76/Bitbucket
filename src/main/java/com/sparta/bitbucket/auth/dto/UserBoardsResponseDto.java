package com.sparta.bitbucket.auth.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.sparta.bitbucket.board.entity.Board;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserBoardsResponseDto {

	private final String username;
	private final List<BoardSummaryDto> boards;

	@Builder
	public UserBoardsResponseDto(String username, List<Board> boards) {
		this.username = username;
		this.boards = boards.stream().map(BoardSummaryDto::new).collect(Collectors.toList());
	}
}
