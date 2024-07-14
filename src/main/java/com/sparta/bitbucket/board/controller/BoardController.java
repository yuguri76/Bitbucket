package com.sparta.bitbucket.board.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.bitbucket.board.dto.BoardCreateRequestDto;
import com.sparta.bitbucket.board.dto.BoardEditRequestDto;
import com.sparta.bitbucket.board.dto.BoardInviteRequestDto;
import com.sparta.bitbucket.board.dto.BoardMemberResponseDto;
import com.sparta.bitbucket.board.dto.BoardResponseDto;
import com.sparta.bitbucket.board.dto.BoardWithMemberListResponseDto;
import com.sparta.bitbucket.board.service.BoardService;
import com.sparta.bitbucket.common.dto.DataResponseDto;
import com.sparta.bitbucket.common.entity.StatusMessage;
import com.sparta.bitbucket.common.util.ResponseFactory;
import com.sparta.bitbucket.security.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	@GetMapping
	public ResponseEntity<DataResponseDto<List<BoardResponseDto>>> getAllBoards(
		@RequestParam(value = "page", defaultValue = "1") int page,
		@RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy
	) {
		List<BoardResponseDto> responseDtoList = boardService.getAllBoards(page - 1, sortBy);

		return ResponseFactory.ok(responseDtoList, StatusMessage.GET_LIST_BOARD_SUCCESS.getMessage());
	}

	@GetMapping("/{boardId}")
	public ResponseEntity<DataResponseDto<BoardWithMemberListResponseDto>> getBoard(
		@PathVariable(value = "boardId") Long boardId,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		BoardWithMemberListResponseDto responseDto = boardService.getBoard(boardId, userDetails.getUser());

		return ResponseFactory.ok(responseDto, StatusMessage.GET_BOARD_SUCCESS.getMessage());
	}

	@PostMapping
	public ResponseEntity<DataResponseDto<BoardResponseDto>> createBoard(
		@Valid @RequestBody BoardCreateRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		BoardResponseDto responseDto = boardService.createBoard(requestDto, userDetails.getUsername());

		return ResponseFactory.created(responseDto, StatusMessage.CREATE_BOARD_SUCCESS.getMessage());
	}

	@PostMapping("/{boardId}/invite")
	public ResponseEntity<DataResponseDto<BoardMemberResponseDto>> inviteBoard(
		@PathVariable(value = "boardId") Long boardId,
		@Valid @RequestBody BoardInviteRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		BoardMemberResponseDto responseDto = boardService.inviteBoard(boardId, requestDto.getEmail(),
			userDetails.getUser());

		return ResponseFactory.ok(responseDto, StatusMessage.INVITE_BOARD_SUCCESS.getMessage());
	}

	@PutMapping("/{boardId}")
	public ResponseEntity<DataResponseDto<BoardResponseDto>> editBoard(
		@PathVariable(value = "boardId") Long boardId,
		@RequestBody BoardEditRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		BoardResponseDto responseDto = boardService.editBoard(boardId, requestDto, userDetails.getUser());

		return ResponseFactory.ok(responseDto, StatusMessage.UPDATE_BOARD_SUCCESS.getMessage());
	}

	@DeleteMapping("/{boardId}")
	public ResponseEntity<?> deleteBoard(
		@PathVariable(value = "boardId") Long boardId,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		boardService.deleteBoard(boardId, userDetails.getUser());

		return ResponseFactory.noContent();
	}

}
