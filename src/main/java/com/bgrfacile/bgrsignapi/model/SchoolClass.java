package com.bgrfacile.bgrsignapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "classes")  // TODO refactor pour appelle la table school_class
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String className;
    private String academicYear;
}
