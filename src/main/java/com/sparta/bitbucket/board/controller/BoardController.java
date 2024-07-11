package com.sparta.bitbucket.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.bitbucket.board.dto.BoardCreateRequestDto;
import com.sparta.bitbucket.board.dto.BoardResponseDto;
import com.sparta.bitbucket.board.service.BoardService;
import com.sparta.bitbucket.common.dto.DataResponseDto;
import com.sparta.bitbucket.common.util.ResponseFactory;
import com.sparta.bitbucket.security.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	@PostMapping
	public ResponseEntity<DataResponseDto<BoardResponseDto>> createBoard(
		@Valid @RequestBody BoardCreateRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		BoardResponseDto responseDto = boardService.createBoard(requestDto, userDetails.getUser());

		return ResponseFactory.ok(responseDto, null);
	}

}
