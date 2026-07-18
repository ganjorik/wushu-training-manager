package com.wushu.controller;

import com.wushu.dto.CoachDto;
import com.wushu.entity.Coach;
import com.wushu.mapper.CoachMapper;
import com.wushu.service.CoachService;
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
public class CoachController {

	private final CoachService coachService;
	private final CoachMapper coachMapper;

	@GetMapping("/coaches")
	public String coaches(
			@RequestParam(defaultValue = "0") int page,
			Model model) {

		Page<Coach> coachPage =
				coachService.getAll(
						PageRequest.of(page, 5)
				);

		model.addAttribute(
				"coaches",
				coachMapper.toDtoList(
						coachPage.getContent()
				)
		);

		model.addAttribute(
				"currentPage",
				page
		);

		model.addAttribute(
				"totalPages",
				coachPage.getTotalPages()
		);

		return "coaches";
	}

	@GetMapping("/coaches/new")
	public String createCoachPage(Model model) {

		model.addAttribute(
				"coach",
				new CoachDto()
		);

		return "coach-create";
	}

	@GetMapping("/coaches/edit/{id}")
	public String editCoachPage(
			@PathVariable Long id,
			Model model) {

		model.addAttribute(
				"coach",
				coachMapper.toDto(
						coachService.getById(id))
		);

		return "coach-edit";
	}

	@PostMapping("/coaches")
	public String createCoach(
			@Valid
			@ModelAttribute("coach") CoachDto coachDto,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			return "coach-create";
		}

		coachService.create(
				coachMapper.toEntity(coachDto)
		);

		return "redirect:/coaches";
	}

	@PostMapping("/coaches/update/{id}")
	public String updateCoach(
			@PathVariable Long id,
			@Valid
			@ModelAttribute("coach") CoachDto coachDto,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			return "coach-edit";
		}

		coachDto.setId(id);

		coachService.update(
				coachMapper.toEntity(coachDto)
		);

		return "redirect:/coaches";
	}

	@PostMapping("/coaches/delete/{id}")
	public String deleteCoach(
			@PathVariable Long id) {

		coachService.delete(id);

		return "redirect:/coaches";
	}
}
