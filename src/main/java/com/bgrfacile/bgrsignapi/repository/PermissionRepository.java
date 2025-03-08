package com.bgrfacile.bgrsignapi.repository;

import com.bgrfacile.bgrsignapi.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
