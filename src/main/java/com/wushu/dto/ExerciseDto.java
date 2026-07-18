package com.wushu.dto;

import com.wushu.entity.DifficultyLevel;
import com.wushu.entity.ExerciseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExerciseDto {

	private Long id;

	@NotBlank(message = "Exercise name is required")
	@Size(max = 100, message = "Exercise name cannot exceed 100 characters")
	private String name;

	@NotNull(message = "Exercise type is required")
	private ExerciseType type;

	@NotNull(message = "Difficulty is required")
	private DifficultyLevel difficulty;

	@Size(max = 500, message = "Description is too long")
	private String description;
}
