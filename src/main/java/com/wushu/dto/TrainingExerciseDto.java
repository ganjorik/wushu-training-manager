package com.wushu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class TrainingExerciseDto {

	private Long trainingId;
	private String trainingDate;

	private Long exerciseId;
	private String exerciseName;

	private Integer repetitions;
	private Integer duration;
	private Integer orderIndex;


}
