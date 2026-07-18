package com.wushu.service;

import com.wushu.entity.Coach;
import com.wushu.exception.BusinessException;
import com.wushu.repository.CoachRepository;
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
public class CoachServiceImpl implements CoachService {

	private final CoachRepository coachRepository;
	private final TrainingSessionRepository trainingSessionRepository;

	@Override
	public List<Coach> getAll() {
		return coachRepository.findAll();
	}

	@Override
	public Page<Coach> getAll(Pageable pageable) {
		return coachRepository.findAll(pageable);
	}

	@Override
	public Coach getById(Long id) {

		return coachRepository.findById(id)
				.orElseThrow(() -> {

					log.warn("Coach with id={} not found", id);

					return new BusinessException("Coach not found");
				});
	}

	@Override
	@Transactional
	public Coach create(Coach coach) {

		log.info("Creating coach '{}'", coach.getName());

		Coach savedCoach = coachRepository.save(coach);

		log.info(
				"Coach created successfully: id={}",
				savedCoach.getId()
		);

		return savedCoach;
	}

	@Override
	@Transactional
	public Coach update(Coach coach) {

		log.info("Updating coach id={}", coach.getId());

		Coach updatedCoach = coachRepository.save(coach);

		log.info(
				"Coach updated successfully: id={}",
				updatedCoach.getId()
		);

		return updatedCoach;
	}

	@Override
	@Transactional
	public void delete(Long id) {

		log.info("Deleting coach id={}", id);

		Coach coach = getById(id);

		if (coachRepository.hasGroups(id)) {

			log.warn(
					"Cannot delete coach {} because he has groups",
					id
			);

			throw new BusinessException(
					"Coach has groups. Delete them first."
			);
		}

		if (trainingSessionRepository.existsByCoachId(id)) {

			log.warn(
					"Cannot delete coach {} because he has trainings",
					id
			);

			throw new BusinessException(
					"Coach has trainings. Delete them first."
			);
		}

		coachRepository.delete(coach);

		log.info(
				"Coach deleted successfully: id={}",
				id
		);
	}
}
