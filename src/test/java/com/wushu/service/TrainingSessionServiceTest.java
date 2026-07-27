package com.wushu.service;

import com.wushu.entity.Coach;
import com.wushu.entity.GroupTraining;
import com.wushu.entity.TrainingSession;
import com.wushu.exception.BusinessException;
import com.wushu.repository.AttendanceRepository;
import com.wushu.repository.CoachRepository;
import com.wushu.repository.GroupTrainingRepository;
import com.wushu.repository.TrainingSessionRepository;
import com.wushu.service.TrainingExerciseServiceImpl;
import com.wushu.service.TrainingSessionServiceImpl;
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
class TrainingSessionServiceTest {

	@Mock
	private TrainingSessionRepository trainingRepository;

	@Mock
	private GroupTrainingRepository groupRepository;

	@Mock
	private CoachRepository coachRepository;

	@Mock
	private TrainingExerciseServiceImpl trainingExerciseService;

	@Mock
	private AttendanceRepository attendanceRepository;

	@InjectMocks
	private TrainingSessionServiceImpl service;

	@Test
	void getAll_shouldReturnTrainings() {

		when(trainingRepository.findAll())
				.thenReturn(List.of(
						new TrainingSession(),
						new TrainingSession()
				));

		List<TrainingSession> result = service.getAll();

		assertEquals(2, result.size());

		verify(trainingRepository).findAll();
	}

	@Test
	void getById_shouldReturnTraining() {

		TrainingSession training = new TrainingSession();

		when(trainingRepository.findById(1L))
				.thenReturn(Optional.of(training));

		assertEquals(training, service.getById(1L));
	}

	@Test
	void getById_shouldThrowBusinessException() {

		when(trainingRepository.findById(1L))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> service.getById(1L)
		);
	}

	@Test
	void create_shouldSaveTraining() {

		GroupTraining group = new GroupTraining();
		group.setId(1L);

		Coach coach = new Coach();
		coach.setId(2L);

		TrainingSession training = new TrainingSession();
		training.setGroup(group);
		training.setCoach(coach);

		when(groupRepository.findById(1L))
				.thenReturn(Optional.of(group));

		when(coachRepository.findById(2L))
				.thenReturn(Optional.of(coach));

		when(trainingRepository.save(any(TrainingSession.class)))
				.thenReturn(training);

		service.create(training);

		verify(trainingRepository).save(training);
	}

	@Test
	void create_shouldThrow_whenGroupMissing() {

		TrainingSession training = new TrainingSession();

		assertThrows(
				BusinessException.class,
				() -> service.create(training)
		);
	}

	@Test
	void create_shouldThrow_whenCoachMissing() {

		GroupTraining group = new GroupTraining();
		group.setId(1L);

		TrainingSession training = new TrainingSession();
		training.setGroup(group);

		assertThrows(
				BusinessException.class,
				() -> service.create(training)
		);
	}

	@Test
	void update_shouldSaveTraining() {

		GroupTraining group = new GroupTraining();
		group.setId(1L);

		Coach coach = new Coach();
		coach.setId(2L);

		TrainingSession training = new TrainingSession();
		training.setGroup(group);
		training.setCoach(coach);

		when(groupRepository.findById(1L))
				.thenReturn(Optional.of(group));

		when(coachRepository.findById(2L))
				.thenReturn(Optional.of(coach));

		when(trainingRepository.save(any(TrainingSession.class)))
				.thenReturn(training);

		service.update(training);

		verify(trainingRepository).save(training);
	}

	@Test
	void delete_shouldDeleteTraining() {

		TrainingSession training = new TrainingSession();
		training.setId(1L);

		when(attendanceRepository.existsByTrainingId(1L))
				.thenReturn(false);

		when(trainingRepository.findById(1L))
				.thenReturn(Optional.of(training));

		service.delete(1L);

		verify(trainingExerciseService)
				.deleteAllByTraining(1L);

		verify(trainingRepository)
				.delete(training);
	}

	@Test
	void delete_shouldThrow_whenAttendanceExists() {

		when(attendanceRepository.existsByTrainingId(1L))
				.thenReturn(true);

		assertThrows(
				BusinessException.class,
				() -> service.delete(1L)
		);

		verify(trainingRepository, never())
				.delete(any(TrainingSession.class));
	}

	@Test
	void getByIdWithRelations_shouldReturnTraining() {

		TrainingSession training = new TrainingSession();

		when(trainingRepository.findWithRelationsById(1L))
				.thenReturn(Optional.of(training));

		assertEquals(
				training,
				service.getByIdWithRelations(1L)
		);
	}

	@Test
	void getByIdWithRelations_shouldThrowBusinessException() {

		when(trainingRepository.findWithRelationsById(1L))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> service.getByIdWithRelations(1L)
		);
	}
}