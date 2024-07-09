package com.sparta.bitbucket.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name; //사용자 이름

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;


	/**
	 * User 객체를 생성하는 생성자입니다.
	 *
	 * @param email    사용자의 이메일 주소
	 * @param password 사용자의 비밀번호
	 * @param name     사용자의 이름
	 * @param role     사용자의 역할 (권한). null일 경우 기본값으로 Role.USER가 설정됩니다.
	 *
	 * @Builder 어노테이션을 사용하여 빌더 패턴으로 객체 생성이 가능합니다.
	 * <p>
	 * 예시: User user = User.builder().email("test@example.com").password("user_password").name("user_name").build();
	 */
	@Builder
	public User(String email, String password, String name, Role role) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.role = role != null ? role : Role.USER;
	}

}
