package com.wushu.service;

import com.wushu.entity.Coach;
import com.wushu.entity.GroupTraining;
import com.wushu.entity.Student;
import com.wushu.exception.BusinessException;
import com.wushu.repository.CoachRepository;
import com.wushu.repository.GroupTrainingRepository;
import com.wushu.repository.StudentRepository;
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
public class GroupTrainingServiceImpl
		implements GroupTrainingService {

	private final GroupTrainingRepository groupRepository;
	private final CoachRepository coachRepository;
	private final StudentRepository studentRepository;

	@Override
	public List<GroupTraining> getAll() {

		return groupRepository.findAll();
	}

	@Override
	public Page<GroupTraining> getAll(Pageable pageable) {

		return groupRepository.findAll(pageable);
	}

	@Override
	public GroupTraining getById(Long id) {

		return groupRepository.findById(id)
				.orElseThrow(() -> {

					log.warn("Group with id={} not found", id);

					return new BusinessException("Group not found");
				});
	}

	@Override
	@Transactional
	public GroupTraining create(GroupTraining group) {

		log.info("Creating group '{}'", group.getName());

		if (group.getCoach() == null || group.getCoach().getId() == null) {

			log.warn("Cannot create group because coach is not specified");

			throw new BusinessException("Coach is required");
		}

		Coach coach = getCoach(group.getCoach().getId());

		group.setCoach(coach);

		GroupTraining savedGroup = groupRepository.save(group);

		log.info(
				"Group created successfully: id={}",
				savedGroup.getId()
		);

		return savedGroup;
	}

	@Override
	@Transactional
	public GroupTraining update(GroupTraining group) {

		log.info("Updating group id={}", group.getId());

		if (group.getCoach() == null || group.getCoach().getId() == null) {

			log.warn("Cannot update group because coach is not specified");

			throw new BusinessException("Coach is required");
		}

		Coach coach = getCoach(group.getCoach().getId());

		group.setCoach(coach);

		GroupTraining updatedGroup = groupRepository.save(group);

		log.info(
				"Group updated successfully: id={}",
				updatedGroup.getId()
		);

		return updatedGroup;
	}

	@Override
	@Transactional
	public void delete(Long id) {

		log.info("Deleting group id={}", id);

		GroupTraining group = getById(id);

		if (groupRepository.existsByIdAndTrainingsIsNotEmpty(id)) {

			log.warn(
					"Cannot delete group {} because it has trainings",
					id
			);

			throw new BusinessException(
					"Group has trainings. Delete trainings first."
			);
		}

		groupRepository.removeStudents(id);

		groupRepository.delete(group);

		log.info(
				"Group deleted successfully: id={}",
				id
		);
	}

	@Override
	public GroupTraining getByIdWithStudents(Long id) {

		return groupRepository.findWithStudentsById(id)
				.orElseThrow(() -> {

					log.warn("Group with id={} not found", id);

					return new BusinessException("Group not found");
				});
	}

	@Override
	@Transactional
	public void addStudent(
			Long groupId,
			Long studentId) {

		log.info(
				"Adding student {} to group {}",
				studentId,
				groupId
		);

		GroupTraining group = getById(groupId);

		Student student = getStudent(studentId);

		student.getGroups().add(group);

		studentRepository.save(student);

		log.info(
				"Student {} successfully added to group {}",
				studentId,
				groupId
		);
	}

	@Override
	@Transactional
	public void removeStudent(
			Long groupId,
			Long studentId) {

		log.info(
				"Removing student {} from group {}",
				studentId,
				groupId
		);

		Student student = getStudent(studentId);

		student.getGroups()
				.removeIf(g -> g.getId().equals(groupId));

		studentRepository.save(student);

		log.info(
				"Student {} successfully removed from group {}",
				studentId,
				groupId
		);
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

	private Student getStudent(Long id) {

		return studentRepository.findById(id)
				.orElseThrow(() -> {

					log.warn(
							"Student with id={} not found",
							id
					);

					return new BusinessException("Student not found");
				});
	}
}