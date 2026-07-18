package com.wushu.mapper;

import com.wushu.dto.ExerciseDto;
import com.wushu.entity.Exercise;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExerciseMapper {

	public ExerciseDto toDto(Exercise entity) {

		ExerciseDto dto = new ExerciseDto();

		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setType(entity.getType());
		dto.setDifficulty(entity.getDifficulty());
		dto.setDescription(entity.getDescription());

		return dto;
	}

	public Exercise toEntity(ExerciseDto dto) {

		Exercise entity = new Exercise();

		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setType(dto.getType());
		entity.setDifficulty(dto.getDifficulty());
		entity.setDescription(dto.getDescription());

		return entity;
	}

	public List<ExerciseDto> toDtoList(List<Exercise> exercises) {

		return exercises.stream()
				.map(this::toDto)
				.toList();
	}
}
