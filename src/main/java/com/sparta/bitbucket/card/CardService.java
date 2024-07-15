package com.sparta.bitbucket.card;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.board.BoardService;
import com.sparta.bitbucket.board.entity.Board;
import com.sparta.bitbucket.card.dto.CardCreateRequestDto;
import com.sparta.bitbucket.card.dto.CardEditRequestDto;
import com.sparta.bitbucket.card.dto.CardMoveRequestDto;
import com.sparta.bitbucket.card.dto.CardOrderDto;
import com.sparta.bitbucket.card.dto.CardResponseDto;
import com.sparta.bitbucket.card.entity.Card;
import com.sparta.bitbucket.column.ColumnService;
import com.sparta.bitbucket.column.entity.Columns;
import com.sparta.bitbucket.common.entity.ErrorMessage;
import com.sparta.bitbucket.common.exception.auth.UnauthorizedException;
import com.sparta.bitbucket.common.exception.card.MissingSearchKeywordException;
import com.sparta.bitbucket.common.exception.card.ResourceNotFoundException;
import com.sparta.bitbucket.common.exception.card.TitleConflictException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {

	private final CardRepository cardRepository;
	private final BoardService boardService;
	private final ColumnService columnService;

	@Transactional
	public CardResponseDto createCard(User user, Long boardId, Long columnId, CardCreateRequestDto requestDto) {
		if (!boardService.isUserBoardMember(boardId, user.getId())) {
			throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED_BOARD_MEMBER);
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

		columns.addCard(saveCard);

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
			throw new UnauthorizedException(
				ErrorMessage.UNAUTHORIZED_MANAGER + " " + ErrorMessage.UNAUTHORIZED_CARD_OWNER);
		}

		card.updateCard(requestDto);

		return CardResponseDto
			.builder()
			.card(card)
			.build();
	}

	@Transactional
	public CardResponseDto moveCard(User user, Long boardId, Long cardId, List<CardMoveRequestDto> requestDtoList) {

		Card card = findCard(cardId);

		if (!boardService.isUserManager(user) && !isCardOwner(card.getId(), user.getId())) {
			throw new UnauthorizedException(
				ErrorMessage.UNAUTHORIZED_MANAGER + " " + ErrorMessage.UNAUTHORIZED_CARD_OWNER);
		}

		for (var request : requestDtoList) {
			Columns columns = columnService.findByColumnIdAndBoardId(request.getColumnId(), boardId);

			for (CardOrderDto orderDto : request.getCardOrderList()) {
				Card orderCard = findCard(orderDto.getId());
				orderCard.updateColumn(columns);
				orderCard.updateOrders(orderDto.getOrders());
			}
		}

		return CardResponseDto
			.builder()
			.card(card)
			.build();
	}

	public void deleteCard(User user, Long boardId, Long columnId, Long cardId) {

		columnService.findByColumnIdAndBoardId(columnId, boardId);
		Card card = findCard(cardId);
		if (!boardService.isUserManager(user) && !isCardOwner(card.getId(), user.getId())) {
			throw new UnauthorizedException(
				ErrorMessage.UNAUTHORIZED_MANAGER + " " + ErrorMessage.UNAUTHORIZED_CARD_OWNER);
		}
		cardRepository.delete(card);
	}

	public List<CardResponseDto> getCards(Long boardId, String condition, String conditionDetail) {
		if (condition.equals("assignee")) {
			if (conditionDetail.isEmpty()) {
				throw new MissingSearchKeywordException(ErrorMessage.MISSING_SEARCH_KEY_WORD);
			}
			List<Card> assigneeCards = cardRepository.findByAssignee(conditionDetail);
			if (assigneeCards.isEmpty()) {
				throw new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND);
			} else {
				return assigneeCards.stream().map(CardResponseDto::new).collect(Collectors.toList());
			}
		} else if (condition.equals("status")) {
			if (conditionDetail.isEmpty()) {
				throw new MissingSearchKeywordException(ErrorMessage.MISSING_SEARCH_KEY_WORD);
			}
			List<Card> statusCards = cardRepository.findByStatus(conditionDetail);
			if (statusCards.isEmpty()) {
				throw new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND);
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
			() -> new EntityNotFoundException(ErrorMessage.NOT_FOUND_CARD.getMessage())
		);
	}

	private boolean isCardOwner(Long cardId, Long userId) {
		return cardRepository.findByIdAndCreateUser_Id(cardId, userId).isPresent();
	}

	private void existsByColumnIdAndTitle(Long columnId, String title) {
		if (cardRepository.existsByColumnsIdAndTitle(columnId, title)) {
			throw new TitleConflictException(ErrorMessage.CARD_TITLE_CONFLICT);
		}
	}
}

