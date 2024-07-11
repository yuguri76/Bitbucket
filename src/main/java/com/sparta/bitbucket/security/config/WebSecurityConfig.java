package com.sparta.bitbucket.security.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sparta.bitbucket.security.filter.JwtAuthorizationFilter;
import com.sparta.bitbucket.security.service.JwtService;
import com.sparta.bitbucket.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final UserDetailsServiceImpl userDetailsService;
	private final JwtService jwtService;

	/**
	 * 생성자를 통해 필요한 서비스들을 주입받습니다.
	 *
	 * @param userDetailsService 사용자 상세 정보 서비스
	 * @param jwtService JWT 관련 서비스
	 */
	public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, JwtService jwtService) {

		this.userDetailsService = userDetailsService;
		this.jwtService = jwtService;
	}

	/**
	 * AuthenticationManager 빈을 생성합니다.
	 *
	 * @param configuration AuthenticationConfiguration 객체
	 * @return AuthenticationManager 객체
	 * @throws Exception 설정 중 발생할 수 있는 예외
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	/**
	 * JwtAuthorizationFilter 빈을 생성합니다.
	 *
	 * @return JwtAuthorizationFilter 객체
	 * @throws Exception 설정 중 발생할 수 있는 예외
	 */
	@Bean
	public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
		return new JwtAuthorizationFilter(jwtService, userDetailsService);
	}

	/**
	 * 보안 필터 체인을 설정합니다.
	 *
	 * @param http HttpSecurity 객체
	 * @return 설정된 SecurityFilterChain 객체
	 * @throws Exception 설정 중 발생할 수 있는 예외
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // 보안 필터 체인을 설정.
		// CSRF 설정
		http.csrf((csrf) -> csrf.disable());

		// 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
		http.sessionManagement(
			(sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.requestMatchers(
				PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
			.requestMatchers("/**").permitAll() // 메인 페이지 요청 허가
			.requestMatchers("/api/users").permitAll() // 회원가입 요청 허가
			.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll() // 로그인 요청 허가
			.requestMatchers(HttpMethod.GET, "/api/boards").permitAll() // 보드 목록 조회 요청 허가
			.requestMatchers(HttpMethod.POST ,"/api/auth/login").permitAll() // 로그인 요청 허가
			.anyRequest().authenticated() // 그 외 모든 요청 인증처리
		);

		// 필터 관리
		http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
