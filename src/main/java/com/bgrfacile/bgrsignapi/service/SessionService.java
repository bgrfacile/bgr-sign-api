package com.bgrfacile.bgrsignapi.service;

import com.bgrfacile.bgrsignapi.dto.request.CreateSessionRequest;
import com.bgrfacile.bgrsignapi.exception.SessionConflictException;
import com.bgrfacile.bgrsignapi.model.Course;
import com.bgrfacile.bgrsignapi.repository.CourseRepository;
import com.bgrfacile.bgrsignapi.repository.SchoolClassRepository;
import com.bgrfacile.bgrsignapi.repository.SubjectRepository;
import com.bgrfacile.bgrsignapi.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    public Course createSession(CreateSessionRequest request) {
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
}
