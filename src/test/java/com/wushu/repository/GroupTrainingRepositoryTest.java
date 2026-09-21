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
class GroupTrainingRepositoryTest {

    @Autowired
    private GroupTrainingRepository groupTrainingRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TrainingSessionRepository trainingSessionRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findWithStudentsById_shouldReturnGroupWithStudents() {
        // given
        Coach coach = createCoach();
        GroupTraining group = createGroup(coach);

        Student student1 = createStudent("Denis");
        Student student2 = createStudent("Alex");

        student1.getGroups().add(group);
        student2.getGroups().add(group);

        studentRepository.save(student1);
        studentRepository.save(student2);

        entityManager.flush();

        Number count = (Number) entityManager
                .createNativeQuery("""
                    select count(*)
                    from student_group
                    where group_id = :groupId
                    """)
                .setParameter("groupId", group.getId())
                .getSingleResult();

        assertEquals(2, count.intValue());

        entityManager.clear();

        // when
        Optional<GroupTraining> result =
                groupTrainingRepository.findWithStudentsById(group.getId());

        // then
        assertTrue(result.isPresent());

        GroupTraining foundGroup = result.get();

        assertEquals(group.getId(), foundGroup.getId());
        assertEquals("Junior", foundGroup.getName());
        assertEquals(coach.getId(), foundGroup.getCoach().getId());
        assertEquals(2, foundGroup.getStudents().size());
    }

    @Test
    void findWithStudentsById_shouldReturnEmpty_whenGroupDoesNotExist() {
        // given
        Long nonExistingId = 999L;

        // when
        Optional<GroupTraining> result =
                groupTrainingRepository.findWithStudentsById(nonExistingId);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void existsByIdAndTrainingsIsNotEmpty_shouldReturnFalse_whenGroupHasNoTrainings() {
        // given
        Coach coach = createCoach();
        GroupTraining group = createGroup(coach);

        // when
        boolean result =
                groupTrainingRepository.existsByIdAndTrainingsIsNotEmpty(
                        group.getId()
                );

        // then
        assertFalse(result);
    }

    @Test
    void existsByIdAndTrainingsIsNotEmpty_shouldReturnTrue_whenGroupHasTrainings() {
        // given
        Coach coach = createCoach();
        GroupTraining group = createGroup(coach);

        createTraining(coach, group);

        // when
        boolean result =
                groupTrainingRepository.existsByIdAndTrainingsIsNotEmpty(
                        group.getId()
                );

        // then
        assertTrue(result);
    }

    @Test
    void removeStudents_shouldRemoveAllStudentsFromGroup() {
        // given
        Coach coach = createCoach();
        GroupTraining group = createGroup(coach);

        Student student1 = createStudent("Denis");
        Student student2 = createStudent("Alex");

        student1.getGroups().add(group);
        student2.getGroups().add(group);

        studentRepository.save(student1);
        studentRepository.save(student2);

        entityManager.flush();
        entityManager.clear();

        // when
        groupTrainingRepository.removeStudents(group.getId());

        entityManager.flush();
        entityManager.clear();

        // then
        Optional<GroupTraining> result =
                groupTrainingRepository.findWithStudentsById(group.getId());

        assertTrue(result.isPresent());
        assertTrue(result.get().getStudents().isEmpty());
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
