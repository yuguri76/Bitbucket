package com.sparta.bitbucket.card.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sparta.bitbucket.card.entity.Card;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CardResponseDto {
	private final Long id;
	private final String status;
	private final String assignee;
	private final String title;
	private final String content;
	private final Long orders;
	private final LocalDate dueDate;
	private final LocalDateTime updatedAt;

	@Builder
	public CardResponseDto(Card card){
		this.id = card.getId();
		this.status = card.getStatus();
		this.assignee = card.getAssignee();
		this.title = card.getTitle();
		this.content = card.getContent();
		this.dueDate = card.getDueDate();
		this.orders = card.getOrders();
		this.updatedAt = card.getUpdatedAt();
	}
}
