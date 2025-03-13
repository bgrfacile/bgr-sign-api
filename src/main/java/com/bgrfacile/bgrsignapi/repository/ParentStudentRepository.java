package com.bgrfacile.bgrsignapi.repository;

import com.bgrfacile.bgrsignapi.dto.ParentStudentId;
import com.bgrfacile.bgrsignapi.model.ParentStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentStudentRepository extends JpaRepository<ParentStudent, ParentStudentId> {
    // Ajoutez des méthodes de recherche spécifiques si nécessaire
}
