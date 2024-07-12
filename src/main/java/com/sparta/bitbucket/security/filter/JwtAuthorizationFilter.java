package com.sparta.bitbucket.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bitbucket.security.service.JwtService;
import com.sparta.bitbucket.security.service.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsServiceImpl userDetailsService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 요청마다 JWT 토큰을 검증하고 인가를 처리합니다.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		try {
			String accessToken = jwtService.getAccessTokenFromRequest(request);
			if (accessToken != null) {
				if (jwtService.validateToken(accessToken)) {
					log.info("Access Token 유효");
					setAuthenticationContext(accessToken);
				} else {
					log.info("Invalid Token: {}", accessToken);
					sendTokenExpiredResponse(response);
					return;
				}
			}
		} catch (ExpiredJwtException e) {
			log.warn("Expired JWT token", e);
			sendTokenExpiredResponse(response);
			return;
		} catch (SecurityException | MalformedJwtException e) {
			log.warn("Invalid JWT token", e);
			sendInvalidTokenResponse(response);
			return;
		} catch (Exception e) {
			log.error("Unexpected error during JWT processing", e);
			sendAuthenticationFailureResponse(response);
			return;
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Authorization 헤더가 없는 요청에 대해 필터를 적용하지 않습니다.
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return jwtService.isAuthorizationHeaderMissing(request);
	}

	/**
	 * 인증 정보를 SecurityContext에 설정합니다.
	 */
	private void setAuthenticationContext(String accessToken) {
		String username = jwtService.extractEmail(accessToken);

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, "",
			userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private void sendTokenExpiredResponse(HttpServletResponse response) throws IOException {
		sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token_Expired",
			"The access token has expired");
	}

	private void sendInvalidTokenResponse(HttpServletResponse response) throws IOException {
		sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid_Token",
			"The access token is invalid");
	}

	private void sendAuthenticationFailureResponse(HttpServletResponse response) throws IOException {
		sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication_Failed",
			"Authentication failed");
	}

	/**
	 * 인증 실패 시 응답을 설정합니다.
	 */
	private void sendErrorResponse(HttpServletResponse response, int status, String error, String message) throws
		IOException {
		response.setStatus(status);
		response.setContentType("application/json");

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("status", status);
		responseBody.put("error", error);
		responseBody.put("message", message);

		response.getWriter().write(objectMapper.writeValueAsString(responseBody));
	}
}
