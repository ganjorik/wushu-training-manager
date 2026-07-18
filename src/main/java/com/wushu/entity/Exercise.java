package com.wushu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "exercise")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Exercise {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;


	@NotBlank(message = "Exercise name is required")
	@Size(max = 100, message = "Exercise name cannot exceed 100 characters")
	@Column(name = "name", nullable = false)
	private String name;


	@NotNull(message = "Exercise type is required")
	@Enumerated(EnumType.STRING)
	@Column(name = "type", columnDefinition = "VARCHAR(50)", nullable = false)
	private ExerciseType type;


	@NotNull(message = "Difficukty is required")
	@Enumerated(EnumType.STRING)
	@Column(name = "difficulty", columnDefinition = "VARCHAR(50)")
	private DifficultyLevel difficulty;


	@Size(max = 500, message = "Description is too long")
	@Column(name = "description", length = 500)
	private String description;
}