package com.sparta.bitbucket.card.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class CardMoveRequestDto {
	private Long columnId;

	private List<CardOrderDto> cardOrderList;
}
