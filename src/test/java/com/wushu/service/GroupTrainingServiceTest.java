package com.wushu.service;

import com.wushu.entity.Coach;
import com.wushu.entity.GroupTraining;
import com.wushu.entity.Student;
import com.wushu.exception.BusinessException;
import com.wushu.repository.CoachRepository;
import com.wushu.repository.GroupTrainingRepository;
import com.wushu.repository.StudentRepository;
import com.wushu.service.GroupTrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupTrainingServiceTest {

	@Mock
	private GroupTrainingRepository groupRepository;

	@Mock
	private CoachRepository coachRepository;

	@Mock
	private StudentRepository studentRepository;

	@InjectMocks
	private GroupTrainingServiceImpl service;

	@Test
	void getAll_shouldReturnGroups() {

		when(groupRepository.findAll())
				.thenReturn(List.of(new GroupTraining(), new GroupTraining()));

		List<GroupTraining> result = service.getAll();

		assertEquals(2, result.size());

		verify(groupRepository).findAll();
	}

	@Test
	void getById_shouldReturnGroup() {

		GroupTraining group = new GroupTraining();

		when(groupRepository.findById(1L))
				.thenReturn(Optional.of(group));

		assertEquals(group, service.getById(1L));

		verify(groupRepository).findById(1L);
	}

	@Test
	void getById_shouldThrowBusinessException() {

		when(groupRepository.findById(1L))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> service.getById(1L)
		);
	}

	@Test
	void create_shouldSaveGroup() {

		Coach coach = new Coach();
		coach.setId(1L);

		GroupTraining group = new GroupTraining();
		group.setCoach(coach);

		when(coachRepository.findById(1L))
				.thenReturn(Optional.of(coach));

		when(groupRepository.save(any(GroupTraining.class)))
				.thenReturn(group);

		service.create(group);

		verify(groupRepository).save(group);
	}

	@Test
	void create_shouldThrow_whenCoachIsMissing() {

		GroupTraining group = new GroupTraining();

		assertThrows(
				BusinessException.class,
				() -> service.create(group)
		);
	}

	@Test
	void create_shouldThrow_whenCoachNotFound() {

		Coach coach = new Coach();
		coach.setId(1L);

		GroupTraining group = new GroupTraining();
		group.setCoach(coach);

		when(coachRepository.findById(1L))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> service.create(group)
		);
	}

	@Test
	void update_shouldSaveGroup() {

		Coach coach = new Coach();
		coach.setId(1L);

		GroupTraining group = new GroupTraining();
		group.setCoach(coach);

		when(coachRepository.findById(1L))
				.thenReturn(Optional.of(coach));

		when(groupRepository.save(any(GroupTraining.class)))
				.thenReturn(group);

		service.update(group);

		verify(groupRepository).save(group);
	}

	@Test
	void delete_shouldDeleteGroup() {

		GroupTraining group = new GroupTraining();

		when(groupRepository.findById(1L))
				.thenReturn(Optional.of(group));

		when(groupRepository.existsByIdAndTrainingsIsNotEmpty(1L))
				.thenReturn(false);

		service.delete(1L);

		verify(groupRepository).removeStudents(1L);
		verify(groupRepository).delete(group);
	}

	@Test
	void delete_shouldThrow_whenGroupHasTrainings() {

		GroupTraining group = new GroupTraining();

		when(groupRepository.findById(1L))
				.thenReturn(Optional.of(group));

		when(groupRepository.existsByIdAndTrainingsIsNotEmpty(1L))
				.thenReturn(true);

		assertThrows(
				BusinessException.class,
				() -> service.delete(1L)
		);

		verify(groupRepository, never()).delete(any(GroupTraining.class));
	}

	@Test
	void getByIdWithStudents_shouldReturnGroup() {

		GroupTraining group = new GroupTraining();

		when(groupRepository.findWithStudentsById(1L))
				.thenReturn(Optional.of(group));

		assertEquals(group, service.getByIdWithStudents(1L));
	}

	@Test
	void addStudent_shouldAddStudentToGroup() {

		GroupTraining group = new GroupTraining();
		Student student = new Student();

		student.setGroups(new HashSet<>());

		when(groupRepository.findById(1L))
				.thenReturn(Optional.of(group));

		when(studentRepository.findById(2L))
				.thenReturn(Optional.of(student));

		service.addStudent(1L, 2L);

		assertTrue(student.getGroups().contains(group));

		verify(studentRepository).save(student);
	}

	@Test
	void removeStudent_shouldRemoveStudentFromGroup() {

		GroupTraining group = new GroupTraining();
		group.setId(1L);

		Student student = new Student();
		student.setGroups(new HashSet<>());

		student.getGroups().add(group);

		when(studentRepository.findById(2L))
				.thenReturn(Optional.of(student));

		service.removeStudent(1L, 2L);

		assertTrue(student.getGroups().isEmpty());

		verify(studentRepository).save(student);
	}
}