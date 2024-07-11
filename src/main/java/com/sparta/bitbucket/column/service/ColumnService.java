package com.sparta.bitbucket.column.service;

import org.springframework.stereotype.Service;

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
		validationColumn(boardId, requestDto.getTitle(), user);
		Board board = boardRepository.findById(boardId);

		if (board.getUser.getId() != user.getId()) {
			throw new IllegalArgumentException("유저아이디가 다릅니다."); // CommonException 로 바꿀예정

		}

		columnRepository.save(Columns.builder()
			.title(requestDto.getTitle())
			.board(board)
			.orders(requestDto.getOrders())
			.build());
	}

	public void validationColumn(Long boardId, String title, User user) {
		if (user.getRole() != Role.MANAGER) {
			throw new IllegalArgumentException("유저아이디가 다릅니다."); // CommonException 로 바꿀예정
		}

		if (columnRepository.existsByBoardIdAndTitle(boardId, title)) {
			throw new IllegalArgumentException("존재하는 컬럼 아이디입니다."); // CommonException 로 바꿀예정
		}
	}
}
