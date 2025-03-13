package com.bgrfacile.bgrsignapi.repository;

import com.bgrfacile.bgrsignapi.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // L'id correspond à user_id
}
