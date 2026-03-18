package com.cognizant.taxease.dao;

import com.cognizant.taxease.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Fulfills TAXFR-11: Get payments for a specific taxpayer
    List<Payment> findByFiling_Taxpayer_Id(Long taxpayerId);
}