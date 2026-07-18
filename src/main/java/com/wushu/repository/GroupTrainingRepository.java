package com.wushu.repository;

import com.wushu.entity.GroupTraining;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface GroupTrainingRepository
		extends JpaRepository<GroupTraining, Long> {

	@EntityGraph(attributePaths = {
			"coach",
			"students"
	})
	Optional<GroupTraining> findWithStudentsById(Long id);

	boolean existsByIdAndTrainingsIsNotEmpty(Long id);

	@Modifying
	@Transactional
	@Query(value = """
			delete
			from student_group
			where group_id = :groupId
			""",
			nativeQuery = true)
	void removeStudents(Long groupId);
}
