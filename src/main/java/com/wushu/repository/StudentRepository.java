package com.wushu.repository;

import com.wushu.entity.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository
		extends JpaRepository<Student, Long> {

	@Query("""
select s 
from Student s
where s.id not in (
select st.id 
from GroupTraining g
join g.students st
where g.id = :groupId
)
""")
	List<Student> findStudentsNotInGroup(Long groupId);

	@EntityGraph(attributePaths = "groups")
	Optional<Student> findWithGroupsById(Long id);
}
