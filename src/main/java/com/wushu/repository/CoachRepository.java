package com.wushu.repository;

import com.wushu.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoachRepository
		extends JpaRepository<Coach, Long> {

	@Query("""
       select
           count(g) > 0
       from GroupTraining g
       where g.coach.id = :coachId
       """)
	boolean hasGroups(@Param("coachId") Long coachId);
}
