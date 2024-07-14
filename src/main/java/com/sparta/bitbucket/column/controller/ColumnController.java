package com.sparta.bitbucket.column.controller;

import static com.sparta.bitbucket.common.entity.StatusMessage.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.bitbucket.column.dto.ColumnRequestDto;
import com.sparta.bitbucket.column.dto.ColumnResponseDto;
import com.sparta.bitbucket.column.dto.CreateColumnRequestDto;
import com.sparta.bitbucket.column.dto.EditColumnRequestDto;
import com.sparta.bitbucket.column.service.ColumnService;
import com.sparta.bitbucket.common.dto.DataResponseDto;
import com.sparta.bitbucket.common.dto.MessageResponseDto;
import com.sparta.bitbucket.common.util.ResponseFactory;
import com.sparta.bitbucket.security.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class ColumnController {

	private final ColumnService columnService;

	@PostMapping("/columns")
	public ResponseEntity<MessageResponseDto> createColumn(@RequestBody @Valid CreateColumnRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		columnService.createColumn(userDetails.getUser(), requestDto);
		return ResponseFactory.ok(CREATE_COLUMNS_SUCCESS.getMessage());
	}

	@DeleteMapping("/columns/{columnId}")
	public ResponseEntity<MessageResponseDto> deleteColumn(@PathVariable("columnId") Long columnId,
		@RequestBody @Valid ColumnRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		columnService.deleteColumn(columnId, userDetails.getUser(), requestDto);
		return ResponseFactory.ok(DELETE_COLUMNS_SUCCESS.getMessage());
	}

	@PatchMapping("/columns/{columnId}")
	public ResponseEntity<MessageResponseDto> updateColumn(@PathVariable("columnId") Long columnId,
		@RequestBody @Valid EditColumnRequestDto editColumnRequestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		columnService.updateColumn(columnId, userDetails.getUser(), editColumnRequestDto);
		return ResponseFactory.ok(UPDATE_COLUMNS_SUCCESS.getMessage());
	}

	@GetMapping("/{boardId}/columns")
	public ResponseEntity<DataResponseDto<List<ColumnResponseDto>>> getAllColumns(
		@PathVariable Long boardId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		ColumnRequestDto requestDto = new ColumnRequestDto(boardId);
		return ResponseFactory.ok(columnService.getAllColumns(userDetails.getUser(), requestDto),
			GET_LIST_COLUMNS_SUCCESS.getMessage());
	}
}
