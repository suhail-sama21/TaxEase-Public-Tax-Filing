package com.cognizant.taxease.config;

import com.cognizant.taxease.dao.TaxpayerRepository;
import com.cognizant.taxease.dao.UserRepository;
import com.cognizant.taxease.entity.Taxpayer;
import com.cognizant.taxease.entity.User;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import com.cognizant.taxease.entity.entityEnum.TaxpayerType;
import com.cognizant.taxease.entity.entityEnum.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TaxpayerRepository taxpayerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Check if data already exists to prevent duplicate key exceptions
        if (userRepository.count() == 0) {
            log.info("Starting Database Seeding...");

            try {
                String commonPassword = passwordEncoder.encode("Password123");

                // 1. Create Internal Staff Users
                createUser("System Admin", "admin@taxease.gov", UserRole.ADMINISTRATOR, commonPassword);
                createUser("Officer Sarah", "officer@taxease.gov", UserRole.OFFICER, commonPassword);
                createUser("Manager Mike", "manager@taxease.gov", UserRole.MANAGER, commonPassword);
                createUser("Compliance Head", "compliance@taxease.gov", UserRole.COMPLIANCE, commonPassword);
                createUser("Gov Auditor", "auditor@taxease.gov", UserRole.AUDITOR, commonPassword);

                // 2. Create Taxpayers (Requires a User record + Taxpayer record)

                // Citizen Taxpayer
                User user1 = createUser("John Doe", "johndoe@example.com", UserRole.TAXPAYER, commonPassword);
                createTaxpayer(user1, "John Doe", TaxpayerType.Citizen, "123 Tech Park, Bengaluru");

                // Business Taxpayer
                User user2 = createUser("Global Tech Corp", "billing@globaltech.com", UserRole.TAXPAYER, commonPassword);
                createTaxpayer(user2, "Global Tech Corp", TaxpayerType.Business, "Plot 45, Hitech City, Hyderabad");

                log.info("Database Seeding Completed Successfully!");
                log.info("Test credentials: All accounts use password 'Password123'");

            } catch (Exception e) {
                log.error("Seeding failed due to an error: {}", e.getMessage());
                throw e; // Rollback transaction
            }
        } else {
            log.info("Database already contains data. Skipping seeding process.");
        }
    }

    private User createUser(String name, String email, UserRole role, String password) {
        User user = User.builder()
                .name(name)
                .email(email)
                .phone("9876543210")
                .passwordHash(password)
                .role(role)
                .status(StatusBasic.Active)
                .build();
        // saveAndFlush ensures the ID is generated and available immediately
        return userRepository.saveAndFlush(user);
    }

    private void createTaxpayer(User user, String name, TaxpayerType type, String address) {
        Taxpayer taxpayer = Taxpayer.builder()
                .user(user)
                .name(name)
                .type(type)
                .address(address)
                .contactInfo(user.getEmail())
                .taxpayerIdNumber("TAX-" + System.currentTimeMillis() % 100000)
                .build();
        taxpayerRepository.saveAndFlush(taxpayer);
    }
}