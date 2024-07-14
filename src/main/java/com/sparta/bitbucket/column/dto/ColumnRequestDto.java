package com.sparta.bitbucket.column.dto;

import lombok.Getter;

@Getter
public class ColumnRequestDto {

	private Long boardId;

	public ColumnRequestDto(Long boardId) {
		this.boardId = boardId;
	}
}
