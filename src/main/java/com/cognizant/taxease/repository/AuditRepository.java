package com.cognizant.taxease.repository;

import com.cognizant.taxease.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Long> {
}