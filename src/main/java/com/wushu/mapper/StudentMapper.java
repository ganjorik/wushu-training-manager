package com.wushu.mapper;

import com.wushu.dto.StudentDto;
import com.wushu.entity.Student;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentMapper {

	public StudentDto toDto(Student student) {

		return new StudentDto(
				student.getId(),
				student.getName(),
				student.getAge(),
				student.getPhone()
		);
	}

	public Student toEntity(StudentDto dto) {

		Student student = new Student();

		student.setId(dto.getId());
		student.setName(dto.getName());
		student.setAge(dto.getAge());
		student.setPhone(dto.getPhone());

		return student;
	}

	public List<StudentDto> toDtoList(List<Student> students) {

		return students.stream()
				.map(this::toDto)
				.toList();
	}
}
