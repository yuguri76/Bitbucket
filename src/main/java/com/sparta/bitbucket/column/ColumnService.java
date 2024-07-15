package com.sparta.bitbucket.column;

import static com.sparta.bitbucket.common.entity.ErrorMessage.*;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.bitbucket.auth.entity.Role;
import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.board.BoardService;
import com.sparta.bitbucket.board.entity.Board;
import com.sparta.bitbucket.column.dto.ColumnResponseDto;
import com.sparta.bitbucket.column.dto.CreateColumnRequestDto;
import com.sparta.bitbucket.column.dto.EditColumnRequestDto;
import com.sparta.bitbucket.column.entity.Columns;
import com.sparta.bitbucket.common.exception.comment.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ColumnService {

	private final ColumnRepository columnRepository;
	private final BoardService boardService;

	@Transactional
	public void createColumn(Long boardId, User user, CreateColumnRequestDto requestDto) {
		validateUser(user);
		checkUserIsBoardOwner(user, boardId);
		existsBoardIdAndTitle(boardId, requestDto.getTitle());

		Board board = boardService.findBoardById(boardId);

		columnRepository.save(Columns.builder()
			.title(requestDto.getTitle())
			.board(board)
			.orders(requestDto.getOrders())
			.build());
	}

	@Transactional
	public void deleteColumn(Long columnId, User user, Long boardId) {
		validateUser(user);
		checkUserIsBoardOwner(user, boardId);
		Columns byColumnId = findByColumnIdAndBoardId(columnId, boardId);
		columnRepository.delete(byColumnId);
	}

	@Transactional
	public void updateColumn(Long boardId, Long columnId, User user, EditColumnRequestDto requestDto) {
		validateUser(user);
		checkUserIsBoardOwner(user, boardId);
		existsBoardIdAndTitle(boardId, requestDto.getTitle());

		Columns columns = findByColumnIdAndBoardId(columnId, boardId);

		columns = columns.toBuilder()
			.title(requestDto.getTitle() == null ? columns.getTitle() : requestDto.getTitle())
			.orders(requestDto.getOrders() == null ? columns.getOrders() : requestDto.getOrders())
			.build();

		columnRepository.save(columns);
	}

	@Transactional
	public void updateColumnOrder(Long boardId, User user, List<EditColumnRequestDto> orderRequestDtos) {
		validateUser(user);
		checkUserIsBoardOwner(user, boardId);

		for (EditColumnRequestDto dto : orderRequestDtos) {
			Columns column = findByColumnIdAndBoardId(dto.getColumnId(), boardId);
			column.setOrders(dto.getOrders());
			columnRepository.save(column); // 수정된 컬럼 저장
		}
	}

	@Transactional(readOnly = true)
	public List<ColumnResponseDto> getAllColumns(User user, Long boardId) {
		if (!boardService.isUserBoardMember(boardId, user.getId())) {
			throw new CustomException(NOT_BOARD_MEMBER);
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
			throw new CustomException(FORBIDDEN);
		}
	}

	public void checkUserIsBoardOwner(User user, Long boardId) {
		Board board = boardService.findBoardById(boardId);
		if (!Objects.equals(board.getUser().getId(), user.getId())) {
			throw new CustomException(FORBIDDEN);
		}
	}

	public Columns findByColumnIdAndBoardId(Long columnId, Long boardId) {
		return columnRepository.findByIdAndBoardId(columnId, boardId).orElseThrow(
			() -> new CustomException(COLUMN_NOT_FOUND)
		);
	}

	private void existsBoardIdAndTitle(Long boardId, String title) {
		if (columnRepository.existsByBoardIdAndTitle(boardId, title)) {
			throw new CustomException(TITLE_ALREADY_EXISTS);
		}
	}
}
