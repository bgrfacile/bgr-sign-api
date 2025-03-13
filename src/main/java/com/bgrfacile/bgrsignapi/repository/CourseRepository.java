package com.bgrfacile.bgrsignapi.repository;

import com.bgrfacile.bgrsignapi.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE c.schedule BETWEEN :startOfDay AND :endOfDay")
    List<Course> findCoursesBetween(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);


    @Query("SELECT c FROM Course c " +
            "WHERE c.teacher.userId = :teacherId " +
            "AND DATE(c.schedule) = :scheduleDate")
    List<Course> findByTeacherIdAndScheduleDate(
            @Param("teacherId") Long teacherId,
            @Param("scheduleDate") LocalDate scheduleDate
    );

    @Query("SELECT COUNT(c) > 0 FROM Course c " +
            "WHERE c.teacher.userId = :teacherId " +
            "AND c.subject.id = :subjectId " +
            "AND c.schoolClass.id = :classId " +
            "AND c.schedule = :schedule")
    boolean existsByTeacherAndSubjectAndClassAndSchedule(
            @Param("teacherId") Long teacherId,
            @Param("subjectId") Long subjectId,
            @Param("classId") Long classId,
            @Param("schedule") LocalDateTime schedule
    );
}
