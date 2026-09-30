package com.wushu.controller;

import com.wushu.dto.StudentDto;
import com.wushu.mapper.StudentMapper;
import com.wushu.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentRestController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    @GetMapping
    public List<StudentDto> getAllStudents(){

        return studentMapper.toDtoList(
                studentService.getAll()
        );
    }

    @GetMapping("/{id}")
    public StudentDto getStudentById(
            @PathVariable Long id){

        return studentMapper.toDto(
                studentService.getById(id)
        );
    }
}
