package com.wushu.repository;

import com.wushu.entity.Coach;
import com.wushu.entity.GroupTraining;
import com.wushu.entity.Student;
import com.wushu.entity.TrainingSession;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TrainingSessionRepositoryTest {

    @Autowired
    private TrainingSessionRepository trainingSessionRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private GroupTrainingRepository groupTrainingRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findWithRelationsById_shouldReturnTrainingWithRelations() {
        // given
        Coach coach = createCoach();
        GroupTraining group = createGroup(coach);

        Student student1 = createStudent("Denis");
        Student student2 = createStudent("Alex");

        student1.getGroups().add(group);
        student2.getGroups().add(group);

        studentRepository.save(student1);
        studentRepository.save(student2);

        TrainingSession training = createTraining(coach, group);

        entityManager.flush();
        entityManager.clear();

        // when
        Optional<TrainingSession> result =
                trainingSessionRepository.findWithRelationsById(
                        training.getId()
                );

        // then
        assertTrue(result.isPresent());

        TrainingSession foundTraining = result.get();

        assertEquals(training.getId(), foundTraining.getId());
        assertEquals(group.getId(), foundTraining.getGroup().getId());
        assertEquals(coach.getId(), foundTraining.getCoach().getId());
        assertEquals(2, foundTraining.getGroup().getStudents().size());
    }

    @Test
    void findWithRelationsById_shouldReturnEmpty_whenTrainingDoesNotExist() {
        // given
        Long nonExistingId = 999L;

        // when
        Optional<TrainingSession> result =
                trainingSessionRepository.findWithRelationsById(nonExistingId);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void existsByCoachId_shouldReturnTrue_whenTrainingExists() {
        // given
        Coach coach = createCoach();
        GroupTraining group = createGroup(coach);

        createTraining(coach, group);

        // when
        boolean result =
                trainingSessionRepository.existsByCoachId(coach.getId());

        // then
        assertTrue(result);
    }

    @Test
    void existsByCoachId_shouldReturnFalse_whenTrainingDoesNotExist() {
        // given
        Coach coach = createCoach();

        // when
        boolean result =
                trainingSessionRepository.existsByCoachId(coach.getId());

        // then
        assertFalse(result);
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

    private Student createStudent(String name) {
        Student student = new Student();
        student.setName(name);
        student.setAge(18);
        student.setPhone("+375292147788");

        return studentRepository.save(student);
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
}