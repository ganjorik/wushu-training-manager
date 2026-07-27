package com.wushu.repository;

import com.wushu.entity.Coach;
import com.wushu.entity.GroupTraining;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CoachRepositoryTest {

	@Autowired
	private CoachRepository coachRepository;

	@Autowired
	private GroupTrainingRepository groupTrainingRepository;

	@Test
	void shouldReturnFalseWhenCoachHasNoGroups() {

		Coach coach = new Coach(
				null,
				"Test Coach",
				10,
				"+375298527896",
				null
		);

		coach =  coachRepository.save(coach);

		boolean result = coachRepository.hasGroups(coach.getId());

		assertFalse(result);
	}

	@Test
	void shouldReturnTrueWhenCoachHasGroups() {

		Coach coach = new Coach(
				null,
				"Test Coach",
				15,
				"+375291597538",
				null
		);

		coach =  coachRepository.save(coach);

		GroupTraining groupTraining = new GroupTraining(
				null,
				"Test Group",
				"Top",
				coach,
				null,
				null
		);

		groupTrainingRepository.save(groupTraining);

		boolean result = coachRepository.hasGroups(coach.getId());

		assertTrue(result);
	}
}
