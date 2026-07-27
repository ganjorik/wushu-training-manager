package com.wushu.service;

import com.wushu.entity.Attendance;
import com.wushu.entity.GroupTraining;
import com.wushu.entity.Student;
import com.wushu.entity.TrainingSession;
import com.wushu.exception.BusinessException;
import com.wushu.repository.AttendanceRepository;
import com.wushu.repository.StudentRepository;
import com.wushu.repository.TrainingSessionRepository;
import com.wushu.service.AttendanceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

	@Mock
	private AttendanceRepository attendanceRepository;

	@Mock
	private TrainingSessionRepository trainingRepository;

	@Mock
	private StudentRepository studentRepository;

	@InjectMocks
	private AttendanceServiceImpl attendanceService;

	@Test
	void getByTraining_shouldReturnAttendance() {

		when(attendanceRepository.findByTrainingId(1L))
				.thenReturn(List.of(new Attendance()));

		List<Attendance> result =
				attendanceService.getByTraining(1L);

		assertEquals(1, result.size());

		verify(attendanceRepository).findByTrainingId(1L);
	}

	@Test
	void markAttendance_shouldSaveAttendance() {

		TrainingSession training = new TrainingSession();
		Student student = new Student();

		Attendance savedAttendance = new Attendance();
		savedAttendance.setId(1L);

		when(attendanceRepository.existsByTrainingIdAndStudentId(1L, 2L))
				.thenReturn(false);

		when(trainingRepository.findById(1L))
				.thenReturn(Optional.of(training));

		when(studentRepository.findById(2L))
				.thenReturn(Optional.of(student));

		when(attendanceRepository.save(any(Attendance.class)))
				.thenReturn(savedAttendance);

		attendanceService.markAttendance(
				1L,
				2L,
				"PRESENT",
				"Good"
		);

		verify(attendanceRepository).save(any(Attendance.class));
	}

	@Test
	void markAttendance_shouldThrow_whenAlreadyMarked() {

		when(attendanceRepository.existsByTrainingIdAndStudentId(1L, 2L))
				.thenReturn(true);

		assertThrows(
				BusinessException.class,
				() -> attendanceService.markAttendance(
						1L,
						2L,
						"PRESENT",
						""
				)
		);
	}

	@Test
	void markAttendance_shouldThrow_whenTrainingNotFound() {

		when(attendanceRepository.existsByTrainingIdAndStudentId(1L, 2L))
				.thenReturn(false);

		when(trainingRepository.findById(1L))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> attendanceService.markAttendance(
						1L,
						2L,
						"PRESENT",
						""
				)
		);
	}

	@Test
	void markAttendance_shouldThrow_whenStudentNotFound() {

		when(attendanceRepository.existsByTrainingIdAndStudentId(1L, 2L))
				.thenReturn(false);

		when(trainingRepository.findById(1L))
				.thenReturn(Optional.of(new TrainingSession()));

		when(studentRepository.findById(2L))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> attendanceService.markAttendance(
						1L,
						2L,
						"PRESENT",
						""
				)
		);
	}

	@Test
	void countPresent_shouldReturnCount() {

		Attendance a1 = new Attendance();
		a1.setStatus("PRESENT");

		Attendance a2 = new Attendance();
		a2.setStatus("ABSENT");

		Attendance a3 = new Attendance();
		a3.setStatus("PRESENT");

		when(attendanceRepository.findByTrainingId(1L))
				.thenReturn(List.of(a1, a2, a3));

		assertEquals(
				2,
				attendanceService.countPresent(1L)
		);
	}

	@Test
	void countAbsent_shouldReturnCount() {

		Attendance a1 = new Attendance();
		a1.setStatus("PRESENT");

		Attendance a2 = new Attendance();
		a2.setStatus("ABSENT");

		Attendance a3 = new Attendance();
		a3.setStatus("ABSENT");

		when(attendanceRepository.findByTrainingId(1L))
				.thenReturn(List.of(a1, a2, a3));

		assertEquals(
				2,
				attendanceService.countAbsent(1L)
		);
	}

	@Test
	void getStudentsWithoutAttendance_shouldReturnOnlyUnmarkedStudents() {

		Student s1 = new Student();
		s1.setId(1L);

		Student s2 = new Student();
		s2.setId(2L);

		GroupTraining group = new GroupTraining();
		group.setStudents(new LinkedHashSet<>(List.of(s1, s2)));

		TrainingSession training = new TrainingSession();
		training.setGroup(group);

		Attendance attendance = new Attendance();
		attendance.setStudent(s1);

		when(trainingRepository.findWithRelationsById(1L))
				.thenReturn(Optional.of(training));

		when(attendanceRepository.findByTrainingId(1L))
				.thenReturn(List.of(attendance));

		List<Student> result =
				attendanceService.getStudentsWithoutAttendance(1L);

		assertEquals(1, result.size());
		assertEquals(2L, result.get(0).getId());
	}

	@Test
	void getStudentsWithoutAttendance_shouldThrow_whenTrainingNotFound() {

		when(trainingRepository.findWithRelationsById(1L))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> attendanceService.getStudentsWithoutAttendance(1L)
		);
	}

}