package com.sparta.bitbucket.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardRequestDto {

	@NotBlank(message = "보드 제목입력은 필수입니다. 보드 제목을 입력해주세요.")
	private String title;

	@NotBlank(message = "보드 한줄 소개 입력은 필수입니다. 보드 한줄 소개를 입력해주세요.")
	private String content;
}
