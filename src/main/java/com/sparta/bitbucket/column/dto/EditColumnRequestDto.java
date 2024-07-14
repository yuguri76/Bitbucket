package com.sparta.bitbucket.column.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EditColumnRequestDto {
	private Long columnId; // 컬럼 ID 추가
	private String title;
	private Integer orders;

	// 생성자 추가
	public EditColumnRequestDto(Long columnId, String title, Integer orders) {
		this.columnId = columnId;
		this.title = title;
		this.orders = orders;
	}
}
