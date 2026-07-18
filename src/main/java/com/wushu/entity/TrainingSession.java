package com.wushu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "training_session")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class TrainingSession {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;


	@NotNull(message = "Date is required")
	@Column(name = "date", nullable = false)
	private LocalDate date;


	@NotNull(message = "Duration is required")
	@Min(value = 1, message = "Duration must be greater than 0")
	@Column(name = "duration", nullable = false)
	private Integer duration;


	@NotNull(message = "Group is required")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id", nullable = false)
	private GroupTraining group;


	@NotNull(message = "Coach is required")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coach_id", nullable = false)
	private Coach coach;


	@NotBlank(message = "Topic is required")
	@Size(max = 255, message = "Topic cannot exceed 255 characters")
	@Column(name = "topic")
	private String topic;
}