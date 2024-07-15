package com.sparta.bitbucket.column.dto;

import lombok.Getter;

@Getter
public class ColumnRequestDto {

	private final Long boardId;

	public ColumnRequestDto(Long boardId) {
		this.boardId = boardId;
	}
}
