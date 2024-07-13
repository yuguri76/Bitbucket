package com.sparta.bitbucket.card.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.bitbucket.card.dto.CardCreateRequestDto;
import com.sparta.bitbucket.card.dto.CardEditRequestDto;
import com.sparta.bitbucket.card.dto.CardMoveRequestDto;
import com.sparta.bitbucket.card.dto.CardResponseDto;
import com.sparta.bitbucket.card.service.CardService;
import com.sparta.bitbucket.common.dto.DataResponseDto;
import com.sparta.bitbucket.common.util.ResponseFactory;
import com.sparta.bitbucket.security.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/boards/{boardId}")
@RequiredArgsConstructor
public class CardController {
	private final CardService cardService;

	@PostMapping("/columns/{columnId}/cards")
	public ResponseEntity<DataResponseDto<CardResponseDto>> createCard(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long boardId,
		@PathVariable Long columnId,
		@Valid @RequestBody CardCreateRequestDto requestDto
	) {
		CardResponseDto responseDto = cardService.createCard(userDetails.getUser(), boardId, columnId, requestDto);

		return ResponseFactory.created(responseDto, "카드 작성이 성공적으로 완료되었습니다.");
	}

	@PutMapping("/columns/{columnId}/cards/{cardId}")
	public ResponseEntity<DataResponseDto<CardResponseDto>> updateCard(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long boardId,
		@PathVariable Long columnId,
		@PathVariable Long cardId,
		@Valid @RequestBody CardEditRequestDto requestDto
	) {
		CardResponseDto responseDto = cardService.updateCard(userDetails.getUser(), boardId, columnId, cardId, requestDto);

		return ResponseFactory.ok(responseDto, "카드 수정이 성공적으로 완료되었습니다.");
	}

	@PutMapping("/columns/{columnId}/cards/{cardId}/move")
	public ResponseEntity<DataResponseDto<CardResponseDto>> moveCard(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long boardId,
		@PathVariable Long columnId,
		@PathVariable Long cardId,
		@Valid @RequestBody CardMoveRequestDto requestDto
	) {
		CardResponseDto responseDto = cardService.moveCard(userDetails.getUser(), boardId, columnId, cardId, requestDto);

		return ResponseFactory.ok(responseDto, "카드 순서 수정이 성공적으로 완료되었습니다.");
	}


	@DeleteMapping("/columns/{columnId}/cards/{cardId}")
	public ResponseEntity<?> deleteCard(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long boardId,
		@PathVariable Long columnId,
		@PathVariable Long cardId
	) {
		cardService.deleteCard(userDetails.getUser(), boardId, columnId, cardId);
		return ResponseFactory.noContent();
	}

	@GetMapping("/cards")
	public ResponseEntity<DataResponseDto<List<CardResponseDto>>> getCards(
		@PathVariable Long boardId,
		@RequestParam(defaultValue = "all") String condition,
		@RequestParam(required = false) String conditionDetail

	) {
		List<CardResponseDto> response = cardService.getCards(boardId, condition, conditionDetail);

		return ResponseFactory.ok(response, "카드 목록 조회가 성공적으로 완료되었습니다.");
	}

}

