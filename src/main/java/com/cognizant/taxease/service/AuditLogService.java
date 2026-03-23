package com.cognizant.taxease.service;

import com.cognizant.taxease.entity.AuditLog;
import com.cognizant.taxease.entity.User;

import java.util.List;

public interface AuditLogService {
    public void record(String action, String resource);
    public void recordRegistration(User newUser, String action, String resource);
    public List<AuditLog> list();
    public AuditLog get(Long id);
}
