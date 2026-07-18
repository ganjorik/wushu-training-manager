package com.wushu.repository;

import com.wushu.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository
		extends JpaRepository<Exercise, Long> {
}
