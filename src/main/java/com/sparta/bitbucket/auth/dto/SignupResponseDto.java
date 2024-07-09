package com.sparta.bitbucket.auth.dto;

import com.sparta.bitbucket.auth.entity.User;

import lombok.Getter;

/**
 * 회원가입 응답을 위한 DTO.
 * 회원가입 후 사용자의 정보를 클라이언트에게 전달.
 */

@Getter
public class SignupResponseDto {

	private String email;
	private String name;

	/**
	 * User Entity 데이터로 초기화.
	 *
	 * @param user User Entity 객체.
	 */
	public SignupResponseDto(User user) {
		this.email = user.getEmail();
		this.name = user.getName();
	}
}
