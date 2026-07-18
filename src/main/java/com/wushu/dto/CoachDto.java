package com.wushu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CoachDto {

	private Long id;

	@NotBlank(message = "Coach name is required")
	@Size(max = 100, message = "Coach name cannot exceed 100 characters")
	private String name;

	@NotNull(message = "Experience is required")
	@Min(value = 0, message = "Experience cannot be negative")
	private Integer experienceYears;

	@NotBlank(message = "Phone is required")
	@Size(max = 20, message = "Phone number is too long")
	private String phone;
}