package com.bgrfacile.bgrsignapi.dto;

import com.bgrfacile.bgrsignapi.model.enums.AttendanceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAttendanceDTO {
    @Schema(description = "ID de l'étudiant", example = "1")
    private Long studentId;

    @Schema(description = "ID du cours", example = "1")
    private Long courseId;

    @Schema(description = "Date de la signature", example = "2023-03-10")
    private LocalDate date;
    private AttendanceStatus status;
    private Long teacherId;

}
