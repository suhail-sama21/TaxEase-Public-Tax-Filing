package com.cognizant.taxease.dao;

import com.cognizant.taxease.entity.Taxpayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxpayerRepository extends JpaRepository<Taxpayer, Long> {
}