package com.cognizant.taxease.config;

import com.cognizant.taxease.entity.Taxpayer;
import com.cognizant.taxease.entity.User; // Import your User entity
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import com.cognizant.taxease.entity.entityEnum.TaxpayerType;
import com.cognizant.taxease.dao.TaxpayerRepository;
import com.cognizant.taxease.dao.UserRepository; // You need this to save the user

import com.cognizant.taxease.entity.entityEnum.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, TaxpayerRepository taxpayerRepository) {
        return args -> {
            // Only insert if the table is empty
            if (taxpayerRepository.count() == 0) {

                // 1. MUST create a User first because Taxpayer requires it
                User dummyUser = User.builder()
                        .name("John Doe")
                        .email("johndoe@example.com")
                        .phone("1234567890")
                        .passwordHash("dummy_password_123")
                        .role(UserRole.Taxpayer) // Add this! (Check your exact enum name for Role)
                        .status(StatusBasic.Active)// If your User entity requires a Role or Status enum, add them here!
                        .build();

                // Save the user to the database to generate its ID
                userRepository.save(dummyUser);

                // 2. Create the Taxpayer (without status) and attach the saved User
                Taxpayer dummyTaxpayer = Taxpayer.builder()
                        .user(dummyUser) // This links the mandatory 1:1 relationship
                        .name("John Doe")
                        .type(TaxpayerType.Citizen) // Make sure 'Citizen' matches your enum exactly
                        .address("123 Tech Park, Bengaluru")
                        .contactInfo("johndoe@example.com")
                        // Status is completely removed!
                        .build();

                // Save the taxpayer
                taxpayerRepository.save(dummyTaxpayer);

                System.out.println("✅ Dummy User and Taxpayer injected successfully! Taxpayer ID: " + dummyTaxpayer.getId());
            }
        };
    }
}