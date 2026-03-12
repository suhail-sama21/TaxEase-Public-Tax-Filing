package com.cognizant.taxease.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int AuditLogId;

    @ManyToOne
    @JoinColumn()
    private User UserId;

    @ManyToOne
    @JoinColumn()
    private Audit auditId;

    @Column(nullable = false)
    private String Action;

    @Column(nullable = false)
    private String Resource;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant TimeStamp;

}
