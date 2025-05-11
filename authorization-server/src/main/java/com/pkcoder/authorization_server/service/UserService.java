package com.pkcoder.authorization_server.service;

import com.pkcoder.authorization_server.enumeration.LoginType;
import com.pkcoder.authorization_server.model.User;

/**
 * @author PK Coder
 * @version 1.0
 * @project authorization-server
 * @since 09-03-2025
 */

public interface UserService {
    User getUserByEmail(String email);
    void resetLoginAttempts(String email);
    void updateLoginAttempts(String email, LoginType loginType);
    void setLastLogin(Long userId);
    boolean verifyMfaQrCode(String email, String code);
}
