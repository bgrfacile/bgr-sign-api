package com.bgrfacile.bgrsignapi.service;

import com.bgrfacile.bgrsignapi.dto.request.CreateCourseRequest;
import com.bgrfacile.bgrsignapi.exception.ResourceNotFoundException;
import com.bgrfacile.bgrsignapi.exception.SessionConflictException;
import com.bgrfacile.bgrsignapi.model.Course;
import com.bgrfacile.bgrsignapi.model.SchoolClass;
import com.bgrfacile.bgrsignapi.model.Student;
import com.bgrfacile.bgrsignapi.repository.CourseRepository;
import com.bgrfacile.bgrsignapi.repository.SchoolClassRepository;
import com.bgrfacile.bgrsignapi.repository.SubjectRepository;
import com.bgrfacile.bgrsignapi.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    public Course createSession(CreateCourseRequest request) {
        boolean sessionExists = courseRepository.existsByTeacherAndSubjectAndClassAndSchedule(
                request.getTeacherId(),
                request.getSubjectId(),
                request.getClassId(),
                request.getSchedule()
        );

        if (sessionExists) {
            throw new SessionConflictException("Une session avec cette combinaison professeur/matière/classe/horaire existe déjà.");
        }

        // Vérifier que le professeur, la matière et la classe existent
        var teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Professeur non trouvé"));
        var subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
        var schoolClass = schoolClassRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Classe non trouvée"));

        // Créer une nouvelle session
        Course course = new Course();
        course.setTeacher(teacher);
        course.setSubject(subject);
        course.setSchoolClass(schoolClass);
        course.setSchedule(request.getSchedule());

        // Sauvegarder la session
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course courseDetails) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        course.setTeacher(courseDetails.getTeacher());
        course.setSubject(courseDetails.getSubject());
        course.setSchoolClass(courseDetails.getSchoolClass());
        course.setSchedule(courseDetails.getSchedule());
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public List<Student> getStudentsByCourseId(Long courseId) {
        return courseRepository.findById(courseId)
                .map(Course::getSchoolClass) // Récupérer la classe liée au cours
                .map(SchoolClass::getStudents) // Récupérer les étudiants de cette classe
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

}
