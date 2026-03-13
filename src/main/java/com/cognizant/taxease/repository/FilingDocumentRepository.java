package com.cognizant.taxease.repository;

import com.cognizant.taxease.entity.FilingDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilingDocumentRepository extends JpaRepository<FilingDocument, Long> {
}