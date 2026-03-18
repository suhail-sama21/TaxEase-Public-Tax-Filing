package com.cognizant.taxease.repository;

import com.cognizant.taxease.entity.Taxpayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxpayerRepositorygiri extends JpaRepository<Taxpayer, Long> {
    boolean existsByTaxpayerIdNumber(String taxpayerIdNumber);
    Optional<Taxpayer> findByTaxpayerIdNumber(String taxpayerIdNumber);
}