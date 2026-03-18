package com.cognizant.taxease.dao;

import com.cognizant.taxease.entity.RevenueRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevenueRecordRepository extends JpaRepository<RevenueRecord, Long> {
}