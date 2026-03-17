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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.batch.jdbc.initialize-database=never",
    "spring.batch.job.enabled=false"
})
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
