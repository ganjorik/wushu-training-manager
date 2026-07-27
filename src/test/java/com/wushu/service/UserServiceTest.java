package com.wushu.service;

import com.wushu.entity.User;
import com.wushu.exception.BusinessException;
import com.wushu.repository.UserRepository;
import com.wushu.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userService;

	@Test
	void findByUsername_shouldReturnUser() {

		User user = new User();
		user.setUsername("admin");

		when(userRepository.findByUsername("admin"))
				.thenReturn(Optional.of(user));

		User result = userService.findByUsername("admin");

		assertEquals(user, result);

		verify(userRepository).findByUsername("admin");
	}

	@Test
	void findByUsername_shouldThrowBusinessException_whenUserNotFound() {

		when(userRepository.findByUsername("admin"))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> userService.findByUsername("admin")
		);

		verify(userRepository).findByUsername("admin");
	}
}