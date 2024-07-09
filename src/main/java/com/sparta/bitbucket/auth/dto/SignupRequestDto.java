package com.sparta.bitbucket.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청을 위한 DTO.
 * 각 필드는 유효성 검사를 포함하고 있음.
 */

@Getter
@NoArgsConstructor
public class SignupRequestDto {

	@NotBlank(message = "Email cannot be blank.")
	@Size(min = 10, max = 50, message = "Email must be between 10 and 50 characters long.")
	@Email(message = "Please provide a valid email address.")
	private String email;

	@NotBlank(message = "Password cannot be blank.")
	@Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters long.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$", message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character.")
	private String password;

	@NotBlank(message = "Name cannot be blank.")
	private String name;

	@Builder
	public SignupRequestDto(String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}
}
