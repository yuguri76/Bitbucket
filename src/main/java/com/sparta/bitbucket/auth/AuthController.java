package com.sparta.bitbucket.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.bitbucket.auth.dto.LoginRequestDto;
import com.sparta.bitbucket.common.util.ResponseFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto) {
		String token = authService.login(requestDto);

		return ResponseFactory.ok(token, null);
	}

	@DeleteMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {

		authService.logout(authorizationHeader);
		return ResponseFactory.ok(null);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
		String newToken = authService.refreshAccessToken(request, response);
		return ResponseFactory.ok(newToken, null);
	}
}
