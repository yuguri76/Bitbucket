package com.sparta.bitbucket.column.controller;

import static com.sparta.bitbucket.column.dto.StatusMessage.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.bitbucket.column.dto.CreateColumnRequestDto;
import com.sparta.bitbucket.column.service.ColumnService;
import com.sparta.bitbucket.common.util.ResponseFactory;
import com.sparta.bitbucket.security.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController("/api/boards")
@RequiredArgsConstructor
public class ColumnController {

	private final ColumnService columnService;

	@PostMapping("/{boardId}/columns")
	public ResponseEntity<?> createColumn(@PathVariable("boardId") Long boardId,
		@RequestBody @Valid CreateColumnRequestDto requestDto, @AuthenticationPrincipal
	UserDetailsImpl userDetails) {
		columnService.createColumn(boardId, requestDto, userDetails.getUser());
		return ResponseFactory.ok(CREATE_COLUMNS_SUCCESS.getMessage());
	}

}
