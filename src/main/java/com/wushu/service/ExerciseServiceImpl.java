package com.wushu.service;

import com.wushu.entity.Exercise;
import com.wushu.exception.BusinessException;
import com.wushu.repository.ExerciseRepository;
import com.wushu.repository.TrainingExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExerciseServiceImpl implements ExerciseService {

	private final ExerciseRepository exerciseRepository;
	private final TrainingExerciseRepository trainingExerciseRepository;

	@Override
	public List<Exercise> getAll() {

		return exerciseRepository.findAll();
	}

	@Override
	public Page<Exercise> getAll(Pageable pageable) {

		return exerciseRepository.findAll(pageable);
	}

	@Override
	public Exercise getById(Long id) {

		return exerciseRepository.findById(id)
				.orElseThrow(() -> {

					log.warn("Exercise with id={} not found", id);

					return new BusinessException("Exercise not found");
				});
	}

	@Override
	@Transactional
	public Exercise create(Exercise exercise) {

		log.info("Creating exercise '{}'", exercise.getName());

		Exercise savedExercise = exerciseRepository.save(exercise);

		log.info(
				"Exercise created successfully: id={}",
				savedExercise.getId()
		);

		return savedExercise;
	}

	@Override
	@Transactional
	public Exercise update(Exercise exercise) {

		log.info("Updating exercise id={}", exercise.getId());

		Exercise updatedExercise = exerciseRepository.save(exercise);

		log.info(
				"Exercise updated successfully: id={}",
				updatedExercise.getId()
		);

		return updatedExercise;
	}

	@Override
	@Transactional
	public void delete(Long id) {

		log.info("Deleting exercise id={}", id);

		if (trainingExerciseRepository.existsByExerciseId(id)) {

			log.warn(
					"Cannot delete exercise {} because it is used in trainings",
					id
			);

			throw new BusinessException(
					"Exercise is used in trainings. Delete training exercises first."
			);
		}

		Exercise exercise = getById(id);

		exerciseRepository.delete(exercise);

		log.info(
				"Exercise deleted successfully: id={}",
				id
		);
	}
}