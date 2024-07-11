package com.sparta.bitbucket.security.service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sparta.bitbucket.security.JwtTokenProvider;
import com.sparta.bitbucket.security.config.JwtConfig;
import com.sparta.bitbucket.security.TokenType;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtService")
@Service
public class JwtService {

	/**
	 * JWT 토큰에서 이메일을 추출합니다.
	 * @param token JWT 토큰
	 * @return 추출된 이메일
	 */
	public String extractEmail(String token) {
		return JwtTokenProvider.extractEmail(token, getSecretKey());
	}

	/**
	 * JWT 토큰에서 역할을 추출합니다.
	 * @param token
	 * @return
	 */
	public Object extractRole(String token) {
		return JwtTokenProvider.extractRole(token, JwtConfig.AUTHORIZATION_KEY, getSecretKey());
	}

	/**
	 * JWT 토큰이 만료되었는지 확인합니다.
	 * @param token JWT 토큰
	 * @return 토큰 만료 여부
	 */
	public Boolean isTokenExpired(String token) {
		return JwtTokenProvider.isTokenExpired(token, getSecretKey());
	}

	/**
	 * JWT 토큰의 유효성을 검증합니다.
	 * @param token JWT 토큰
	 * @return 토큰 유효성 여부
	 */
	public Boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			log.error("Invalid JWT signature.");
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token.");
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token.");
		} catch (IllegalArgumentException e) {
			log.error("JWT claims is empty.");
		}
		return false;
	}

	public Boolean isAuthorizationHeaderMissing(HttpServletRequest request) {
		return request.getHeader(JwtConfig.HEADER) == null;
	}

	/**
	 * JWT 토큰을 생성합니다.
	 *
	 * @param username  사용자의 식별자 (일반적으로 이메일 또는 사용자 ID)
	 * @param role      사용자의 역할 (예: "ROLE_USER", "ROLE_MANAGER" 등)
	 * @param tokenType 토큰 타입 (ACCESS 또는 REFRESH)
	 * @return 생성된 JWT 토큰 문자열
	 */
	public String generateToken(String username, Object role, TokenType tokenType) {
		long expiration = (tokenType == TokenType.ACCESS) ? JwtConfig.staticAccessTokenExpiration :
			JwtConfig.staticRefreshTokenExpiration;

		return JwtTokenProvider.generateToken(username, JwtConfig.AUTHORIZATION_KEY, role, expiration, getSecretKey());
	}

	/**
	 * Authorization 헤더에서 Access Token을 추출합니다.
	 * @param header Authorization 헤더 값
	 * @return 추출된 Access Token
	 */
	public String getAccessTokenFromHeader(String header) {

		if (header.startsWith(JwtConfig.TOKEN_PREFIX)) {
			return header.replace(JwtConfig.TOKEN_PREFIX, "");
		}
		return null;
	}

	/**
	 * Http 요청에서 Access Token을 추출합니다.
	 * @param request Http 요청
	 * @return 추출된 Access Token
	 */
	public String getAccessTokenFromRequest(HttpServletRequest request) {

		return getAccessTokenFromHeader(request.getHeader(JwtConfig.HEADER));
	}

	/**
	 * HTTP 요청의 쿠키에서 Refresh 토큰을 추출합니다.
	 * @param request HTTP 요청
	 * @return 추출된 Refresh 토큰
	 */
	public String getRefreshTokenFromRequest(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(JwtConfig.REFRESH_TOKEN_COOKIE_NAME)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Refresh 토큰을 쿠키에 저장합니다.
	 * @param refreshToken 저장할 Refresh 토큰
	 */
	public void setRefreshTokenAtCookie(String refreshToken) {
		Cookie cookie = new Cookie(JwtConfig.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(JwtConfig.staticRefreshTokenExpirationSecond);
		HttpServletResponse response = ((ServletRequestAttributes)Objects.requireNonNull(
			RequestContextHolder.getRequestAttributes())).getResponse();

		if (response != null) {
			response.addCookie(cookie);
		}
	}

	/**
	 * HTTP 응답 헤더에 Access 토큰을 설정합니다.
	 * @param response HTTP 응답
	 * @param accessToken 설정할 Access 토큰
	 */
	public void setHeaderWithAccessToken(HttpServletResponse response, String accessToken) {
		response.setHeader(JwtConfig.HEADER, JwtConfig.TOKEN_PREFIX + accessToken);
	}

	/**
	 * Refresh 토큰 쿠키를 삭제합니다.
	 */
	public void deleteRefreshTokenAtCookie() {
		// 덮어 쓰기
		Cookie cookie = new Cookie(JwtConfig.REFRESH_TOKEN_COOKIE_NAME, null);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(0); // 쿠키 시간 초기화
		HttpServletResponse response = ((ServletRequestAttributes)Objects.requireNonNull(
			RequestContextHolder.getRequestAttributes())).getResponse();

		if (response != null) {
			response.addCookie(cookie);
		}
	}

	/**
	 * JWT 서명에 사용할 비밀 키를 생성합니다.
	 * @return 생성된 SecretKey 객체
	 */
	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(JwtConfig.staticSecretKey.getBytes(StandardCharsets.UTF_8));
	}

}
