package com.wushu.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class TrainingExerciseId implements Serializable {

	private Long training;
	private Long exercise;
}