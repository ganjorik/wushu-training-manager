package com.wushu.controller;

import com.wushu.dto.ExerciseDto;
import com.wushu.entity.Exercise;
import com.wushu.mapper.ExerciseMapper;
import com.wushu.service.ExerciseService;
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
@RequestMapping("/exercises")
public class ExerciseController {

	private final ExerciseService exerciseService;
	private final ExerciseMapper exerciseMapper;

	@GetMapping
	public String exercises(
			@RequestParam(defaultValue = "0") int page,
			Model model) {

		Page<Exercise> exercisePage =
				exerciseService.getAll(
						PageRequest.of(page, 5)
				);

		model.addAttribute(
				"exercises",
				exerciseMapper.toDtoList(
						exercisePage.getContent()
				)
		);

		model.addAttribute(
				"currentPage",
				page
		);

		model.addAttribute(
				"totalPages",
				exercisePage.getTotalPages()
		);

		return "exercises";
	}

	@GetMapping("/new")
	public String createPage(Model model) {

		model.addAttribute(
				"exercise",
				new ExerciseDto()
		);

		return "exercise-create";
	}

	@PostMapping
	public String createExercise(
			@Valid
			@ModelAttribute("exercise") ExerciseDto exerciseDto,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			return "exercise-create";
		}

		exerciseService.create(
				exerciseMapper.toEntity(exerciseDto)
		);

		return "redirect:/exercises";
	}

	@GetMapping("/edit/{id}")
	public String editPage(
			@PathVariable Long id,
			Model model) {

		model.addAttribute(
				"exercise",
				exerciseMapper.toDto(
						exerciseService.getById(id)
				)
		);

		return "exercise-edit";
	}

	@PostMapping("/update/{id}")
	public String updateExercise(
			@PathVariable Long id,
			@Valid
			@ModelAttribute("exercise") ExerciseDto exerciseDto,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			return "exercise-edit";
		}

		exerciseDto.setId(id);

		exerciseService.update(
				exerciseMapper.toEntity(exerciseDto)
		);

		return "redirect:/exercises";
	}

	@PostMapping("/delete/{id}")
	public String deleteExercise(
			@PathVariable Long id) {

		exerciseService.delete(id);

		return "redirect:/exercises";
	}
}


