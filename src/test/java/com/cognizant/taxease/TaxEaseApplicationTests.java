package com.cognizant.taxease;

import com.cognizant.taxease.dao.TaxFilingRepository;
import com.cognizant.taxease.dao.TaxpayerRepository;
import com.cognizant.taxease.dao.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
