package com.wushu.service;

import com.wushu.entity.Student;
import com.wushu.exception.BusinessException;
import com.wushu.repository.AttendanceRepository;
import com.wushu.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;
	private final AttendanceRepository attendanceRepository;

	@Override
	public List<Student> getAll() {
		return studentRepository.findAll();
	}

	@Override
	public Page<Student> getAll(Pageable pageable) {
		return studentRepository.findAll(pageable);
	}

	@Override
	public Student getById(Long id) {

		return studentRepository.findById(id)
				.orElseThrow(() -> {

					log.warn("Student with id={} not found", id);

					return new BusinessException("Student not found");
				});
	}

	@Override
	@Transactional
	public Student create(Student student) {

		log.info("Creating student '{}'", student.getName());

		Student savedStudent = studentRepository.save(student);

		log.info(
				"Student created successfully: id={}",
				savedStudent.getId()
		);

		return savedStudent;
	}

	@Override
	@Transactional
	public Student update(Student student) {

		log.info("Updating student id={}", student.getId());

		Student updatedStudent = studentRepository.save(student);

		log.info(
				"Student updated successfully: id={}",
				updatedStudent.getId()
		);

		return updatedStudent;
	}

	@Override
	@Transactional
	public void delete(Long id) {

		log.info("Deleting student id={}", id);

		if (attendanceRepository.existsByStudentId(id)) {

			log.warn(
					"Cannot delete student {} because attendance history exists",
					id
			);

			throw new BusinessException(
					"This student cannot be deleted because attendance history is preserved."
			);
		}

		Student student = getStudentWithGroups(id);

		student.getGroups().clear();

		studentRepository.save(student);

		studentRepository.delete(student);

		log.info("Student deleted successfully: id={}", id);
	}

	@Override
	public List<Student> getStudentsNotInGroup(Long groupId) {

		return studentRepository.findStudentsNotInGroup(groupId);
	}

	private Student getStudentWithGroups(Long id) {

		return studentRepository.findWithGroupsById(id)
				.orElseThrow(() -> {

					log.warn("Student with id={} not found", id);

					return new BusinessException("Student not found");
				});
	}
}