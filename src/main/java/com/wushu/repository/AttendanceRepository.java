package com.wushu.repository;

import com.wushu.entity.Attendance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository
		extends JpaRepository<Attendance, Long> {

	@EntityGraph(attributePaths = {
			"student",
			"training"
	})
	List<Attendance> findByTrainingId(Long trainingId);

	boolean existsByStudentId(Long studentId);

	boolean existsByTrainingId(Long trainingId);

	boolean existsByTrainingIdAndStudentId(
			Long trainingId,
			Long studentId
	);
}
