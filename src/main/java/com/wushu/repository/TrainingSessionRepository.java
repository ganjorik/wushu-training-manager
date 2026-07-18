package com.wushu.repository;

import com.wushu.entity.TrainingSession;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingSessionRepository
		extends JpaRepository<TrainingSession, Long> {

	@EntityGraph(attributePaths = {
			"group",
			"group.students",
			"coach"
	})

	Optional<TrainingSession> findWithRelationsById(Long id);

	boolean existsByCoachId(Long coachId);
}
