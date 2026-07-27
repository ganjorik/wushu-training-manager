package com.wushu.repository;

import com.wushu.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AttendanceRepositoryTest {

	@Autowired
	private CoachRepository coachRepository;
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private GroupTrainingRepository groupTrainingRepository;
	@Autowired
	private TrainingSessionRepository trainingSessionRepository;
	@Autowired
	private AttendanceRepository attendanceRepository;

	@Test
	void existsByTrainingId_shouldReturnTrue_whenAttendanceExists() {
		// given
		Coach coach = createCoach();
		Student student = createStudent();
		GroupTraining group = createGroup(coach);
		TrainingSession training = createTraining(coach, group);

		createAttendance(training, student);

		// when
		boolean exists = attendanceRepository.existsByTrainingId(training.getId());

		// then
		assertTrue(exists);
	}

	@Test
	void existsByTrainingId_shouldReturnFalse_whenAttendanceDoesNotExist() {
		//given
		Coach coach = createCoach();
		GroupTraining group = createGroup(coach);
		TrainingSession training = createTraining(coach, group);

		//when
		boolean exists = attendanceRepository.existsByTrainingId(training.getId());

		//then
		assertFalse(exists);
	}

	@Test
	void findByTrainingId_shouldReturnAttendances_whenAttendancesExist() {
		// given
		Coach coach = createCoach();
		Student student = createStudent();
		GroupTraining group = createGroup(coach);
		TrainingSession training = createTraining(coach, group);

		createAttendance(training, student);

		//when
		List<Attendance> attendances =
				attendanceRepository.findByTrainingId(training.getId());

		//then
		assertFalse(attendances.isEmpty());
		assertEquals(1, attendances.size());

		Attendance result = attendances.get(0);

		assertEquals(training.getId(), result.getTraining().getId());
		assertEquals(student.getId(), result.getStudent().getId());
		assertEquals("PRESENT", result.getStatus());
		assertEquals("This is a comment", result.getComment());
	}

	@Test
	void findByTrainingId_shouldReturnEmptyList_whenAttendanceDoesNotExist() {
		// given
		Coach coach = createCoach();
		GroupTraining group = createGroup(coach);
		TrainingSession training = createTraining(coach, group);

		//when
		List<Attendance> attendances =
				attendanceRepository.findByTrainingId(training.getId());

		//then
		assertTrue(attendances.isEmpty());
	}

	@Test
	void existsByStudentId_shouldReturnTrue_whenAttendanceExists() {
		// given
		Coach coach = createCoach();
		Student student = createStudent();
		GroupTraining group = createGroup(coach);
		TrainingSession training = createTraining(coach, group);

		createAttendance(training, student);

		//when
		boolean exists =
				attendanceRepository.existsByStudentId(student.getId());

		//then
		assertTrue(exists);
	}

	@Test
	void existsByStudentId_shouldReturnFalse_whenAttendanceDoesNotExist() {
		// given
		Student student = createStudent();

		//when
		boolean exists =
				attendanceRepository.existsByStudentId(student.getId());

		//then
		assertFalse(exists);
	}

	@Test
	void existsByTrainingIdAndStudentId_shouldReturnTrue_whenAttendanceExists() {
		// given
		Coach coach = createCoach();
		Student student = createStudent();
		GroupTraining group = createGroup(coach);
		TrainingSession training = createTraining(coach, group);

		createAttendance(training, student);

		//when
		boolean exists =
				attendanceRepository.existsByTrainingIdAndStudentId(
								training.getId(),
								student.getId()
						);

		//then
		assertTrue(exists);
	}

	@Test
	void existsByTrainingIdAndStudentId_shouldReturnFalse_whenAttendanceDoesNotExist() {
		// given
		Coach coach = createCoach();
		Student student = createStudent();
		GroupTraining group = createGroup(coach);
		TrainingSession training = createTraining(coach, group);

		//when
		boolean exists =
				attendanceRepository.existsByTrainingIdAndStudentId(
						training.getId(),
						student.getId()
				);

		//then
		assertFalse(exists);
	}

	private Coach createCoach() {
		Coach coach = new Coach();
		coach.setName("John Smith");
		coach.setExperienceYears(10);
		coach.setPhone("+375292148963");
		return coachRepository.save(coach);
	}

	private Student createStudent() {
		Student student = new Student();
		student.setName("Denis");
		student.setAge(18);
		student.setPhone("+375292147788");
		return studentRepository.save(student);
	}

	private GroupTraining createGroup(Coach coach) {
		GroupTraining group = new GroupTraining();
		group.setName("Junior");
		group.setLevel("Beginner");
		group.setCoach(coach);
		return groupTrainingRepository.save(group);
	}

	private TrainingSession createTraining(
			Coach coach,
			GroupTraining group
	) {
		TrainingSession training = new TrainingSession();
		training.setDate(LocalDate.now());
		training.setDuration(90);
		training.setCoach(coach);
		training.setGroup(group);
		training.setTopic("Kicks");

		return trainingSessionRepository.save(training);
	}

	private Attendance createAttendance(
			TrainingSession training,
			Student student
	) {
		Attendance attendance = new Attendance();
		attendance.setTraining(training);
		attendance.setStudent(student);
		attendance.setStatus("PRESENT");
		attendance.setComment("This is a comment");

		return attendanceRepository.save(attendance);
	}
}
