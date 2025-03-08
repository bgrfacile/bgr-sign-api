package com.bgrfacile.bgrsignapi.repository;

import com.bgrfacile.bgrsignapi.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findBySubjectName(String subjectName);
}
