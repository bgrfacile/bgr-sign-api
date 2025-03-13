package com.bgrfacile.bgrsignapi.service;

import com.bgrfacile.bgrsignapi.exception.ResourceNotFoundException;
import com.bgrfacile.bgrsignapi.model.Subject;
import com.bgrfacile.bgrsignapi.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
    }

    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Subject updateSubject(Long id, Subject subjectDetails) {
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        subject.setSubjectName(subjectDetails.getSubjectName());
        subject.setDescription(subjectDetails.getDescription());
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }
}
