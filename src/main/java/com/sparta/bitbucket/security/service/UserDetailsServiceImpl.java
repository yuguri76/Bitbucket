package com.sparta.bitbucket.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sparta.bitbucket.auth.UserRepository;
import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.common.entity.ErrorMessage;
import com.sparta.bitbucket.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_EMAIL_NOT_FOUND.getMessage()));

		return new UserDetailsImpl(user);
	}

}
