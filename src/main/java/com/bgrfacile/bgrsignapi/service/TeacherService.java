package com.bgrfacile.bgrsignapi.service;

import com.bgrfacile.bgrsignapi.dto.TeacherSessionDTO;
import com.bgrfacile.bgrsignapi.model.Course;
import com.bgrfacile.bgrsignapi.repository.AttendanceRepository;
import com.bgrfacile.bgrsignapi.repository.CourseRepository;
import com.bgrfacile.bgrsignapi.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {
    @Autowired
    private final CourseRepository courseRepository;
    @Autowired
    private final StudentRepository studentRepository;
    @Autowired
    private final AttendanceRepository attendanceRepository;

    public List<TeacherSessionDTO> getTodaySessions(Long teacherId) {
        LocalDate today = LocalDate.now();
        List<Course> courses = courseRepository.findByTeacherIdAndScheduleDate(teacherId, today);

        return courses.stream()
                .map(course -> {
                    int total = studentRepository.countByClassId(course.getSchoolClass().getId());
                    int present = attendanceRepository.countByCourseAndDateAndStatus(
                            course.getId(), today, "present");

                    return TeacherSessionDTO.builder()
                            .time(formatTime(course.getSchedule()))
                            .subjectName(course.getSubject().getSubjectName())
                            .presentCount(present)
                            .totalStudents(total)
                            .build();
                }).toList();
    }

    private String formatTime(LocalDateTime localDateTime) {
        LocalTime time = localDateTime.toLocalTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        return time.format(formatter);
    }
}
