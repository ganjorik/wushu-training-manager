package com.wushu.controller;

import com.wushu.dto.StudentDto;
import com.wushu.entity.Student;
import com.wushu.mapper.StudentMapper;
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
public class StudentController {

	private final StudentService studentService;
	private final StudentMapper studentMapper;

	@GetMapping("/students")
	public String students(
			@RequestParam(defaultValue = "0") int page,
			Model model) {

		Page<Student> studentPage =
				studentService.getAll(
						PageRequest.of(page, 5)
				);

		model.addAttribute(
				"students",
				studentMapper.toDtoList(
						studentPage.getContent()
				)
		);

		model.addAttribute(
				"currentPage",
				page
		);

		model.addAttribute(
				"totalPages",
				studentPage.getTotalPages()
		);

		return "students";
	}

	@GetMapping("/students/new")
	public String createStudentPage(Model model) {

		model.addAttribute(
				"student",
				new StudentDto());

		return "student-create";
	}

	@PostMapping("/students")
	public String createStudent(
			@Valid
			@ModelAttribute("student") StudentDto studentDto,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			return "student-create";
		}

		studentService.create(studentMapper.toEntity(studentDto));

		return "redirect:/students";
	}

	@GetMapping("/students/edit/{id}")
	public String editStudentPage(
			@PathVariable Long id,
			Model model) {

		model.addAttribute(
				"student",
				studentMapper.toDto(studentService.getById(id))
		);

		return "student-edit";
	}

	@PostMapping("/students/update/{id}")
	public String updateStudent(
			@PathVariable Long id,
			@Valid
			@ModelAttribute("student") StudentDto studentDto,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			return "student-edit";
		}

		studentDto.setId(id);

		studentService.update(studentMapper.toEntity(studentDto));

		return "redirect:/students";
	}

	@PostMapping("/students/delete/{id}")
	public String deleteStudent(
			@PathVariable Long id) {

		studentService.delete(id);

		return "redirect:/students";
	}
}
