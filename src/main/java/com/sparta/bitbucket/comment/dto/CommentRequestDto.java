package com.sparta.bitbucket.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {
	@NotBlank(message = "내용을 입력해주세요.")
	private String content;
}