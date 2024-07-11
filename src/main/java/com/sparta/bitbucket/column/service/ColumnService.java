package com.sparta.bitbucket.column.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.bitbucket.auth.entity.Role;
import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.column.dto.CreateColumnRequestDto;
import com.sparta.bitbucket.column.entity.Columns;
import com.sparta.bitbucket.column.repository.ColumnRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ColumnService {

	private final ColumnRepository columnRepository;
	private final BoardRepository boardRepository;

	@Transactional
	public void createColumn(Long boardId, CreateColumnRequestDto requestDto, User user) {
		Board board = validateUserAndBoard(boardId, user);
		checkColumnTitleExists(boardId, requestDto.getTitle());

		columnRepository.save(Columns.builder()
			.title(requestDto.getTitle())
			.board(board)
			.orders(requestDto.getOrders())
			.build());
	}

	@Transactional
	public void deleteColumn(Long boardId, Long columnId, User user) {
		validateUserAndBoard(boardId, user);

		Columns byColumnId = findByColumnId(columnId);
		columnRepository.delete(byColumnId);
	}

	public Board validateUserAndBoard(Long boardId, User user) {
		if (user.getRole() != Role.MANAGER) {
			throw new IllegalArgumentException("유저 권한이 없습니다."); // CommonException 로 바꿀예정
		}

		Board board = boardRepository.findById(boardId);

		if (board.getUser.getId() != user.getId()) {
			throw new IllegalArgumentException("유저아이디가 다릅니다."); // CommonException 로 바꿀예정
		}

		return board;
	}

	public void checkColumnTitleExists(Long boardId, String title) {
		if (columnRepository.existsByBoardIdAndTitle(boardId, title)) {
			throw new IllegalArgumentException("존재하는 컬럼 아이디입니다."); // CommonException 로 바꿀예정
		}
	}

	public Columns findByColumnId(Long columnId) {
		return columnRepository.findById(columnId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 컬럼입니다.") // CommonException 로 바꿀예정
		);
	}
}
