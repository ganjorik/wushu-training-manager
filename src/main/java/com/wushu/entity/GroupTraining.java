package com.wushu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"students", "trainings"})
@Entity
@Table(name = "group_training")

public class GroupTraining {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;


	@NotBlank(message = "Group name is required")
	@Size(max = 100, message = "Group name cannot exceed 100 characters")
	@Column(name = "name", nullable = false)
	private String name;


	@NotBlank(message = "Level is required")
	@Size(max = 50, message = "Level cannot exceed 50 characters")
	@Column(name = "level", nullable = false)
	private String level;


	@NotNull(message = "Coach is required")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "coach_id", nullable = false)
	private Coach coach;

	@ManyToMany(mappedBy = "groups")
	private Set<Student> students =  new HashSet<>();

	@OneToMany(mappedBy = "group")
	private Set<TrainingSession> trainings = new HashSet<>();

}