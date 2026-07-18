package com.wushu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "student")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"groups"})

public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;


	@NotBlank(message = "Student name is required")
	@Size(max = 100, message = "Student name cannot exceed 100 characters")
	@Column(name = "name", nullable = false)
	private String name;


	@NotNull(message = "Age is required")
	@Min(value = 6, message = "Age must be greater than 5")
	@Column(name = "age", nullable = false)
	private Integer age;


	@NotBlank(message = "Phone is required")
	@Size(max = 20, message = "Phone number is too long")
	@Column(name = "phone")
	private String phone;

	@ManyToMany
	@JoinTable(
			name = "student_group",
			joinColumns = @JoinColumn(name = "student_id"),
			inverseJoinColumns = @JoinColumn(name = "group_id")
	)
	private Set<GroupTraining> groups =  new HashSet<>();

}
