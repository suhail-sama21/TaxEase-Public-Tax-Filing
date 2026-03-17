package com.cognizant.taxease.dao;

import com.cognizant.taxease.entity.RevenueRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevenueRecordRepository extends JpaRepository<RevenueRecord, Long> {
}