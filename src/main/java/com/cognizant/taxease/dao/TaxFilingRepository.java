package com.cognizant.taxease.dao;

import com.cognizant.taxease.entity.TaxFiling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxFilingRepository extends JpaRepository<TaxFiling, Long> {
}