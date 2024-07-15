package com.sparta.bitbucket.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sparta.bitbucket.auth.entity.User;

public class UserDetailsImpl implements UserDetails {

	private final User user;

	public UserDetailsImpl(User user) {
		this.user = user;
	}

	/**
	 * 사용자 정보를 반환하는 메서드
	 *
	 * @return user 객체의 정보
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 사용자가 가지고 있는 권한 목록을 반환한다.
	 *
	 * @return 사용자의 권한 목록 (현재는 빈 리스트를 반환)
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	/**
	 * 사용자의 비밀번호를 반환한다.
	 *
	 * @return 사용자의 비밀번호
	 */
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	/**
	 * 사용자의 로그인 이름을 반환한다.
	 *
	 * @return 사용자의 이메일 (사용자의 고유 식별자로 사용)
	 */
	@Override
	public String getUsername() {
		return user.getEmail();
	}

	/**
	 * 사용자 계정이 만료되지 않았는지 여부를 반환한다.
	 *
	 * @return 사용자 계정의 만료 여부 (항상 true)
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 사용자 계정이 잠기지 않았는지 여부를 반환한다.
	 *
	 * @return 사용자 계정의 잠금 여부 (항상 true)
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 사용자 자격 증명(비밀번호)이 만료되지 않았는지 여부를 반환한다.
	 *
	 * @return 사용자 자격 증명의 만료 여부 (항상 true)
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 사용자 계정이 활성화되어 있는지 여부를 반환한다.
	 *
	 * @return 사용자 계정의 활성화 여부 (항상 true)
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}
