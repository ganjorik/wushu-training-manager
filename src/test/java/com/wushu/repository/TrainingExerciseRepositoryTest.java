package com.wushu.repository;

import com.wushu.entity.Coach;
import com.wushu.entity.DifficultyLevel;
import com.wushu.entity.Exercise;
import com.wushu.entity.ExerciseType;
import com.wushu.entity.GroupTraining;
import com.wushu.entity.TrainingExercise;
import com.wushu.entity.TrainingSession;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TrainingExerciseRepositoryTest {

    @Autowired
    private TrainingExerciseRepository trainingExerciseRepository;

    @Autowired
    private TrainingSessionRepository trainingSessionRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private GroupTrainingRepository groupTrainingRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findByTrainingIdOrderByOrderIndex_shouldReturnExercisesInOrder() {
        // given
        Coach coach = createCoach();
        GroupTraining group = createGroup(coach);
        TrainingSession training = createTraining(coach, group);

        Exercise exercise1 = createExercise("Punch");
        Exercise exercise2 = createExercise("Kick");
        Exercise exercise3 = createExercise("Block");

        createTrainingExercise(training, exercise1, 10, 2);
        createTrainingExercise(training, exercise2, 8, 1);
        createTrainingExercise(training, exercise3, 5, 3);

        entityManager.flush();
        entityManager.clear();

        // when
        List<TrainingExercise> result =
                trainingExerciseRepository
                        .findByTrainingIdOrderByOrderIndex(training.getId());

        // then
        assertEquals(3, result.size());

        assertEquals(exercise2.getId(), result.get(0).getExercise().getId());
        assertEquals(1, result.get(0).getOrderIndex());

        assertEquals(exercise1.getId(), result.get(1).getExercise().getId());
        assertEquals(2, result.get(1).getOrderIndex());

        assertEquals(exercise3.getId(), result.get(2).getExercise().getId());
        assertEquals(3, result.get(2).getOrderIndex());
    }

    @Test
    void findByTrainingIdOrderByOrderIndex_shouldReturnEmptyList_whenNoExercisesExist() {
        // given
        Coach coach = createCoach();
        GroupTraining group = createGroup(coach);
        TrainingSession training = createTraining(coach, group);

        // when
        List<TrainingExercise> result =
                trainingExerciseRepository
                        .findByTrainingIdOrderByOrderIndex(training.getId());

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void existsByExerciseId_shouldReturnTrue_whenExerciseIsAssignedToTraining() {
        // given
        Coach coach = createCoach();
        GroupTraining group = createGroup(coach);
        TrainingSession training = createTraining(coach, group);

        Exercise exercise = createExercise("Punch");

        createTrainingExercise(training, exercise, 10, 1);

        // when
        boolean result =
                trainingExerciseRepository.existsByExerciseId(exercise.getId());

        // then
        assertTrue(result);
    }

    @Test
    void existsByExerciseId_shouldReturnFalse_whenExerciseIsNotAssignedToTraining() {
        // given
        Exercise exercise = createExercise("Punch");

        // when
        boolean result =
                trainingExerciseRepository.existsByExerciseId(exercise.getId());

        // then
        assertFalse(result);
    }

    @Test
    void deleteByTrainingIdAndExerciseId_shouldDeleteOnlySpecifiedExercise() {
        // given
        Coach coach = createCoach();
        GroupTraining group = createGroup(coach);
        TrainingSession training = createTraining(coach, group);

        Exercise exercise1 = createExercise("Punch");
        Exercise exercise2 = createExercise("Kick");

        createTrainingExercise(training, exercise1, 10, 1);
        createTrainingExercise(training, exercise2, 8, 2);

        // when
        trainingExerciseRepository.deleteByTrainingIdAndExerciseId(
                training.getId(),
                exercise1.getId()
        );

        entityManager.flush();
        entityManager.clear();

        // then
        List<TrainingExercise> result =
                trainingExerciseRepository
                        .findByTrainingIdOrderByOrderIndex(training.getId());

        assertEquals(1, result.size());
        assertEquals(exercise2.getId(), result.get(0).getExercise().getId());
    }

    @Test
    void deleteAllByTraining_shouldDeleteAllExercisesFromSpecifiedTraining() {
        // given
        Coach coach = createCoach();
        GroupTraining group = createGroup(coach);

        TrainingSession training1 = createTraining(coach, group);
        TrainingSession training2 = createTraining(coach, group);

        Exercise exercise1 = createExercise("Punch");
        Exercise exercise2 = createExercise("Kick");
        Exercise exercise3 = createExercise("Block");

        createTrainingExercise(training1, exercise1, 10, 1);
        createTrainingExercise(training1, exercise2, 8, 2);
        createTrainingExercise(training2, exercise3, 5, 1);

        // when
        trainingExerciseRepository.deleteAllByTrainingId(training1.getId());

        entityManager.flush();
        entityManager.clear();

        // then
        List<TrainingExercise> resultTraining1 =
                trainingExerciseRepository
                        .findByTrainingIdOrderByOrderIndex(training1.getId());

        List<TrainingExercise> resultTraining2 =
                trainingExerciseRepository
                        .findByTrainingIdOrderByOrderIndex(training2.getId());

        assertTrue(resultTraining1.isEmpty());
        assertEquals(1, resultTraining2.size());
        assertEquals(
                exercise3.getId(),
                resultTraining2.get(0).getExercise().getId()
        );
    }

    private Coach createCoach() {
        Coach coach = new Coach();
        coach.setName("John Smith");
        coach.setExperienceYears(10);
        coach.setPhone("+375292148963");

        return coachRepository.save(coach);
    }

    private GroupTraining createGroup(Coach coach) {
        GroupTraining group = new GroupTraining();
        group.setName("Junior");
        group.setLevel("Beginner");
        group.setCoach(coach);

        return groupTrainingRepository.save(group);
    }

    private TrainingSession createTraining(
            Coach coach,
            GroupTraining group
    ) {
        TrainingSession training = new TrainingSession();
        training.setDate(LocalDate.now());
        training.setDuration(90);
        training.setCoach(coach);
        training.setGroup(group);
        training.setTopic("Kicks");

        return trainingSessionRepository.save(training);
    }

    private Exercise createExercise(String name) {
        Exercise exercise = new Exercise();
        exercise.setName(name);
        exercise.setType(ExerciseType.values()[0]);
        exercise.setDifficulty(DifficultyLevel.values()[0]);
        exercise.setDescription("Test exercise");

        return exerciseRepository.save(exercise);
    }

    private TrainingExercise createTrainingExercise(
            TrainingSession training,
            Exercise exercise,
            Integer repetitions,
            Integer orderIndex
    ) {
        TrainingExercise trainingExercise = new TrainingExercise();

        trainingExercise.setTraining(training);
        trainingExercise.setExercise(exercise);
        trainingExercise.setRepetitions(repetitions);
        trainingExercise.setDuration(60);
        trainingExercise.setOrderIndex(orderIndex);

        return trainingExerciseRepository.save(trainingExercise);
    }
}