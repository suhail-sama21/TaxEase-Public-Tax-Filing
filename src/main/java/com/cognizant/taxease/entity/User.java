package com.cognizant.taxease.entity;


import com.cognizant.taxease.entity.entityEnum.UserRole;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer UserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole Role;

    @Column(nullable = false)
    private String Name;

    @Column(unique = true,nullable = false)
    private String Email;

    private String Phone;

    @Column(nullable = false)
    private String PasswordHash;

    @CreationTimestamp
    private LocalDate PasswordChangedAt;

    @UpdateTimestamp
    private LocalDate CreatedAt;

    @UpdateTimestamp
    private LocalDate UpdateAt;


}
