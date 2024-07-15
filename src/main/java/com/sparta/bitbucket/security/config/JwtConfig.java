package com.sparta.bitbucket.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class JwtConfig {

	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER = "Authorization";
	public static final String AUTHORIZATION_KEY = "auth";
	public static final String REFRESH_TOKEN_COOKIE_NAME = "RefreshToken";

	@Value("${jwt.secret.key}")
	private String secretKey;

	@Value("${access-token-expiration}")
	private Long accessTokenExpiration; // access token 만료 시간

	@Value("${refresh-token-expiration}")
	private Long refreshTokenExpiration; // refresh token 만료 시간

	public static String staticSecretKey;
	public static long staticAccessTokenExpiration;
	public static long staticRefreshTokenExpiration;
	public static int staticRefreshTokenExpirationSecond;

	@PostConstruct
	public void init() {
		staticSecretKey = secretKey;
		staticAccessTokenExpiration = accessTokenExpiration; // 30분
		staticRefreshTokenExpiration = refreshTokenExpiration; // 2주
		staticRefreshTokenExpirationSecond = (int)(refreshTokenExpiration / 1000);
	}
}
