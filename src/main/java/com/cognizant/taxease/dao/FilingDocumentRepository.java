package com.cognizant.taxease.dao;

import com.cognizant.taxease.entity.FilingDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilingDocumentRepository extends JpaRepository<FilingDocument, Long> {
}