package com.wushu.service;

import com.wushu.entity.User;
import com.wushu.exception.BusinessException;
import com.wushu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public User findByUsername(String username) {

		return userRepository.findByUsername(username)
				.orElseThrow(() -> {

					log.warn(
							"User with username '{}' not found",
							username
					);

					return new BusinessException("User not found");
				});
	}
}