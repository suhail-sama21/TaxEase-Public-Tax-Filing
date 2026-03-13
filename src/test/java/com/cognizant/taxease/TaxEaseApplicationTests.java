package com.cognizant.taxease;

import com.cognizant.taxease.entity.TaxFiling;
import com.cognizant.taxease.entity.Taxpayer;
import com.cognizant.taxease.entity.User;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import com.cognizant.taxease.entity.entityEnum.TaxpayerType;
import com.cognizant.taxease.entity.entityEnum.UserRole;
import com.cognizant.taxease.repository.AuditRepository;
import com.cognizant.taxease.repository.TaxFilingRepository;
import com.cognizant.taxease.repository.TaxpayerRepository;
import com.cognizant.taxease.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class TaxEaseApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaxpayerRepository taxpayerRepository;

    @Autowired
    private TaxFilingRepository taxFilingRepository;

    @Test
    void contextLoads() {
    }



}
