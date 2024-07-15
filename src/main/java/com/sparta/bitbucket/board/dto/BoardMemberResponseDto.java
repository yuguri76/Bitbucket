package com.sparta.bitbucket.board.dto;

import com.sparta.bitbucket.board.entity.BoardMember;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardMemberResponseDto {
	private final String boardTitle;
	private final String userName;

	@Builder
	public BoardMemberResponseDto(BoardMember boardMember) {
		this.boardTitle = boardMember.getBoard().getTitle();
		this.userName = boardMember.getUser().getName();
	}
}
