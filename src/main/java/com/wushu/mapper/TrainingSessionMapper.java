package com.wushu.mapper;

import com.wushu.dto.TrainingSessionDto;
import com.wushu.entity.Coach;
import com.wushu.entity.GroupTraining;
import com.wushu.entity.TrainingSession;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingSessionMapper {

	public TrainingSessionDto toDto(TrainingSession training) {

		TrainingSessionDto dto = new TrainingSessionDto();

		dto.setId(training.getId());
		dto.setDate(training.getDate());
		dto.setDuration(training.getDuration());
		dto.setTopic(training.getTopic());

		GroupTraining group = training.getGroup();

		if (group != null) {
			dto.setGroupId(group.getId());
			dto.setGroupName(group.getName());
		}

		Coach coach = training.getCoach();

		if (coach != null) {
			dto.setCoachId(coach.getId());
			dto.setCoachName(coach.getName());
		}

		return dto;
	}

	public TrainingSession toEntity(TrainingSessionDto dto) {

		TrainingSession entity = new TrainingSession();

		entity.setId(dto.getId());
		entity.setDate(dto.getDate());
		entity.setDuration(dto.getDuration());
		entity.setTopic(dto.getTopic());

		if (dto.getGroupId() != null) {

			GroupTraining group = new GroupTraining();
			group.setId(dto.getGroupId());

			entity.setGroup(group);
		}

		if (dto.getCoachId() != null) {

			Coach coach = new Coach();
			coach.setId(dto.getCoachId());

			entity.setCoach(coach);
		}

		return entity;
	}

	public List<TrainingSessionDto> toDtoList(List<TrainingSession> trainings) {

		return trainings.stream()
				.map(this::toDto)
				.toList();
	}
}