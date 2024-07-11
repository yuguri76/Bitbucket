package com.sparta.bitbucket.column.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.bitbucket.auth.entity.Role;
import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.board.entity.Board;
import com.sparta.bitbucket.board.service.BoardService;
import com.sparta.bitbucket.column.dto.ColumnRequestDto;
import com.sparta.bitbucket.column.dto.ColumnResponseDto;
import com.sparta.bitbucket.column.dto.CreateColumnRequestDto;
import com.sparta.bitbucket.column.dto.EditColumnRequestDto;
import com.sparta.bitbucket.column.entity.Columns;
import com.sparta.bitbucket.column.repository.ColumnRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ColumnService {

	private final ColumnRepository columnRepository;
	private final BoardService boardService;

	@Transactional
	public void createColumn(User user, CreateColumnRequestDto requestDto) {
		validateUser(user);
		checkUserIsBoardOwner(user, requestDto.getBoardId());
		Board board = boardService.findBoardById(requestDto.getBoardId());

		columnRepository.save(Columns.builder()
			.title(requestDto.getTitle())
			.board(board)
			.orders(requestDto.getOrders())
			.build());
	}

	@Transactional
	public void deleteColumn(Long columnId, User user, ColumnRequestDto requestDto) {
		validateUser(user);
		checkUserIsBoardOwner(user, requestDto.getBoardId());
		Columns byColumnId = findByColumnId(columnId);
		columnRepository.delete(byColumnId);
	}

	@Transactional
	public void updateColumn(Long columnId, User user, EditColumnRequestDto requestDto) {

		validateUser(user);
		checkUserIsBoardOwner(user, requestDto.getBoardId());
		Columns columns = findByColumnId(columnId);

		Columns updatedColumns = columns.toBuilder()
			.title(requestDto.getTitle())
			.orders(requestDto.getOrders())
			.build();

		columnRepository.save(updatedColumns);
	}

	@Transactional(readOnly = true)
	public List<ColumnResponseDto> getAllColumns(User user, ColumnRequestDto requestDto) {
		Long boardId = requestDto.getBoardId();
		Long userId = user.getId();

		if(!boardService.isUserBoardMember(boardId, userId)) {
			throw new IllegalArgumentException("방장이 아니거나 보드 멤버에 없습니다."); // CommonException 바꿀 예정
		}

		return columnRepository.findAllByBoardIdOrderByOrders(boardId).stream()
			.map(val ->
				ColumnResponseDto.builder()
					.columnId(val.getId())
					.title(val.getTitle())
					.orders(val.getOrders())
					.createdAt(val.getCreatedAt())
					.updatedAt(val.getUpdatedAt())
					.build()
			)
			.toList();
	}

	public void validateUser(User user) {
		if (user.getRole() != Role.MANAGER) {
			throw new IllegalArgumentException("유저 권한이 없습니다."); // CommonException 로 바꿀예정
		}
	}

	public void checkUserIsBoardOwner(User user, Long boardId) {
		Board board = boardService.findBoardById(boardId);
		if (!Objects.equals(board.getUser().getId(), user.getId())) {
			throw new IllegalArgumentException("유저 권한이 없습니다."); // CommonException 로 바꿀예정
		}
	}

	public Columns findByColumnId(Long columnId) {
		return columnRepository.findById(columnId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 컬럼입니다.") // CommonException 로 바꿀예정
		);
	}

}
