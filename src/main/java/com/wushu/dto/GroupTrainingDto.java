package com.wushu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupTrainingDto {

	private Long id;

	@NotBlank(message = "Group name is required")
	@Size(max = 100, message = "Group name cannot exceed 100 characters")
	private String name;

	@NotBlank(message = "Level is required")
	@Size(max = 50, message = "Level cannot exceed 50 characters")
	private String level;

	@NotNull(message = "Coach is required")
	private Long coachId;

	private String coachName;
}