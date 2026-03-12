package com.cognizant.taxease.entity;

import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "RevenueRecord",
        indexes = {
                @Index(name = "idx_revenue_taxpayer_date", columnList = "TaxpayerID, Date")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RevenueID")
    private Integer revenueId;

//    // FK -> Payment.PaymentID (unique: one revenue per payment)
//    @Column(name = "PaymentID", nullable = false, unique = true)
//    private Integer paymentId;

    // FK -> Taxpayer.TaxpayerID
    @Column(name = "TaxpayerID", nullable = false)
    private Integer taxpayerId;

    @Column(name = "Amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    // Recorded timestamp; auto-set on insert
    @CreationTimestamp
    @Column(name = "Date", nullable = false, updatable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private StatusBasic status = StatusBasic.Completed;
}
