package com.wushu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "coach")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Coach {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank(message = "Coach name is required")
	@Size(max = 100, message = "Coach name cannot exceed 100 characters")
	@Column(name = "name", nullable = false)
	private String name;


	@NotNull(message = "Experience is required")
	@Min(value = 0,
			message = "Experience cannot be negative")
	@Column(name = "experience_years")
	private Integer experienceYears;


	@NotBlank(message = "Phone is required")
	@Size(max = 20,
			message = "Phone number is too long")
	@Column(name = "phone")
	private String phone;

	@JsonIgnore
	@OneToMany(mappedBy = "coach")
	@ToString.Exclude
	private Set<GroupTraining> groups;

}