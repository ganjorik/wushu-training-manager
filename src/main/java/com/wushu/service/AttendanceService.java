package com.wushu.service;

import com.wushu.entity.Attendance;
import com.wushu.entity.Student;

import java.util.List;

public interface AttendanceService {

	List<Attendance> getByTraining(Long trainingId);

	Attendance markAttendance(
			Long trainingId,
			Long studentId,
			String status,
			String comment);

	long countPresent(Long trainingId);

	long countAbsent(Long trainingId);

	List<Student> getStudentsWithoutAttendance(Long trainingId);
}
