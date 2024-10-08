package com.sparta.bitbucket.auth;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.bitbucket.auth.dto.LoginRequestDto;
import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.common.entity.ErrorMessage;
import com.sparta.bitbucket.common.exception.auth.PasswordInvalidException;
import com.sparta.bitbucket.security.JwtService;
import com.sparta.bitbucket.security.TokenType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	private final JwtService jwtService;

	private final PasswordEncoder passwordEncoder;

	/**
	 * 사용자 로그인을 처리하는 메서드
	 *
	 * @param requestDto 로그인 요청 정보를 담은 DTO
	 * @return 생성된 액세스 토큰
	 * @throws UsernameNotFoundException 이메일에 해당하는 사용자가 없을 경우
	 * @throws PasswordInvalidException   비밀번호가 일치하지 않을 경우
	 */
	public String login(LoginRequestDto requestDto) {

		User user = userRepository.findByEmail(requestDto.getEmail())
			.orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_EMAIL_NOT_FOUND.getMessage()));

		if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			String accessToken = jwtService.generateToken(user.getEmail(), user.getRole(), TokenType.ACCESS);
			String refreshToken = jwtService.generateToken(user.getEmail(), user.getRole(), TokenType.REFRESH);

			user.updateRefreshToken(refreshToken);

			jwtService.setRefreshTokenAtCookie(refreshToken);

			userRepository.save(user);

			return accessToken;
		} else {
			throw new PasswordInvalidException(ErrorMessage.PASSWORD_INVALID);
		}
	}

	/**
	 * 사용자 로그아웃을 처리하는 메서드
	 *
	 * @param header 요청 헤더 (액세스 토큰 포함)
	 * @throws UsernameNotFoundException 이메일에 해당하는 사용자가 없을 경우
	 */
	public void logout(String header) {

		String token = jwtService.getAccessTokenFromHeader(header);
		String email = jwtService.extractEmail(token);

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_EMAIL_NOT_FOUND.getMessage()));

		user.updateRefreshToken(null);

		userRepository.save(user);

		jwtService.deleteRefreshTokenAtCookie();
	}

	/**
	 * 액세스 토큰을 갱신하는 메서드
	 *
	 * @param request  HTTP 요청 객체
	 * @param response HTTP 응답 객체
	 * @return 새로 생성된 액세스 토큰
	 * @throws UsernameNotFoundException 이메일에 해당하는 사용자가 없을 경우
	 */
	public String refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {

		String refreshToken = jwtService.getRefreshTokenFromRequest(request);

		String email = jwtService.extractEmail(refreshToken);

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_EMAIL_NOT_FOUND.getMessage()));

		if (user.getRefreshToken().equals(refreshToken)) {
			Object role = jwtService.extractRole(refreshToken);
			String newAccessToken = jwtService.generateToken(email, role, TokenType.ACCESS);
			jwtService.setHeaderWithAccessToken(response, newAccessToken);
			return newAccessToken;
		}
		return "";
	}
}
