package com.bgrfacile.bgrsignapi.service;

import com.bgrfacile.bgrsignapi.dto.CreateAttendanceDTO;
import com.bgrfacile.bgrsignapi.dto.TodaysClassDTO;
import com.bgrfacile.bgrsignapi.model.Attendance;
import com.bgrfacile.bgrsignapi.model.Course;
import com.bgrfacile.bgrsignapi.model.Student;
import com.bgrfacile.bgrsignapi.model.Teacher;
import com.bgrfacile.bgrsignapi.repository.AttendanceRepository;
import com.bgrfacile.bgrsignapi.repository.CourseRepository;
import com.bgrfacile.bgrsignapi.repository.StudentRepository;
import com.bgrfacile.bgrsignapi.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    private static final int COURSE_DURATION_MINUTES = 90;

/*    public Attendance markAttendance(Long studentId, Long courseId, LocalDate date, AttendanceStatus status, Long teacherId) {
        // Vérification de l'existence de l'étudiant et du cours
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setDate(date);
        attendance.setStatus(status);

        // Vous pouvez rechercher le teacher via son repository et l'attribuer ici
        // attendance.setRecordedBy(teacher);

        return attendanceRepository.save(attendance);
    }*/

/*    public List<Attendance> getAttendancesForStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }*/

    public Map<String, List<TodaysClassDTO>> getTodaysAttendances() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<Course> coursesToday = courseRepository.findCoursesBetween(startOfDay, endOfDay);
        List<TodaysClassDTO> todaysClasses = new ArrayList<>();

        for (Course course : coursesToday) {
            // Comptage des présences pour le cours et la date d'aujourd'hui
            int presentStudents = attendanceRepository.countPresentByCourseAndDate(course.getId(), today);

            // Comptage du nombre total d'étudiants dans la classe associée au cours.
            // Ici, on suppose que l’entité Course possède une relation avec l’entité Classe (nommée ici "clazz")
            int totalStudents = 0;
            if (course.getSchoolClass() != null) {
                totalStudents = studentRepository.countBySchoolClass_Id(course.getSchoolClass().getId());
            }

            // Calcul de l'heure de fin basée sur une durée fixe
            LocalDateTime startTime = course.getSchedule();
            LocalDateTime endTime = startTime.plusMinutes(COURSE_DURATION_MINUTES);

            TodaysClassDTO dto = new TodaysClassDTO(
                    course.getId(),
                    course.getSubject().getSubjectName(),
                    startTime,
                    endTime,
                    presentStudents,
                    totalStudents
            );
            todaysClasses.add(dto);
        }

        Map<String, List<TodaysClassDTO>> response = new HashMap<>();
        response.put("todaysClasses", todaysClasses);
        return response;
    }

    public Attendance markAttendance(CreateAttendanceDTO attendanceDTO) {
        // Récupérer l'étudiant
        Student student = studentRepository.findById(attendanceDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        // Récupérer le cours
        Course course = courseRepository.findById(attendanceDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setDate(attendanceDTO.getDate());
        attendance.setStatus(attendanceDTO.getStatus());

        // Optionnel : récupérer l'enseignant si teacherId est fourni
        if (attendanceDTO.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(attendanceDTO.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));
            attendance.setRecordedBy(teacher);
        }

        return attendanceRepository.save(attendance);
    }
}
