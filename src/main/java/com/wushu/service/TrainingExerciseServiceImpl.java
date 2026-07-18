package com.wushu.service;

import com.wushu.entity.Exercise;
import com.wushu.entity.TrainingExercise;
import com.wushu.entity.TrainingSession;
import com.wushu.exception.BusinessException;
import com.wushu.repository.ExerciseRepository;
import com.wushu.repository.TrainingExerciseRepository;
import com.wushu.repository.TrainingSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrainingExerciseServiceImpl
		implements TrainingExerciseService {

	private final TrainingExerciseRepository repository;
	private final TrainingSessionRepository trainingSessionRepository;
	private final ExerciseRepository exerciseRepository;

	@Override
	public List<TrainingExercise> getByTraining(Long trainingId) {

		return repository.findByTrainingIdOrderByOrderIndex(trainingId);
	}

	@Override
	@Transactional
	public void addExercise(
			Long trainingId,
			Long exerciseId,
			Integer repetitions,
			Integer duration,
			Integer orderIndex) {

		log.info(
				"Adding exercise {} to training {}",
				exerciseId,
				trainingId
		);

		TrainingSession training =
				getTraining(trainingId);

		Exercise exercise =
				getExercise(exerciseId);

		TrainingExercise item = new TrainingExercise();

		item.setTraining(training);
		item.setExercise(exercise);
		item.setRepetitions(repetitions);
		item.setDuration(duration);
		item.setOrderIndex(orderIndex);

		repository.save(item);

		log.info(
				"Exercise {} successfully assigned to training {}",
				exerciseId,
				trainingId
		);
	}

	@Override
	@Transactional
	public void delete(
			Long trainingId,
			Long exerciseId) {

		log.info(
				"Removing exercise {} from training {}",
				exerciseId,
				trainingId
		);

		repository.deleteByTrainingIdAndExerciseId(
				trainingId,
				exerciseId
		);

		log.info(
				"Exercise {} successfully removed from training {}",
				exerciseId,
				trainingId
		);
	}

	@Override
	@Transactional
	public void deleteAllByTraining(Long trainingId) {

		log.info(
				"Removing all exercises from training {}",
				trainingId
		);

		repository.deleteAllByTrainingId(trainingId);

		log.info(
				"All exercises successfully removed from training {}",
				trainingId
		);
	}

	private TrainingSession getTraining(Long id) {

		return trainingSessionRepository.findById(id)
				.orElseThrow(() -> {

					log.warn(
							"Training with id={} not found",
							id
					);

					return new BusinessException("Training not found");
				});
	}

	private Exercise getExercise(Long id) {

		return exerciseRepository.findById(id)
				.orElseThrow(() -> {

					log.warn(
							"Exercise with id={} not found",
							id
					);

					return new BusinessException("Exercise not found");
				});
	}
}