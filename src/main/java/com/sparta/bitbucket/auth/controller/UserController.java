package com.sparta.bitbucket.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.bitbucket.auth.dto.SignupRequestDto;
import com.sparta.bitbucket.auth.dto.SignupResponseDto;
import com.sparta.bitbucket.auth.dto.UserBoardsResponseDto;
import com.sparta.bitbucket.auth.service.UserService;
import com.sparta.bitbucket.common.util.ResponseFactory;
import com.sparta.bitbucket.security.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/**
	 * 회원 가입 요청을 처리하는 엔드포인트
	 *
	 * @param requestDto 회원 가입에 필요한 정보를 담은 DTO
	 * @return 회원 가입 결과를 담은 ResponseEntity
	 */
	@PostMapping
	public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto requestDto) {

		SignupResponseDto responseDto = userService.signup(requestDto);

		return ResponseFactory.ok(responseDto, null);
	}

	/**
	 * 현재 인증된 사용자의 보드 목록을 조회하는 엔드포인트
	 *
	 * @param userDetails 현재 인증된 사용자의 상세 정보
	 * @return 사용자의 보드 목록을 담은 ResponseEntity
	 */
	@GetMapping
	public ResponseEntity<?> getUserBoards(@AuthenticationPrincipal UserDetailsImpl userDetails) {

		UserBoardsResponseDto responseDto = userService.getUserBoards(userDetails.getUsername());

		return ResponseFactory.ok(responseDto, null);
	}

}
