package com.wushu.service;

import com.wushu.entity.Coach;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CoachService {

	List<Coach> getAll();

	Page<Coach> getAll(Pageable pageable);

	Coach getById(Long id);

	Coach create(Coach coach);

	Coach update(Coach coach);

	void delete(Long id);
}