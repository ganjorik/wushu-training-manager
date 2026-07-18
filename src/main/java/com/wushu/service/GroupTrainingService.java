package com.wushu.service;

import com.wushu.entity.GroupTraining;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupTrainingService {

	List<GroupTraining> getAll();

	Page<GroupTraining> getAll(Pageable pageable);

	GroupTraining getById(Long id);

	GroupTraining create(GroupTraining group);

	GroupTraining update(GroupTraining group);

	void delete(Long id);

	GroupTraining getByIdWithStudents(Long id);

	void addStudent(Long groupId, Long studentId);

	void removeStudent(Long groupId, Long studentId);
}