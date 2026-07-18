package com.wushu.mapper;

import com.wushu.dto.TrainingExerciseDto;
import com.wushu.entity.TrainingExercise;

public class TrainingExerciseMapper {

	public static TrainingExerciseDto toDto(TrainingExercise e) {

		TrainingExerciseDto dto = new TrainingExerciseDto();

		dto.setTrainingId(e.getTraining().getId());
		dto.setTrainingDate(e.getTraining().getDate().toString());

		dto.setExerciseId(e.getExercise().getId());
		dto.setExerciseName(e.getExercise().getName());

		dto.setRepetitions(e.getRepetitions());
		dto.setDuration(e.getDuration());
		dto.setOrderIndex(e.getOrderIndex());

		return dto;

	}
}
