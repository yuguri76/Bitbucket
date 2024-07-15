package com.sparta.bitbucket.column.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateColumnRequestDto {

	@NotBlank
	private String title;

	private int orders;
}
