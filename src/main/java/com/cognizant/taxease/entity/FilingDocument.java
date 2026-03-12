package com.cognizant.taxease.entity;

import ch.qos.logback.classic.pattern.LineOfCallerConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
public class FilingDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int DocumentId;

    @ManyToOne
    @JoinColumn()
    private TaxFiling FilingId;

    //DocType

    private String FileUrl;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant UploadedDate;

    //verification enum


    private Instant CreatedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant UpdatedAt;
}
