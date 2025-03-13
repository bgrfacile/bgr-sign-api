package com.bgrfacile.bgrsignapi.repository;

import com.bgrfacile.bgrsignapi.model.AttendanceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendanceRuleRepository extends JpaRepository<AttendanceRule, Long> {
    Optional<AttendanceRule> findByRuleName(String ruleName);
}
