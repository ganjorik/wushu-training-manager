package com.wushu.mapper;

import com.wushu.dto.GroupTrainingDto;
import com.wushu.entity.Coach;
import com.wushu.entity.GroupTraining;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupTrainingMapper {

	public GroupTrainingDto toDto(GroupTraining group) {

		GroupTrainingDto dto = new GroupTrainingDto();

		dto.setId(group.getId());
		dto.setName(group.getName());
		dto.setLevel(group.getLevel());

		Coach coach = group.getCoach();

		if (coach != null) {

			dto.setCoachId(coach.getId());
			dto.setCoachName(coach.getName());
		}

		return dto;
	}

	public GroupTraining toEntity(GroupTrainingDto dto) {

		GroupTraining entity = new GroupTraining();

		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setLevel(dto.getLevel());

		Coach coach = null;

		if (dto.getCoachId() != null) {

			coach = new Coach();
			coach.setId(dto.getCoachId());
		}

		entity.setCoach(coach);

		return entity;
	}

	public List<GroupTrainingDto> toDtoList(List<GroupTraining> groups) {

		return groups.stream()
				.map(this::toDto)
				.toList();
	}
}