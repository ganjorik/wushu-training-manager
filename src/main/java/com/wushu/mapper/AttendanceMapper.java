package com.wushu.mapper;

import com.wushu.dto.AttendanceDto;
import com.wushu.entity.Attendance;

public class AttendanceMapper {

	public static AttendanceDto toDto(Attendance attendance) {

		AttendanceDto dto = new AttendanceDto();

		dto.setId(attendance.getId());

		dto.setStudentId(
				attendance.getStudent().getId()
		);

		dto.setStudentName(
				attendance.getStudent().getName()
		);

		dto.setStatus(
				attendance.getStatus()
		);

		dto.setComment(
				attendance.getComment()
		);

		return dto;
	}
}
