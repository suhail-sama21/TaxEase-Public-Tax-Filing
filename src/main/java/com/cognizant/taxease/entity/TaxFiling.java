package com.cognizant.taxease.entity;

import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TaxFiling")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxFiling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FilingID")
    private Integer filingId;

    @Column(name = "TaxpayerID", nullable = false)
    private Integer taxpayerId;

    @Column(name = "Period", nullable = false, length = 20)
    private String period; // e.g., FY2025-26 or 2026Q1

    @Column(name = "AmountDeclared", nullable = false, precision = 14, scale = 2)
    private BigDecimal amountDeclared;

//    @Column(name = "SubmittedDate")
//    private LocalDateTime submittedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private StatusBasic status = StatusBasic.Pending;

    // NEW: Officer who handled/approved/reviewed this filing
    @Column(name = "OfficerID")
    private Integer officerId; // FK -> User.UserID (role should be Officer)
//
//    @Column(name = "CreatedBy")
//    private Integer createdBy;
//
//    @Column(name = "UpdatedBy")
//    private Integer updatedBy;

    @CreationTimestamp
    @Column(name = "SubmittedDate", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UpdatedAt", nullable = false)
    private LocalDateTime updatedAt;
}