package com.sparta.bitbucket.card.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.board.entity.Board;
import com.sparta.bitbucket.board.service.BoardService;
import com.sparta.bitbucket.card.dto.CardCreateRequestDto;
import com.sparta.bitbucket.card.dto.CardEditRequestDto;
import com.sparta.bitbucket.card.dto.CardMoveRequestDto;
import com.sparta.bitbucket.card.dto.CardResponseDto;
import com.sparta.bitbucket.card.entity.Card;
import com.sparta.bitbucket.card.repository.CardRepository;
import com.sparta.bitbucket.column.entity.Columns;
import com.sparta.bitbucket.column.service.ColumnService;
import com.sparta.bitbucket.common.entity.StatusMessage;
import com.sparta.bitbucket.exception.card.MissingSearchKeywordException;
import com.sparta.bitbucket.exception.card.ResourceNotFoundException;
import com.sparta.bitbucket.exception.card.TitleConflictException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {

	private final CardRepository cardRepository;
	private final BoardService boardService;
	private final ColumnService columnService;

	public CardResponseDto createCard(User user, Long boardId, Long columnId, CardCreateRequestDto requestDto) {
		if (!boardService.isUserBoardMember(boardId, user.getId())) {
			throw new IllegalArgumentException("권한이 없는 유저입니다.");
		}
		existsByColumnIdAndTitle(columnId, requestDto.getTitle());
		Board board = boardService.findBoardById(boardId);
		Columns columns = columnService.findByColumnIdAndBoardId(columnId, boardId);

		Card saveCard = Card.builder()
			.createUser(user)
			.columns(columns)
			.board(board)
			.title(requestDto.getTitle())
			.status(columns.getTitle())
			.assignee(requestDto.getAssignee())
			.content(requestDto.getContent())
			.dueDate(requestDto.getDueDate())
			.orders(requestDto.getOrders())
			.build();

		saveCard = cardRepository.save(saveCard);

		return CardResponseDto
			.builder()
			.card(saveCard)
			.build();
	}

	@Transactional
	public CardResponseDto updateCard(User user, Long boardId, Long columnId,
		Long cardId, CardEditRequestDto requestDto) {

		existsByColumnIdAndTitle(columnId, requestDto.getTitle());

		columnService.findByColumnIdAndBoardId(columnId, boardId);
		Card card = findCard(cardId);

		if (!boardService.isUserManager(user) && !isCardOwner(card.getId(), user.getId())) {
			throw new IllegalArgumentException("수정 권한이 없습니다");
		}

		card.updateCard(requestDto);

		return CardResponseDto
			.builder()
			.card(card)
			.build();
	}

	@Transactional
	public CardResponseDto moveCard(User user, Long boardId, Long columnId, Long cardId,
		CardMoveRequestDto requestDto) {

		columnService.findByColumnIdAndBoardId(columnId, boardId);
		Card card = findCard(cardId);

		if (!boardService.isUserManager(user) && !isCardOwner(card.getId(), user.getId())) {
			throw new IllegalArgumentException("수정 권한이 없습니다");
		}

		card.updateOrders(requestDto);

		return CardResponseDto
			.builder()
			.card(card)
			.build();
	}

	public void deleteCard(User user, Long boardId, Long columnId, Long cardId) {

		columnService.findByColumnIdAndBoardId(columnId, boardId);
		Card card = findCard(cardId);
		if (!boardService.isUserManager(user) && !isCardOwner(card.getId(), user.getId())) {
			throw new IllegalArgumentException("수정 권한이 없습니다");
		}
		cardRepository.delete(card);
	}

	public List<CardResponseDto> getCards(Long boardId, String condition, String conditionDetail) {
		if (condition.equals("assignee")) {
			if (conditionDetail.isEmpty()) {
				throw new MissingSearchKeywordException(StatusMessage.MISSING_SEARCH_KEY_WORD);
			}
			List<Card> assigneeCards = cardRepository.findByAssignee(conditionDetail);
			if (assigneeCards.isEmpty()) {
				throw new ResourceNotFoundException(StatusMessage.REASOURCE_NOT_FOUND);
			} else {
				return assigneeCards.stream().map(CardResponseDto::new).collect(Collectors.toList());
			}
		} else if (condition.equals("status")) {
			if (conditionDetail.isEmpty()) {
				throw new MissingSearchKeywordException(StatusMessage.MISSING_SEARCH_KEY_WORD);
			}
			List<Card> statusCards = cardRepository.findByStatus(conditionDetail);
			if (statusCards.isEmpty()) {
				throw new ResourceNotFoundException(StatusMessage.REASOURCE_NOT_FOUND);
			} else {
				return statusCards.stream().map(CardResponseDto::new).collect(Collectors.toList());
			}
		} else {
			List<Card> allCards = cardRepository.findByBoardId(boardId);
			return allCards.stream().map(CardResponseDto::new).collect(Collectors.toList());
		}
	}

	public Card findCard(Long cardId) {
		return cardRepository.findById(cardId).orElseThrow(
			() -> new EntityNotFoundException(StatusMessage.ENTITY_NOT_FOUND.getMessage())
		);
	}

	private boolean isCardOwner(Long cardId, Long userId) {
		return cardRepository.findByIdAndCreateUser_Id(cardId, userId).isPresent();
	}

	private void existsByColumnIdAndTitle(Long columnId, String title) {
		if (cardRepository.existsByColumnsIdAndTitle(columnId, title)) {
			throw new TitleConflictException(StatusMessage.TITLE_CONFLICT);
		}
	}
}

