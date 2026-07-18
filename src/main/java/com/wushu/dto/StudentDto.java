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

public class StudentDto {

	private Long id;

	@NotBlank(message = "Student name is required")
	@Size(max = 100, message = "Student name cannot exceed 100 characters")
	private String name;

	@NotNull(message = "Age is required")
	@Min(value = 6, message = "Age must be greater than 5")
	private Integer age;

	@NotBlank(message = "Phone is required")
	@Size(max = 20, message = "Phone number is too long")
	private String phone;
}