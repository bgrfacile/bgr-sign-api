package com.bgrfacile.bgrsignapi.repository;

import com.bgrfacile.bgrsignapi.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
