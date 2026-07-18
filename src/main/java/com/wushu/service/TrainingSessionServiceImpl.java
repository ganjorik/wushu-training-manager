package com.wushu.service;

import com.wushu.entity.Coach;
import com.wushu.entity.GroupTraining;
import com.wushu.entity.TrainingSession;
import com.wushu.exception.BusinessException;
import com.wushu.repository.AttendanceRepository;
import com.wushu.repository.CoachRepository;
import com.wushu.repository.GroupTrainingRepository;
import com.wushu.repository.TrainingSessionRepository;
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
public class TrainingSessionServiceImpl
		implements TrainingSessionService {

	private final TrainingSessionRepository trainingSessionRepository;
	private final GroupTrainingRepository groupRepository;
	private final CoachRepository coachRepository;
	private final TrainingExerciseService trainingExerciseService;
	private final AttendanceRepository attendanceRepository;

	@Override
	public List<TrainingSession> getAll() {

		return trainingSessionRepository.findAll();
	}

	@Override
	public Page<TrainingSession> getAll(Pageable pageable) {

		return trainingSessionRepository.findAll(pageable);
	}

	@Override
	public TrainingSession getById(Long id) {

		return trainingSessionRepository.findById(id)
				.orElseThrow(() -> {

					log.warn("Training with id={} not found", id);

					return new BusinessException("Training not found");
				});
	}

	@Override
	@Transactional
	public TrainingSession create(TrainingSession training) {

		log.info("Creating training '{}'", training.getTopic());

		if (training.getGroup() == null || training.getGroup().getId() == null) {

			log.warn("Cannot create training because group is not specified");

			throw new BusinessException("Group is required");
		}

		if (training.getCoach() == null || training.getCoach().getId() == null) {

			log.warn("Cannot create training because coach is not specified");

			throw new BusinessException("Coach is required");
		}

		GroupTraining group =
				getGroup(training.getGroup().getId());

		Coach coach =
				getCoach(training.getCoach().getId());

		training.setGroup(group);
		training.setCoach(coach);

		TrainingSession savedTraining =
				trainingSessionRepository.save(training);

		log.info(
				"Training created successfully: id={}",
				savedTraining.getId()
		);

		return savedTraining;
	}

	@Override
	@Transactional
	public TrainingSession update(TrainingSession training) {

		log.info("Updating training id={}", training.getId());

		if (training.getGroup() == null || training.getGroup().getId() == null) {

			log.warn("Cannot update training because group is not specified");

			throw new BusinessException("Group is required");
		}

		if (training.getCoach() == null || training.getCoach().getId() == null) {

			log.warn("Cannot update training because coach is not specified");

			throw new BusinessException("Coach is required");
		}

		GroupTraining group =
				getGroup(training.getGroup().getId());

		Coach coach =
				getCoach(training.getCoach().getId());

		training.setGroup(group);
		training.setCoach(coach);

		TrainingSession updatedTraining =
				trainingSessionRepository.save(training);

		log.info(
				"Training updated successfully: id={}",
				updatedTraining.getId()
		);

		return updatedTraining;
	}

	@Override
	@Transactional
	public void delete(Long id) {

		log.info("Deleting training id={}", id);

		if (attendanceRepository.existsByTrainingId(id)) {

			log.warn(
					"Cannot delete training {} because attendance history exists",
					id
			);

			throw new BusinessException(
					"Training cannot be deleted because attendance history must be preserved."
			);
		}

		TrainingSession training = getById(id);

		trainingExerciseService.deleteAllByTraining(id);

		trainingSessionRepository.delete(training);

		log.info(
				"Training deleted successfully: id={}",
				id
		);
	}

	@Override
	public TrainingSession getByIdWithRelations(Long id) {

		return trainingSessionRepository.findWithRelationsById(id)
				.orElseThrow(() -> {

					log.warn("Training with id={} not found", id);

					return new BusinessException("Training not found");
				});
	}

	private GroupTraining getGroup(Long id) {

		return groupRepository.findById(id)
				.orElseThrow(() -> {

					log.warn(
							"Group with id={} not found",
							id
					);

					return new BusinessException("Group not found");
				});
	}

	private Coach getCoach(Long id) {

		return coachRepository.findById(id)
				.orElseThrow(() -> {

					log.warn(
							"Coach with id={} not found",
							id
					);

					return new BusinessException("Coach not found");
				});
	}
}