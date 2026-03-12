package com.cognizant.taxease.entity;

import com.cognizant.taxease.entity.entityEnum.ComplianceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ComplianceRecord")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplianceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ComplianceID")
    private Integer complianceId;

    // FK -> Taxpayer.TaxpayerID (required)
    @Column(name = "TaxpayerID", nullable = false)
    private Integer taxpayerId;

    // FK -> TaxFiling.FilingID (optional; required when Type = Filing)
    @Column(name = "FilingID")
    private Integer filingId;

    // ✅ NEW: FK -> Payment.PaymentID (optional; required when Type = Payment)
    @Column(name = "PaymentID")
    private Integer paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private ComplianceType type; // Filing or Payment

    @Column(name = "Result", nullable = false, length = 100)
    private String result;

    @Column(name = "Date")
    private LocalDate date; // DEFAULT can be set at DB level if needed

    @Column(name = "Notes", columnDefinition = "text")
    private String notes;

    // Who created this compliance record (FK -> User.UserID)
    @Column(name = "ComplianceOfficer")
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // --- Convenience helpers (optional) ---

    /**
     * Ensure only one of filingId/paymentId is set according to Type.
     * Call from service layer before save, or add a Bean Validation group if you prefer.
     */
    @PrePersist
    @PreUpdate
    private void validateTarget() {
        if (type == ComplianceType.Filing) {
            if (filingId == null || paymentId != null) {
                throw new IllegalStateException("For Type=Filing, FilingID must be set and PaymentID must be null.");
            }
        } else if (type == ComplianceType.Payment) {
            if (paymentId == null || filingId != null) {
                throw new IllegalStateException("For Type=Payment, PaymentID must be set and FilingID must be null.");
            }
        }
        // if date not provided, set today's date
        if (date == null) {
            date = LocalDate.now();
        }
    }
}
