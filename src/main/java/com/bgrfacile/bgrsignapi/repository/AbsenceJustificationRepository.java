package com.bgrfacile.bgrsignapi.repository;

import com.bgrfacile.bgrsignapi.model.AbsenceJustification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceJustificationRepository extends JpaRepository<AbsenceJustification, Long> {
    // Ajoutez des méthodes spécifiques si besoin
}
