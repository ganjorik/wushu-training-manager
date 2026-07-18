package com.wushu.repository;

import com.wushu.entity.Attendance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository
		extends JpaRepository<Attendance, Long> {

	@EntityGraph(attributePaths = {
			"student",
			"training"
	})
	List<Attendance> findByTrainingId(Long trainingId);

	Optional<Attendance> findByTrainingIdAndStudentId(
			Long trainingId,
			Long studentId);

	boolean existsByStudentId(Long studentId);

	boolean existsByTrainingId(Long trainingId);

	boolean existsByTrainingIdAndStudentId(
			Long trainingId,
			Long studentId
	);
}
