package com.cognizant.taxease.dao;

import com.cognizant.taxease.entity.FilingDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilingDocumentRepository extends JpaRepository<FilingDocument, Long> {
    List<FilingDocument> findByFilingId(Long filingId);
}