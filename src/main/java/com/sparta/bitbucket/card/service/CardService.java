package com.sparta.bitbucket.card.service;

import org.springframework.stereotype.Service;

import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.auth.repository.UserRepository;
import com.sparta.bitbucket.board.entity.Board;
import com.sparta.bitbucket.board.service.BoardService;
import com.sparta.bitbucket.card.dto.CardCreateRequestDto;
import com.sparta.bitbucket.card.dto.CardResponseDto;
import com.sparta.bitbucket.card.entity.Card;
import com.sparta.bitbucket.card.repository.CardRepository;
import com.sparta.bitbucket.column.entity.Columns;
import com.sparta.bitbucket.column.repository.ColumnRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {

	private final UserRepository userRepository;
	private final CardRepository cardRepository;
	private final ColumnRepository columnRepository;
	private final BoardService boardService;

	public CardResponseDto createCard(User user, Long columnId, Long boardId, CardCreateRequestDto requestDto) {
		if(! boardService.isUserBoardMember(boardId,user.getId())){
			throw new IllegalArgumentException("권한이 없는 유저입니다.");
		}
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

	public Columns findColum(Long columnId) {
		return columnRepository.findById(columnId).orElseThrow(
			() -> new IllegalArgumentException("조회된 컬럼의 정보가 없습니다.")
		);
	}
}
