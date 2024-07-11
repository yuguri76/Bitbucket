package com.sparta.bitbucket.board.service;

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

	private final BoardRepository boardRepository;

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
