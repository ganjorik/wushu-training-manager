package com.wushu.controller;

import com.wushu.dto.TrainingSessionDto;
import com.wushu.entity.TrainingSession;
import com.wushu.mapper.CoachMapper;
import com.wushu.mapper.GroupTrainingMapper;
import com.wushu.mapper.TrainingSessionMapper;
import com.wushu.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/trainings")
public class TrainingSessionController {

	private final TrainingSessionService trainingService;
	private final GroupTrainingService groupService;
	private final CoachService coachService;
	private final TrainingExerciseService trainingExerciseService;
	private final ExerciseService exerciseService;
	private final AttendanceService attendanceService;

	private final TrainingSessionMapper trainingMapper;
	private final GroupTrainingMapper groupMapper;
	private final CoachMapper coachMapper;

	@GetMapping
	public String trainings(
			@RequestParam(defaultValue = "0") int page,
			Model model) {

		Page<TrainingSession> trainingPage =
				trainingService.getAll(
						PageRequest.of(page, 5)
				);

		model.addAttribute(
				"trainings",
				trainingMapper.toDtoList(
						trainingPage.getContent()
				)
		);

		model.addAttribute(
				"currentPage",
				page
		);

		model.addAttribute(
				"totalPages",
				trainingPage.getTotalPages()
		);

		model.addAttribute(
				"groups",
				groupMapper.toDtoList(
						groupService.getAll()
				)
		);

		model.addAttribute(
				"coaches",
				coachMapper.toDtoList(
						coachService.getAll()
				)
		);

		return "trainings";
	}

	@GetMapping("/new")
	public String createPage(Model model) {

		model.addAttribute(
				"training",
				new TrainingSessionDto()
		);

		model.addAttribute(
				"groups",
				groupMapper.toDtoList(
						groupService.getAll()
				)
		);

		model.addAttribute(
				"coaches",
				coachMapper.toDtoList(
						coachService.getAll()
				)
		);

		return "training-create";
	}

	@PostMapping
	public String createTraining(
			@Valid
			@ModelAttribute("training") TrainingSessionDto trainingDto,
			BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {

			model.addAttribute(
					"groups",
					groupMapper.toDtoList(
							groupService.getAll()
					)
			);

			model.addAttribute(
					"coaches",
					coachMapper.toDtoList(
							coachService.getAll()
					)
			);

			return "training-create";
		}

		trainingService.create(
				trainingMapper.toEntity(trainingDto)
		);

		return "redirect:/trainings";
	}

	@GetMapping("/edit/{id}")
	public String editPage(
			@PathVariable Long id,
			Model model) {

		model.addAttribute(
				"training",
				trainingMapper.toDto(
						trainingService.getById(id)
				)
		);

		model.addAttribute(
				"groups",
				groupMapper.toDtoList(
						groupService.getAll()
				)
		);

		model.addAttribute(
				"coaches",
				coachMapper.toDtoList(
						coachService.getAll()
				)
		);

		return "training-edit";
	}

	@PostMapping("/update/{id}")
	public String updateTraining(
			@PathVariable Long id,
			@Valid
			@ModelAttribute("training") TrainingSessionDto trainingDto,
			BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {

			model.addAttribute(
					"groups",
					groupMapper.toDtoList(
							groupService.getAll()
					)
			);

			model.addAttribute(
					"coaches",
					coachMapper.toDtoList(
							coachService.getAll()
					)
			);

			return "training-edit";
		}

		trainingDto.setId(id);

		trainingService.update(
				trainingMapper.toEntity(trainingDto)
		);

		return "redirect:/trainings";
	}

	@PostMapping("/delete/{id}")
	public String deleteTraining(
			@PathVariable Long id) {

		trainingService.delete(id);

		return "redirect:/trainings";
	}

	@GetMapping("/{id}")
	public String viewTraining(
			@PathVariable Long id,
			Model model) {

		model.addAttribute(
				"training",
				trainingService.getByIdWithRelations(id)
		);

		model.addAttribute(
				"items",
				trainingExerciseService.getByTraining(id)
		);

		model.addAttribute(
				"exercises",
				exerciseService.getAll()
		);

		model.addAttribute(
				"attendance",
				attendanceService.getByTraining(id)
		);

		model.addAttribute(
				"presentCount",
				attendanceService.countPresent(id)
		);

		model.addAttribute(
				"absentCount",
				attendanceService.countAbsent(id)
		);

		model.addAttribute(
				"availableStudents",
				attendanceService.getStudentsWithoutAttendance(id)
		);

		return "training-view";
	}

	@PostMapping("/{id}/exercises")
	public String addExercise(
			@PathVariable Long id,
			@RequestParam Long exerciseId,
			@RequestParam Integer repetitions,
			@RequestParam Integer duration,
			@RequestParam Integer orderIndex) {

		trainingExerciseService.addExercise(
				id,
				exerciseId,
				repetitions,
				duration,
				orderIndex
		);

		return "redirect:/trainings/" + id;
	}

	@PostMapping("/{trainingId}/exercises/{exerciseId}/delete")
	public String deleteExercise(
			@PathVariable Long trainingId,
			@PathVariable Long exerciseId) {

		trainingExerciseService.delete(
				trainingId,
				exerciseId
		);

		return "redirect:/trainings/" + trainingId;
	}

	@PostMapping("/{trainingId}/attendance")
	public String markAttendance(
			@PathVariable Long trainingId,
			@RequestParam Long studentId,
			@RequestParam String status,
			@RequestParam(required = false) String comment) {

		attendanceService.markAttendance(
				trainingId,
				studentId,
				status,
				comment
		);

		return "redirect:/trainings/" + trainingId;
	}
}
