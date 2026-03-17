package com.cognizant.taxease.repository;

import com.cognizant.taxease.entity.ComplianceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplianceRecordRepository extends JpaRepository<ComplianceRecord, Long> {
}