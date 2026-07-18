package com.wushu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attendance")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "training_id", nullable = false)
	private TrainingSession training;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "comment")
	private String comment;
}