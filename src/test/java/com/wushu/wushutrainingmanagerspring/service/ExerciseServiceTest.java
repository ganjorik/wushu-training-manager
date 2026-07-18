package com.wushu.wushutrainingmanagerspring.service;

import com.wushu.entity.Exercise;
import com.wushu.exception.BusinessException;
import com.wushu.repository.ExerciseRepository;
import com.wushu.repository.TrainingExerciseRepository;
import com.wushu.service.ExerciseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

	@Mock
	private ExerciseRepository exerciseRepository;

	@Mock
	private TrainingExerciseRepository trainingExerciseRepository;

	@InjectMocks
	private ExerciseServiceImpl exerciseService;

	@Test
	void getAll_shouldReturnAllExercises() {

		List<Exercise> exercises = List.of(
				new Exercise(),
				new Exercise()
		);

		when(exerciseRepository.findAll())
				.thenReturn(exercises);

		List<Exercise> result = exerciseService.getAll();

		assertEquals(2, result.size());

		verify(exerciseRepository).findAll();
	}

	@Test
	void getById_shouldReturnExercise() {

		Exercise exercise = new Exercise();
		exercise.setId(1L);

		when(exerciseRepository.findById(1L))
				.thenReturn(Optional.of(exercise));

		Exercise result = exerciseService.getById(1L);

		assertEquals(exercise, result);

		verify(exerciseRepository).findById(1L);
	}

	@Test
	void getById_shouldThrowBusinessException_whenExerciseNotFound() {

		when(exerciseRepository.findById(1L))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> exerciseService.getById(1L)
		);

		verify(exerciseRepository).findById(1L);
	}

	@Test
	void create_shouldSaveExercise() {

		Exercise exercise = new Exercise();

		when(exerciseRepository.save(any(Exercise.class)))
				.thenReturn(exercise);

		Exercise result = exerciseService.create(exercise);

		assertEquals(exercise, result);

		verify(exerciseRepository).save(exercise);
	}

	@Test
	void update_shouldSaveExercise() {

		Exercise exercise = new Exercise();
		exercise.setId(1L);

		when(exerciseRepository.save(any(Exercise.class)))
				.thenReturn(exercise);

		Exercise result = exerciseService.update(exercise);

		assertEquals(exercise, result);

		verify(exerciseRepository).save(exercise);
	}

	@Test
	void delete_shouldDeleteExercise() {

		Exercise exercise = new Exercise();
		exercise.setId(1L);

		when(trainingExerciseRepository.existsByExerciseId(1L))
				.thenReturn(false);

		when(exerciseRepository.findById(1L))
				.thenReturn(Optional.of(exercise));

		exerciseService.delete(1L);

		verify(exerciseRepository).delete(exercise);
	}

	@Test
	void delete_shouldThrowBusinessException_whenExerciseIsUsed() {

		when(trainingExerciseRepository.existsByExerciseId(1L))
				.thenReturn(true);

		assertThrows(
				BusinessException.class,
				() -> exerciseService.delete(1L)
		);

		verify(exerciseRepository, never())
				.delete(any(Exercise.class));
	}

}