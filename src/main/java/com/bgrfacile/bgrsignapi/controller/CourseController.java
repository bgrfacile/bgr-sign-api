package com.bgrfacile.bgrsignapi.controller;

import com.bgrfacile.bgrsignapi.dto.response.StudentResponse;
import com.bgrfacile.bgrsignapi.model.Course;
import com.bgrfacile.bgrsignapi.service.AttendanceService;
import com.bgrfacile.bgrsignapi.service.CourseService;
import com.bgrfacile.bgrsignapi.service.QRCodeService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private QRCodeService qrCodeService;


    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/attendance-info")
    public ResponseEntity<Map<String, Object>> getAttendanceInfo(@PathVariable Long id) {

        try {
            // URL pour la signature de présence
            String attendanceUrl = "https://yourdomain.com/attendance/sign?courseId=" + id;
            // Génération du QR Code en Base64
            String qrCodeBase64 = qrCodeService.generateQRCodeBase64(attendanceUrl, 250, 250);
            // Récupération des étudiants inscrits au cours
            List<StudentResponse> students = courseService.getStudentsByCourseId(id)
                    .stream()
                    .map(StudentResponse::new)
                    .collect(Collectors.toList());

            // Création de la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("qrCode", "data:image/png;base64," + qrCodeBase64);
            response.put("scanUrl", attendanceUrl);
            response.put("students", students);

            return ResponseEntity.ok(response);
        } catch (WriterException | IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
