package com.wushu.service;

import com.wushu.entity.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExerciseService {

	List<Exercise> getAll();

	Page<Exercise> getAll(Pageable pageable);

	Exercise getById(Long id);

	Exercise create(Exercise exercise);

	Exercise update(Exercise exercise);

	void delete(Long id);
}