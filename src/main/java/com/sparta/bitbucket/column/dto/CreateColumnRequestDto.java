package com.sparta.bitbucket.column.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateColumnRequestDto {

	@NotNull
	private String title;

	private int orders;
}
