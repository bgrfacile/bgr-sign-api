package com.bgrfacile.bgrsignapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            length = 60,
            unique = true,
            nullable = false)
    private String name;
}
