package com.wushu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrainingSessionDto {

	private Long id;

	@NotNull(message = "Date is required")
	private LocalDate date;

	@NotNull(message = "Duration is required")
	@Min(value = 1, message = "Duration must be greater than 0")
	private Integer duration;

	@NotBlank(message = "Topic is required")
	@Size(max = 255, message = "Topic cannot exceed 255 characters")
	private String topic;

	@NotNull(message = "Group is required")
	private Long groupId;

	private String groupName;

	@NotNull(message = "Coach is required")
	private Long coachId;

	private String coachName;
}