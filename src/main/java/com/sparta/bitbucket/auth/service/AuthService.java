package com.sparta.bitbucket.auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.bitbucket.auth.dto.LoginRequestDto;
import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.auth.repository.UserRepository;
import com.sparta.bitbucket.security.TokenType;
import com.sparta.bitbucket.security.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	private final JwtService jwtService;

	private final PasswordEncoder passwordEncoder;

	public String login(LoginRequestDto requestDto) {

		User user = userRepository.findByEmail(requestDto.getEmail())
			.orElseThrow(() -> new UsernameNotFoundException("Invalid username"));

		if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			String accessToken = jwtService.generateToken(user.getEmail(), user.getRole(), TokenType.ACCESS);
			String refreshToken = jwtService.generateToken(user.getEmail(), user.getRole(), TokenType.REFRESH);

			user.updateRefreshToken(refreshToken);

			jwtService.setRefreshTokenAtCookie(refreshToken);

			userRepository.save(user);

			return accessToken;
		} else {
			throw new IllegalArgumentException("Invalid password"); // todo : Exception 만들기
		}
	}

	public void logout(String header) {

		String token = jwtService.getAccessTokenFromHeader(header);
		String email = jwtService.extractEmail(token);

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("Invalid username"));

		user.updateRefreshToken(null);

		userRepository.save(user);

		jwtService.deleteRefreshTokenAtCookie();
	}

	public String refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {

		String refreshToken = jwtService.getRefreshTokenFromRequest(request);

		String email = jwtService.extractEmail(refreshToken);

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

		if (user.getRefreshToken().equals(refreshToken)) {
			Object role = jwtService.extractRole(refreshToken);
			String newAccessToken = jwtService.generateToken(email, role, TokenType.ACCESS);
			jwtService.setHeaderWithAccessToken(response, newAccessToken);
			return newAccessToken;
		}
		return "";
	}
}
