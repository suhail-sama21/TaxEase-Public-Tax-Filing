package com.cognizant.taxease.repository;

import com.cognizant.taxease.entity.TaxFiling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxFilingRepository extends JpaRepository<TaxFiling, Long> {
}