package com.cognizant.taxease.repository;

import com.cognizant.taxease.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}