package com.sparta.bitbucket.security;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * JWT 토큰 관련 유틸리티 클래스
 */
public final class JwtTokenProvider {


	private JwtTokenProvider() { // 인스턴스화 방지
	}

	/**
	 * 토큰에서 사용자 Email 추출
	 *
	 * @param token     JWT 토큰
	 * @param secretKey 토큰 검증을 위한 비밀 키
	 * @return 추출된 사용자 Email
	 */
	public static String extractEmail(String token, SecretKey secretKey) {
		Claims claims = extractAllClaims(token, secretKey);
		return claims.getSubject();
	}

	/**
	 * 토큰에서 역할 정보 추출
	 *
	 * @param token            JWT 토큰
	 * @param authorizationKey 역할 정보가 저장된 클레임 키
	 * @param secretKey        토큰 검증을 위한 비밀 키
	 * @return 추출된 역할 정보
	 */
	public static Object extractRole(String token, String authorizationKey, SecretKey secretKey) {
		Claims claims = extractAllClaims(token, secretKey);
		return claims.get(authorizationKey);
	}

	/**
	 * 토큰에서 만료 시간 추출
	 *
	 * @param token     JWT 토큰
	 * @param secretKey 토큰 검증을 위한 비밀 키
	 * @return 추출된 만료 시간
	 */
	public static Date extractExpiration(String token, SecretKey secretKey) {
		Claims claims = extractAllClaims(token, secretKey);
		return claims.getExpiration();
	}

	/**
	 * 토큰 만료 여부 확인
	 *
	 * @param token     JWT 토큰
	 * @param secretKey 토큰 검증을 위한 비밀 키
	 * @return 토큰 만료 여부 (만료: true, 유효: false)
	 */
	public static Boolean isTokenExpired(String token, SecretKey secretKey) {
		return extractExpiration(token, secretKey).before(new Date(System.currentTimeMillis()));
	}

	/**
	 * JWT 토큰 생성
	 *
	 * @param username         사용자 이름
	 * @param authorizationKey 역할 정보가 저장될 클레임 키
	 * @param role             사용자 역할
	 * @param expiration       토큰 만료 시간 (밀리초)
	 * @param secretKey        토큰 서명을 위한 비밀 키
	 * @return 생성된 JWT 토큰
	 */
	public static String generateToken(String username, String authorizationKey, Object role, long expiration,
		SecretKey secretKey) {

		return Jwts.builder()
			.claim(authorizationKey, role)
			.setSubject(username)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(secretKey)
			.compact();
	}

	/**
	 * 토큰에서 모든 클레임 추출
	 *
	 * @param token     JWT 토큰
	 * @param secretKey 토큰 검증을 위한 비밀 키
	 * @return 추출된 모든 클레임
	 */
	private static Claims extractAllClaims(String token, SecretKey secretKey) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}

}
