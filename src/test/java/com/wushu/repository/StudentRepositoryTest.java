package com.wushu.repository;

import com.wushu.entity.Coach;
import com.wushu.entity.GroupTraining;
import com.wushu.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupTrainingRepository groupTrainingRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Test
    void findStudentsNotInGroup_shouldReturnStudentsNotInGroup() {
        // given
        Coach coach = createCoach();
        GroupTraining group = createGroup(coach);

        Student studentInGroup = createStudent("Denis");
        Student studentNotInGroup = createStudent("Alex");

        studentInGroup.getGroups().add(group);
        studentRepository.save(studentInGroup);

        // when
        List<Student> students =
                studentRepository.findStudentsNotInGroup(group.getId());

        // then
        assertEquals(1, students.size());
        assertEquals(studentNotInGroup.getId(), students.get(0).getId());
    }

    @Test
    void findStudentsNotInGroup_shouldReturnAllStudents_whenGroupIsEmpty() {
        // given
        Coach coach = createCoach();
        GroupTraining group = createGroup(coach);

        Student student1 = createStudent("Denis");
        Student student2 = createStudent("Alex");

        // when
        List<Student> students =
                studentRepository.findStudentsNotInGroup(group.getId());

        // then
        assertEquals(2, students.size());
        assertTrue(
                students.stream()
                        .anyMatch(student -> student.getId().equals(student1.getId()))
        );
        assertTrue(
                students.stream()
                        .anyMatch(student -> student.getId().equals(student2.getId()))
        );
    }

    @Test
    void findWithGroupsById_shouldReturnStudentWithGroups() {
        // given
        Coach coach = createCoach();
        GroupTraining group = createGroup(coach);

        Student student = createStudent("Denis");

        student.getGroups().add(group);
        studentRepository.save(student);

        // when
        Optional<Student> result =
                studentRepository.findWithGroupsById(student.getId());

        // then
        assertTrue(result.isPresent());

        Student foundStudent = result.get();

        assertEquals(student.getId(), foundStudent.getId());
        assertEquals("Denis", foundStudent.getName());
        assertEquals(1, foundStudent.getGroups().size());
        assertEquals(group.getId(),
                foundStudent.getGroups().iterator().next().getId());
    }

    @Test
    void findWithGroupsById_shouldReturnEmpty_whenStudentDoesNotExist() {
        // given
        Long nonExistingId = 999L;

        // when
        Optional<Student> result =
                studentRepository.findWithGroupsById(nonExistingId);

        // then
        assertTrue(result.isEmpty());
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
}