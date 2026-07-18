package com.wushu.service;

import com.wushu.entity.Attendance;
import com.wushu.entity.Student;
import com.wushu.entity.TrainingSession;
import com.wushu.exception.BusinessException;
import com.wushu.repository.AttendanceRepository;
import com.wushu.repository.StudentRepository;
import com.wushu.repository.TrainingSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final TrainingSessionRepository trainingSessionRepository;
	private final StudentRepository studentRepository;

	@Override
	public List<Attendance> getByTraining(Long trainingId) {

		return attendanceRepository.findByTrainingId(trainingId);
	}

	@Override
	@Transactional
	public Attendance markAttendance(
			Long trainingId,
			Long studentId,
			String status,
			String comment) {

		log.info(
				"Marking attendance: trainingId={}, studentId={}, status={}",
				trainingId,
				studentId,
				status
		);

		if (attendanceRepository.existsByTrainingIdAndStudentId(
				trainingId,
				studentId)) {

			log.warn(
					"Attendance already exists: trainingId={}, studentId={}",
					trainingId,
					studentId
			);

			throw new BusinessException(
					"Attendance for this student has already been recorded."
			);
		}

		TrainingSession training = getTraining(trainingId);

		Student student = getStudent(studentId);

		Attendance attendance = new Attendance();

		attendance.setTraining(training);
		attendance.setStudent(student);
		attendance.setStatus(status);
		attendance.setComment(comment);

		Attendance savedAttendance =
				attendanceRepository.save(attendance);

		log.info(
				"Attendance saved successfully: id={}",
				savedAttendance.getId()
		);

		return savedAttendance;
	}

	@Override
	public long countPresent(Long trainingId) {

		return attendanceRepository.findByTrainingId(trainingId)
				.stream()
				.filter(a -> "PRESENT".equals(a.getStatus()))
				.count();
	}

	@Override
	public long countAbsent(Long trainingId) {

		return attendanceRepository.findByTrainingId(trainingId)
				.stream()
				.filter(a -> "ABSENT".equals(a.getStatus()))
				.count();
	}

	@Override
	public List<Student> getStudentsWithoutAttendance(Long trainingId) {

		TrainingSession training =
				trainingSessionRepository.findWithRelationsById(trainingId)
						.orElseThrow(() -> {

							log.warn(
									"Training with id={} not found",
									trainingId
							);

							return new BusinessException("Training not found");
						});

		List<Student> students =
				new ArrayList<>(training.getGroup().getStudents());

		List<Long> markedIds =
				attendanceRepository.findByTrainingId(trainingId)
						.stream()
						.map(a -> a.getStudent().getId())
						.toList();

		return students.stream()
				.filter(s -> !markedIds.contains(s.getId()))
				.toList();
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

	private Student getStudent(Long id) {

		return studentRepository.findById(id)
				.orElseThrow(() -> {

					log.warn("Student with id={} not found",
							id
					);

					return new BusinessException("Student not found");
				});
	}
}