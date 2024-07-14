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
@RequestMapping("/api/boards/{boardId}")
@RequiredArgsConstructor
public class ColumnController {

	private final ColumnService columnService;

	@PostMapping("/columns")
	public ResponseEntity<MessageResponseDto> createColumn(@PathVariable("boardId") Long boardId,
		@RequestBody @Valid CreateColumnRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		columnService.createColumn(boardId, userDetails.getUser(), requestDto);
		return ResponseFactory.created(CREATE_COLUMNS_SUCCESS.getMessage());
	}
	@DeleteMapping("/columns/{columnId}")
	public ResponseEntity<MessageResponseDto> deleteColumn(
		@PathVariable("boardId") Long boardId,
		@PathVariable("columnId") Long columnId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		try {
			columnService.deleteColumn(columnId, userDetails.getUser(), boardId);
			return ResponseFactory.ok(DELETE_COLUMNS_SUCCESS.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body(new MessageResponseDto(500, "Failed to delete column"));
		}
	}

	@PatchMapping("/columns/{columnId}")
	public ResponseEntity<MessageResponseDto> updateColumn(@PathVariable("boardId") Long boardId,
		@PathVariable("columnId") Long columnId,
		@RequestBody @Valid EditColumnRequestDto editColumnRequestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		columnService.updateColumn(boardId, columnId, userDetails.getUser(), editColumnRequestDto);
		return ResponseFactory.ok(UPDATE_COLUMNS_SUCCESS.getMessage());
	}

	@PatchMapping("/columns/order")
	public ResponseEntity<MessageResponseDto> updateColumnOrder(
		@PathVariable("boardId") Long boardId,
		@RequestBody @Valid List<EditColumnRequestDto> orderRequestDtos,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		columnService.updateColumnOrder(boardId, userDetails.getUser(), orderRequestDtos);
		return ResponseFactory.ok("Column order updated successfully");
	}

	@GetMapping("/columns")
	public ResponseEntity<DataResponseDto<List<ColumnResponseDto>>> getAllColumns(
		@PathVariable("boardId") Long boardId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseFactory.ok(columnService.getAllColumns(userDetails.getUser(), boardId),
			GET_LIST_COLUMNS_SUCCESS.getMessage());
	}
}
