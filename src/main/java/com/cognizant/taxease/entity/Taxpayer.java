package com.cognizant.taxease.entity;

import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import com.cognizant.taxease.entity.entityEnum.TaxpayerType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Taxpayer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Taxpayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TaxpayerID")
    private Integer taxpayerId;

    @Column(name = "Name", nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private TaxpayerType type;

    @Column(name = "Address", columnDefinition = "text")
    private String address;

    @Column(name = "ContactInfo", columnDefinition = "text")
    private String contactInfo;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "Status", nullable = false)
//    private StatusBasic status = StatusBasic.Active;

    // FK fields to User (optional)
//    @Column(name = "CreatedBy")
//    private Integer createdBy;
//
//    @Column(name = "UpdatedBy")
//    private Integer updatedBy;

    // --- Auto timestamps (Hibernate) ---
    @CreationTimestamp
    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UpdatedAt", nullable = false)
    private LocalDateTime updatedAt;
}
