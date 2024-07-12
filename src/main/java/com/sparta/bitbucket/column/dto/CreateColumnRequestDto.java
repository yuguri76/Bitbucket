package com.sparta.bitbucket.column.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateColumnRequestDto {

	@NotBlank
	private String title;

	@NotNull
	private Long boardId;

	private int orders;
}
