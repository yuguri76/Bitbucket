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
import com.sparta.bitbucket.column.repository.ColumnRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {

	private final CardRepository cardRepository;
	private final ColumnRepository columnRepository;
	private final BoardService boardService;

	public CardResponseDto createCard(User user, Long columnId, Long boardId, CardCreateRequestDto requestDto) {
		if (!boardService.isUserBoardMember(boardId, user.getId())) {
			throw new IllegalArgumentException("권한이 없는 유저입니다.");
		}
		existsByColumnIdAndTitle(columnId, requestDto.getTitle());
		Board board = boardService.findBoardById(boardId);
		Columns columns = findColum(columnId);

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
	public CardResponseDto updateCard(User user, Long columnId,
		Long cardId, CardEditRequestDto requestDto) {
		existsByColumnIdAndTitle(columnId, requestDto.getTitle());

		findColum(columnId);
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
	public CardResponseDto moveCard(User user, Long columnId, Long cardId, CardMoveRequestDto requestDto) {
		findColum(columnId);
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

	public void deleteCard(User user, Long columnId, Long cardId) {
		findColum(columnId);
		Card card = findCard(cardId);
		if (!boardService.isUserManager(user) && !isCardOwner(card.getId(), user.getId())) {
			throw new IllegalArgumentException("수정 권한이 없습니다");
		}
		cardRepository.delete(card);
	}

	public List<CardResponseDto> getCards(Long boardId, String condition, String conditionDetail) {
		if (condition.equals("assignee")) {
			if (conditionDetail.isEmpty()) {
				throw new IllegalArgumentException("작업자를 입력해주세요");
			}
			List<Card> assigneeCards = cardRepository.findByAssignee(conditionDetail);
			if (assigneeCards.isEmpty()) {
				throw new IllegalArgumentException("해당 작업자는 존재하지 않는 작업자입니다");
			} else {
				return assigneeCards.stream().map(CardResponseDto::new).collect(Collectors.toList());
			}
		} else if (condition.equals("status")) {
			if (conditionDetail.isEmpty()) {
				throw new IllegalArgumentException("컬럼 상태를 입력해주세요");
			}
			List<Card> statusCards = cardRepository.findByStatus(conditionDetail);
			if (statusCards.isEmpty()) {
				throw new IllegalArgumentException("해당 상태는 존재하지 않는 상태입니다");
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
			() -> new IllegalArgumentException("조회된 카드의 정보가 없습니다")
		);
	}

	public Columns findColum(Long columnId) {
		return columnRepository.findById(columnId).orElseThrow(
			() -> new IllegalArgumentException("조회된 컬럼의 정보가 없습니다.")
		);
	}

	private boolean isCardOwner(Long cardId, Long userId) {
		return cardRepository.findByIdAndCreateUser_Id(cardId, userId).isPresent();
	}

	private void existsByColumnIdAndTitle(Long columnId, String title) {
		if (cardRepository.existsByColumnsIdAndTitle(columnId, title)) {
			throw new IllegalArgumentException("이미 존재하는 타이틀 입니다."); // CommonException 로 바꿀예정
		}
	}
}

