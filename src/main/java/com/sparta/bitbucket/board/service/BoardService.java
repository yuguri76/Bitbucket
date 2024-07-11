package com.sparta.bitbucket.board.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sparta.bitbucket.auth.entity.Role;
import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.board.dto.BoardCreateRequestDto;
import com.sparta.bitbucket.board.dto.BoardResponseDto;
import com.sparta.bitbucket.board.entity.Board;
import com.sparta.bitbucket.board.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final static int PAGE_SIZE = 5;

	private final BoardRepository boardRepository;

	public List<BoardResponseDto> getAllBoards(int page, String sortBy) {

		Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

		Page<BoardResponseDto> boardResponseDtoPage = boardRepository.findAll(pageable).map(BoardResponseDto::new);
		List<BoardResponseDto> boardResponseDtoList = boardResponseDtoPage.getContent();

		if (boardResponseDtoList.isEmpty()) {
			throw new IllegalArgumentException("조회된 보드가 없습니다.");
		}

		return boardResponseDtoList;
	}

	public BoardResponseDto createBoard(BoardCreateRequestDto requestDto, User user) {

		if (user.getRole() != Role.MANAGER) {
			throw new IllegalArgumentException("로그인한 사용자는 매니저가 아닙니다.");
		}

		Board board = Board.builder()
			.title(requestDto.getTitle())
			.content(requestDto.getContent())
			.user(user)
			.build();

		Board saveBoard = boardRepository.save(board);

		return BoardResponseDto.builder()
			.board(saveBoard)
			.build();
	}
}
