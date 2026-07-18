package com.wushu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "training_exercise")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@IdClass(TrainingExerciseId.class)

public class TrainingExercise {

	@Id
	@ManyToOne
	@JoinColumn(name = "training_id", nullable = false)
	private TrainingSession training;

	@Id
	@ManyToOne
	@JoinColumn(name = "exercise_id",  nullable = false)
	private Exercise exercise;

	@Column(name = "repetitions")
	private Integer repetitions;

	@Column(name = "duration")
	private Integer duration;

	@Column(name = "order_index")
	private Integer orderIndex;
}