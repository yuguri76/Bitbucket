package com.sparta.bitbucket.column.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ColumnResponseDto {

	private final Long columnId;
	private final String title;
	private final int orders;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	@Builder
	public ColumnResponseDto(Long columnId, String title, int orders, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.columnId = columnId;
		this.title = title;
		this.orders = orders;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
