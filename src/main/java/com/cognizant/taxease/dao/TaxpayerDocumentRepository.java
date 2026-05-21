package com.cognizant.taxease.dao;

import com.cognizant.taxease.entity.Taxpayer;
import com.cognizant.taxease.entity.TaxpayerDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxpayerDocumentRepository extends JpaRepository<TaxpayerDocument, Long> {
    List<TaxpayerDocument> findByTaxpayer(Taxpayer taxpayer);
}