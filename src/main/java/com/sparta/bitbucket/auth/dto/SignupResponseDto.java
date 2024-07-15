package com.sparta.bitbucket.auth.dto;

import java.time.LocalDateTime;

import com.sparta.bitbucket.auth.entity.Role;
import com.sparta.bitbucket.auth.entity.User;

import lombok.Getter;

/**
 * 회원가입 응답을 위한 DTO.
 * 회원가입 후 사용자의 정보를 클라이언트에게 전달.
 */

@Getter
public class SignupResponseDto {

	private final String email;
	private final String name;
	private final Role role;
	private final LocalDateTime createdAt;

	/**
	 * User Entity 데이터로 초기화.
	 *
	 * @param user User Entity 객체.
	 */
	public SignupResponseDto(User user) {
		this.email = user.getEmail();
		this.name = user.getName();
		this.role = user.getRole();
		this.createdAt = user.getCreatedAt();
	}
}
