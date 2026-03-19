package com.cognizant.taxease.dao;

import com.cognizant.taxease.entity.Taxpayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxpayerRepository extends JpaRepository<Taxpayer, Long> {
    // Spring Data JPA provides all the standard CRUD operations automatically.
    // You can add custom queries here later, for example:
    // Optional<Taxpayer> findByContactInfo(String contactInfo);
}