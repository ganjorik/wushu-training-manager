package com.wushu.service;

import com.wushu.entity.TrainingSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TrainingSessionService {

	List<TrainingSession> getAll();

	Page<TrainingSession> getAll(Pageable pageable);

	TrainingSession getById(Long id);

	TrainingSession create(TrainingSession training);

	TrainingSession update(TrainingSession training);

	void delete(Long id);

	TrainingSession getByIdWithRelations(Long id);
}