package com.bgrfacile.bgrsignapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "school_classes")
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String className;
    private String academicYear;

    @OneToMany(mappedBy = "schoolClass")
    private List<Student> students;
}
