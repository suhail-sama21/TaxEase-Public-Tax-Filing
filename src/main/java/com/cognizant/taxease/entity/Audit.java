package com.cognizant.taxease.entity;

import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int AuditId;
    //relationship
    @JoinColumn(name = "OfficerId", nullable = false)
    private User user;
    private String Scope;
    private String Findings;
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Instant TimeStamp;
    private StatusBasic Status;

}
