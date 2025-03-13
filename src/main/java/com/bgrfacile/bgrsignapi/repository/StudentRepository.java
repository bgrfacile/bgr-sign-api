package com.bgrfacile.bgrsignapi.repository;

import com.bgrfacile.bgrsignapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    int countBySchoolClass_Id(Long classId);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.schoolClass.id = :classId")
    int countByClassId(@Param("classId") Long classId);
}
