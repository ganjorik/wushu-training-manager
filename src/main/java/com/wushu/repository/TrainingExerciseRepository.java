package com.wushu.repository;

import com.wushu.entity.TrainingExercise;
import com.wushu.entity.TrainingExerciseId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TrainingExerciseRepository
		extends JpaRepository<TrainingExercise, TrainingExerciseId> {

	@EntityGraph(attributePaths = {
			"exercise",
			"training"
	})
	List<TrainingExercise> findByTrainingIdOrderByOrderIndex(Long trainingId);

	Optional<TrainingExercise> findByTrainingIdAndExerciseId(
			Long trainingId,
			Long exerciseId
	);

	boolean existsByExerciseId(Long exerciseId);

	boolean existsByTrainingId(Long trainingId);

	@Modifying
	@Transactional
	@Query("""
       delete from TrainingExercise te
       where te.training.id = :trainingId
       and te.exercise.id = :exerciseId
       """)
	void deleteByTrainingIdAndExerciseId(
			Long trainingId,
			Long exerciseId
	);

	@Modifying
	@Transactional
	@Query("""
       delete from TrainingExercise te
       where te.training.id = :trainingId
       """)
	void deleteAllByTrainingId(Long trainingId);
}
