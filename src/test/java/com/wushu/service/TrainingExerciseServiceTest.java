package com.wushu.service;

import com.wushu.entity.Exercise;
import com.wushu.entity.TrainingExercise;
import com.wushu.entity.TrainingSession;
import com.wushu.exception.BusinessException;
import com.wushu.repository.ExerciseRepository;
import com.wushu.repository.TrainingExerciseRepository;
import com.wushu.repository.TrainingSessionRepository;
import com.wushu.service.TrainingExerciseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingExerciseServiceTest {

	@Mock
	private TrainingExerciseRepository repository;

	@Mock
	private TrainingSessionRepository trainingSessionRepository;

	@Mock
	private ExerciseRepository exerciseRepository;

	@InjectMocks
	private TrainingExerciseServiceImpl service;

	@Test
	void getByTraining_shouldReturnExercises() {

		when(repository.findByTrainingIdOrderByOrderIndex(1L))
				.thenReturn(List.of(new TrainingExercise()));

		service.getByTraining(1L);

		verify(repository).findByTrainingIdOrderByOrderIndex(1L);
	}

	@Test
	void addExercise_shouldSaveExercise() {

		TrainingSession training = new TrainingSession();
		Exercise exercise = new Exercise();

		when(trainingSessionRepository.findById(1L))
				.thenReturn(Optional.of(training));

		when(exerciseRepository.findById(2L))
				.thenReturn(Optional.of(exercise));

		service.addExercise(
				1L,
				2L,
				10,
				30,
				1
		);

		verify(repository).save(any(TrainingExercise.class));
	}

	@Test
	void addExercise_shouldThrow_whenTrainingNotFound() {

		when(trainingSessionRepository.findById(1L))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> service.addExercise(
						1L,
						2L,
						10,
						30,
						1
				)
		);
	}

	@Test
	void addExercise_shouldThrow_whenExerciseNotFound() {

		when(trainingSessionRepository.findById(1L))
				.thenReturn(Optional.of(new TrainingSession()));

		when(exerciseRepository.findById(2L))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> service.addExercise(
						1L,
						2L,
						10,
						30,
						1
				)
		);
	}

	@Test
	void delete_shouldDeleteExercise() {

		service.delete(1L, 2L);

		verify(repository)
				.deleteByTrainingIdAndExerciseId(1L, 2L);
	}

	@Test
	void deleteAll_shouldDeleteTrainingExercises() {

		service.deleteAllByTraining(1L);

		verify(repository)
				.deleteAllByTrainingId(1L);
	}

}
