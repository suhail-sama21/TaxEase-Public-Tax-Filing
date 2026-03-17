package com.cognizant.taxease.repository;

import com.cognizant.taxease.entity.TaxpayerDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxpayerDocumentRepository extends JpaRepository<TaxpayerDocument, Long> {
}