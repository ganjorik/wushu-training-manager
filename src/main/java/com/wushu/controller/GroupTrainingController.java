package com.wushu.controller;

import com.wushu.dto.GroupTrainingDto;
import com.wushu.entity.GroupTraining;
import com.wushu.mapper.CoachMapper;
import com.wushu.mapper.GroupTrainingMapper;
import com.wushu.service.CoachService;
import com.wushu.service.GroupTrainingService;
import com.wushu.service.StudentService;
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
@RequestMapping("/groups")
public class GroupTrainingController {

	private final GroupTrainingService groupService;
	private final CoachService coachService;
	private final StudentService studentService;

	private final GroupTrainingMapper groupMapper;
	private final CoachMapper coachMapper;

	@GetMapping
	public String groups(
			@RequestParam(defaultValue = "0") int page,
			Model model) {

		Page<GroupTraining> groupPage =
				groupService.getAll(
						PageRequest.of(page, 5)
				);

		model.addAttribute(
				"groups",
				groupMapper.toDtoList(
						groupPage.getContent()
				)
		);

		model.addAttribute(
				"currentPage",
				page
		);

		model.addAttribute(
				"totalPages",
				groupPage.getTotalPages()
		);

		return "groups";
	}

	@GetMapping("/new")
	public String createPage(Model model) {

		model.addAttribute(
				"group",
				new GroupTrainingDto()
		);

		model.addAttribute(
				"coaches",
				coachMapper.toDtoList(
						coachService.getAll()
				)
		);

		return "group-create";
	}

	@PostMapping
	public String createGroup(
			@Valid
			@ModelAttribute("group") GroupTrainingDto groupDto,
			BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {

			model.addAttribute(
					"coaches",
					coachMapper.toDtoList(
							coachService.getAll()
					)
			);

			return "group-create";
		}

		groupService.create(
				groupMapper.toEntity(groupDto));

		return "redirect:/groups";
	}

	@GetMapping("/edit/{id}")
	public String editPage(
			@PathVariable Long id,
			Model model) {

		model.addAttribute(
				"group",
				groupMapper.toDto(
						groupService.getById(id)
				)
		);

		model.addAttribute(
				"coaches",
				coachMapper.toDtoList(
						coachService.getAll()
				)
		);

		return "group-edit";
	}

	@PostMapping("/update/{id}")
	public String updateGroup(
			@PathVariable Long id,
			@Valid
			@ModelAttribute("group") GroupTrainingDto groupDto,
			BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {

			model.addAttribute(
					"coaches",
					coachMapper.toDtoList(
							coachService.getAll()
					)
			);

			return "group-edit";
		}

		groupDto.setId(id);

		groupService.update(
				groupMapper.toEntity(groupDto));

		return "redirect:/groups";
	}

	@PostMapping("/delete/{id}")
	public String deleteGroup(
			@PathVariable Long id) {

		groupService.delete(id);

		return "redirect:/groups";
	}

	@GetMapping("/{id}")
	public String viewGroup(
			@PathVariable Long id,
			Model model) {

		model.addAttribute(
				"group",
				groupService.getByIdWithStudents(id)
		);

		model.addAttribute(
				"students",
				studentService.getStudentsNotInGroup(id)
		);

		return "group-view";
	}

	@PostMapping("/{groupId}/students")
	public String addStudent(
			@PathVariable Long groupId,
			@RequestParam Long studentId) {

		groupService.addStudent(
				groupId,
				studentId
		);

		return "redirect:/groups/" + groupId;
	}

	@PostMapping("/{groupId}/students/{studentId}/delete")
	public String removeStudent(
			@PathVariable Long groupId,
			@PathVariable Long studentId) {

		groupService.removeStudent(
				groupId,
				studentId
		);

		return "redirect:/groups/" + groupId;
	}
}