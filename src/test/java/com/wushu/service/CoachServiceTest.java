package com.wushu.service;

import com.wushu.entity.Coach;
import com.wushu.exception.BusinessException;
import com.wushu.repository.CoachRepository;
import com.wushu.repository.TrainingSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoachServiceTest {

	@Mock
	private CoachRepository coachRepository;

	@Mock
	private TrainingSessionRepository trainingSessionRepository;

	@InjectMocks
	private CoachServiceImpl coachService;

	@Test
	void getById_shouldReturnCoach() {

		Coach coach = new Coach();
		coach.setId(1L);
		coach.setName("John Smith");

		when(coachRepository.findById(1L))
				.thenReturn(Optional.of(coach));

		Coach result = coachService.getById(1L);

		assertEquals(coach, result);

		verify(coachRepository).findById(1L);
	}

	@Test
	void getById_shouldThrowBusinessException_whenCoachNotFound() {

		when(coachRepository.findById(1L))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> coachService.getById(1L)
		);

		verify(coachRepository).findById(1L);
		verify(trainingSessionRepository, never()).existsByCoachId(1L);
	}

	@Test
	void create_shouldSaveCoach() {

		Coach coach = new Coach();
		coach.setName("John Smith");

		when(coachRepository.save(any(Coach.class)))
				.thenReturn(coach);

		Coach result = coachService.create(coach);

		assertEquals(coach, result);

		verify(coachRepository).save(coach);
	}

	@Test
	void update_shouldSaveCoach() {

		Coach coach = new Coach();
		coach.setId(1L);
		coach.setName("Updated Coach");

		when(coachRepository.save(any(Coach.class)))
				.thenReturn(coach);

		Coach result = coachService.update(coach);

		assertEquals(coach, result);

		verify(coachRepository).save(coach);
	}

	@Test
	void delete_shouldDeleteCoach() {

		Coach coach = new Coach();
		coach.setId(1L);
		coach.setName("John Smith");

		when(coachRepository.findById(1L))
				.thenReturn(Optional.of(coach));

		when(coachRepository.hasGroups(1L))
				.thenReturn(false);

		when(trainingSessionRepository.existsByCoachId(1L))
				.thenReturn(false);

		coachService.delete(1L);

		verify(coachRepository).delete(coach);
	}

	@Test
	void delete_shouldThrowBusinessException_whenCoachNotFound() {

		when(coachRepository.findById(1L))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> coachService.delete(1L)
		);

		verify(coachRepository).findById(1L);
		verify(coachRepository, never()).delete(any(Coach.class));
	}

	@Test
	void delete_shouldThrowBusinessException_whenCoachHasGroups() {

		Coach coach = new Coach();
		coach.setId(1L);

		when(coachRepository.findById(1L))
				.thenReturn(Optional.of(coach));

		when(coachRepository.hasGroups(1L))
				.thenReturn(true);

		assertThrows(
				BusinessException.class,
				() -> coachService.delete(1L)
		);

		verify(coachRepository, never()).delete(any(Coach.class));
	}

	@Test
	void delete_shouldThrowBusinessException_whenCoachHasTrainings() {

		Coach coach = new Coach();
		coach.setId(1L);

		when(coachRepository.findById(1L))
				.thenReturn(Optional.of(coach));

		when(coachRepository.hasGroups(1L))
				.thenReturn(false);

		when(trainingSessionRepository.existsByCoachId(1L))
				.thenReturn(true);

		assertThrows(
				BusinessException.class,
				() -> coachService.delete(1L)
		);

		verify(coachRepository, never()).delete(any(Coach.class));
	}

	@Test
	void getAll_shouldReturnAllCoaches() {

		List<Coach> coaches = List.of(
				new Coach(),
				new Coach()
		);

		when(coachRepository.findAll())
				.thenReturn(coaches);

		List<Coach> result = coachService.getAll();

		assertEquals(2, result.size());

		verify(coachRepository).findAll();
	}
}
