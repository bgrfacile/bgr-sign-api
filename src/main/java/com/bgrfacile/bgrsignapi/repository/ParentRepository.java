package com.bgrfacile.bgrsignapi.repository;

import com.bgrfacile.bgrsignapi.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    // L'id correspond à user_id
}
