package com.cognizant.taxease.entity;

import com.cognizant.taxease.entity.entityEnum.DocTypeTaxpayer;
import com.cognizant.taxease.entity.entityEnum.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "TaxpayerDocument")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxpayerDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DocumentID")
    private Integer documentId;

    // FK to Taxpayer.TaxpayerID (kept as Integer like your other FKs)
    @Column(name = "TaxpayerID", nullable = false)
    private Integer taxpayerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "DocType", nullable = false)
    private DocTypeTaxpayer docType;

    @Column(name = "FileURI", nullable = false, columnDefinition = "text")
    private String fileUri;

    // When the file was uploaded (can be set by app; no default here)
//    @Column(name = "UploadedDate")
//    private LocalDateTime uploadedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "VerificationStatus", nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.Pending;

//    // Optional creator/updater references to User.UserID
//    @Column(name = "CreatedBy")
//    private Integer createdBy;
//
//    @Column(name = "UpdatedBy")
//    private Integer updatedBy;

    // Auto-maintained timestamps
    @CreationTimestamp
    @Column(name = "UploadedDate", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UpdatedAt", nullable = false)
    private LocalDateTime updatedAt;
}