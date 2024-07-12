package com.sparta.bitbucket.column.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EditColumnRequestDto {

	private String title;
	@NotNull
	private Long boardId;
	private Integer orders;
}
