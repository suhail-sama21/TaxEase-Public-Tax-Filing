package com.cognizant.taxease.dao;

import com.cognizant.taxease.entity.Payment;
import com.cognizant.taxease.entity.entityEnum.PaymentMethod;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    long countByStatus(StatusBasic status);
    long countByStatusAndMethod(StatusBasic status, PaymentMethod method);
    long countByMethod(PaymentMethod method);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'Pending'")
    BigDecimal sumOutstandingPayments();
}