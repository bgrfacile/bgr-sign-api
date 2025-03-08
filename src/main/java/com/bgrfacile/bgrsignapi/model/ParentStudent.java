package com.bgrfacile.bgrsignapi.model;

import com.bgrfacile.bgrsignapi.dto.ParentStudentId;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "parent_student")
@Data
public class ParentStudent {
    @EmbeddedId
    private ParentStudentId id;

    @ManyToOne
    @MapsId("parentId")
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "relationship_type", length = 20)
    private String relationshipType;
}
