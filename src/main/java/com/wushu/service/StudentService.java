package com.wushu.service;

import com.wushu.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {

	List<Student> getAll();

	Page<Student> getAll(Pageable pageable);

	Student getById(Long id);

	Student create(Student student);

	Student update(Student student);

	void delete(Long id);

	List<Student> getStudentsNotInGroup(Long groupId);
}