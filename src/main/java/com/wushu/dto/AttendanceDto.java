package com.wushu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttendanceDto {

	private Long id;

	private Long studentId;
	private String studentName;

	private String status;

	private String comment;
}
