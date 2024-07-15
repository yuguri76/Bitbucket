package com.sparta.bitbucket.card.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CardEditRequestDto {
	@NotBlank(message = "제목을 입력해주세요.")
	private String title;

	private String content;
	private String assignee;
	private LocalDate dueDate;
}
