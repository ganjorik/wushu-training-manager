package com.wushu.service;

import com.wushu.entity.TrainingExercise;

import java.util.List;

public interface TrainingExerciseService {

	List<TrainingExercise> getByTraining(Long trainingId);

	void addExercise(
			Long trainingId,
			Long exerciseId,
			Integer repetitions,
			Integer duration,
			Integer orderIndex
	);

	void delete(
			Long trainingId,
			Long exerciseId
	);

	void deleteAllByTraining(Long trainingId);
}