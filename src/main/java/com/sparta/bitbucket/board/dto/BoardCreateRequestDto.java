package com.sparta.bitbucket.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardCreateRequestDto {

	@NotBlank(message = "보드 제목을 입력해주세요.")
	private String title;

	@NotBlank(message = "보드 내용을 입력해주세요.")
	private String content;
}
