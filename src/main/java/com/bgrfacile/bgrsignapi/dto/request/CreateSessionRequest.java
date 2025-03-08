package com.bgrfacile.bgrsignapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateSessionRequest {
    @NotNull(message = "L'ID du professeur est requis")
    private Long teacherId;

    @NotNull(message = "L'ID de la matière est requis")
    private Long subjectId;

    @NotNull(message = "L'ID de la classe est requis")
    private Long classId;

    @NotNull(message = "La date et l'heure sont requis")
    private LocalDateTime schedule;
}
