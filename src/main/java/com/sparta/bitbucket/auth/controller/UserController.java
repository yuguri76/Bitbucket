package com.sparta.bitbucket.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.bitbucket.auth.dto.SignupRequestDto;
import com.sparta.bitbucket.auth.dto.SignupResponseDto;
import com.sparta.bitbucket.auth.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// 회원 가입 요청을 처리하는 엔드포인트
	@PostMapping
	public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto requestDto) {

		SignupResponseDto responseDto = userService.signup(requestDto);

		return ResponseEntity.ok().body(responseDto);
	}

}
