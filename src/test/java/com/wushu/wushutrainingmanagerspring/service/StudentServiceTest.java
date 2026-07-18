package com.wushu.wushutrainingmanagerspring.service;

import com.wushu.entity.GroupTraining;
import com.wushu.entity.Student;
import com.wushu.exception.BusinessException;
import com.wushu.repository.AttendanceRepository;
import com.wushu.repository.StudentRepository;
import com.wushu.service.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

	@Mock
	private StudentRepository studentRepository;

	@Mock
	private AttendanceRepository attendanceRepository;

	@InjectMocks
	private StudentServiceImpl studentService;

	@Test
	void getAll_shouldReturnAllStudents() {

		List<Student> students = List.of(
				new Student(),
				new Student()
		);

		when(studentRepository.findAll())
				.thenReturn(students);

		List<Student> result = studentService.getAll();

		assertEquals(2, result.size());

		verify(studentRepository).findAll();
	}

	@Test
	void getById_shouldReturnStudent() {

		Student student = new Student();
		student.setId(1L);

		when(studentRepository.findById(1L))
				.thenReturn(Optional.of(student));

		Student result = studentService.getById(1L);

		assertEquals(student, result);

		verify(studentRepository).findById(1L);
	}

	@Test
	void getById_shouldThrowBusinessException_whenStudentNotFound() {

		when(studentRepository.findById(1L))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> studentService.getById(1L)
		);

		verify(studentRepository).findById(1L);
	}

	@Test
	void create_shouldSaveStudent() {

		Student student = new Student();

		when(studentRepository.save(any(Student.class)))
				.thenReturn(student);

		Student result = studentService.create(student);

		assertEquals(student, result);

		verify(studentRepository).save(student);
	}

	@Test
	void update_shouldSaveStudent() {

		Student student = new Student();
		student.setId(1L);

		when(studentRepository.save(any(Student.class)))
				.thenReturn(student);

		Student result = studentService.update(student);

		assertEquals(student, result);

		verify(studentRepository).save(student);
	}

	@Test
	void delete_shouldDeleteStudent() {

		Student student = new Student();
		student.setId(1L);

		student.getGroups().add(new GroupTraining());

		when(attendanceRepository.existsByStudentId(1L))
				.thenReturn(false);

		when(studentRepository.findWithGroupsById(1L))
				.thenReturn(Optional.of(student));

		studentService.delete(1L);

		assertTrue(student.getGroups().isEmpty());

		verify(studentRepository).save(student);
		verify(studentRepository).delete(student);
	}

	@Test
	void delete_shouldThrowBusinessException_whenAttendanceExists() {

		when(attendanceRepository.existsByStudentId(1L))
				.thenReturn(true);

		assertThrows(
				BusinessException.class,
				() -> studentService.delete(1L)
		);

		verify(studentRepository, never())
				.delete(any(Student.class));
	}

	@Test
	void delete_shouldThrowBusinessException_whenStudentNotFound() {

		when(attendanceRepository.existsByStudentId(1L))
				.thenReturn(false);

		when(studentRepository.findWithGroupsById(1L))
				.thenReturn(Optional.empty());

		assertThrows(
				BusinessException.class,
				() -> studentService.delete(1L)
		);

		verify(studentRepository, never())
				.delete(any(Student.class));
	}

	@Test
	void getStudentsNotInGroup_shouldReturnStudents() {

		List<Student> students = List.of(
				new Student(),
				new Student()
		);

		when(studentRepository.findStudentsNotInGroup(1L))
				.thenReturn(students);

		List<Student> result =
				studentService.getStudentsNotInGroup(1L);

		assertEquals(2, result.size());

		verify(studentRepository)
				.findStudentsNotInGroup(1L);
	}

}
