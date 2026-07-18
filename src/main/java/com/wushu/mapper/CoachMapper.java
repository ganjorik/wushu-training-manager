package com.wushu.mapper;

import com.wushu.dto.CoachDto;
import com.wushu.entity.Coach;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoachMapper {

	public CoachDto toDto(Coach coach) {

		return new CoachDto(
				coach.getId(),
				coach.getName(),
				coach.getExperienceYears(),
				coach.getPhone()
		);
	}

	public static Coach toEntity(CoachDto dto) {

		Coach coach = new Coach();

		coach.setId(dto.getId());
		coach.setName(dto.getName());
		coach.setExperienceYears(dto.getExperienceYears());
		coach.setPhone(dto.getPhone());

		return coach;
	}

	public List<CoachDto> toDtoList(List<Coach> coaches) {

		return coaches.stream()
				.map(this::toDto)
				.toList();
	}
}