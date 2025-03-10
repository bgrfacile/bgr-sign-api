package com.bgrfacile.bgrsignapi.service;

import com.bgrfacile.bgrsignapi.exception.ResourceNotFoundException;
import com.bgrfacile.bgrsignapi.model.SchoolClass;
import com.bgrfacile.bgrsignapi.repository.SchoolClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolClassService {
    @Autowired
    private SchoolClassRepository schoolClassRepository;

    public List<SchoolClass> getAllClasses() {
        return schoolClassRepository.findAll();
    }

    public SchoolClass getClassById(Long id) {
        return schoolClassRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Class not found"));
    }

    public SchoolClass createClass(SchoolClass schoolClass) {
        return schoolClassRepository.save(schoolClass);
    }

    public SchoolClass updateClass(Long id, SchoolClass classDetails) {
        SchoolClass schoolClass = schoolClassRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        schoolClass.setClassName(classDetails.getClassName());
        schoolClass.setAcademicYear(classDetails.getAcademicYear());
        return schoolClassRepository.save(schoolClass);
    }

    public void deleteClass(Long id) {
        schoolClassRepository.deleteById(id);
    }
}
