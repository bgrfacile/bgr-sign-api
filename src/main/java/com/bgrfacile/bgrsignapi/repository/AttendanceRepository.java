package com.bgrfacile.bgrsignapi.repository;

import com.bgrfacile.bgrsignapi.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentUserId(Long userId);

    List<Attendance> findByCourseId(Long courseId);

    @Query("""
            SELECT COUNT(a) FROM Attendance a 
            WHERE a.course.id = :courseId 
            AND a.date = :date 
            AND a.status = 'present'
            """)
    int countPresentByCourseAndDate(@Param("courseId") Long courseId, @Param("date") LocalDate date);

    @Query("SELECT COUNT(a) FROM Attendance a " +
            "WHERE a.course.id = :courseId " +
            "AND a.date = :date " +
            "AND a.status = 'present'")
    int countByCourseAndDateAndStatus(
            @Param("courseId") Long courseId,
            @Param("date") LocalDate date,
            @Param("status") String status);
}

