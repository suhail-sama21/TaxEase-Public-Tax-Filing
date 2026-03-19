package com.cognizant.taxease.dao;

import com.cognizant.taxease.entity.Audit;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditRepository extends JpaRepository<Audit, Long> {
    long countByStatus(StatusBasic status);
    List<Audit> findByStatus(StatusBasic status);
}