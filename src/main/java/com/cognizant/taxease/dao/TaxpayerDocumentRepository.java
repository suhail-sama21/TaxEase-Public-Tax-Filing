package com.cognizant.taxease.dao;

import com.cognizant.taxease.entity.TaxpayerDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxpayerDocumentRepository extends JpaRepository<TaxpayerDocument, Long> {
}