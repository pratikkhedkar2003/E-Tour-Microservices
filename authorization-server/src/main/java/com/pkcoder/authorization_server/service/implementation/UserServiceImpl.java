package com.pkcoder.authorization_server.service.implementation;

import com.pkcoder.authorization_server.cache.CacheStore;
import com.pkcoder.authorization_server.entity.CredentialEntity;
import com.pkcoder.authorization_server.entity.UserEntity;
import com.pkcoder.authorization_server.enumeration.LoginType;
import com.pkcoder.authorization_server.exception.ApiException;
import com.pkcoder.authorization_server.model.User;
import com.pkcoder.authorization_server.repository.CredentialRepository;
import com.pkcoder.authorization_server.repository.UserRepository;
import com.pkcoder.authorization_server.service.UserService;
import dev.samstevens.totp.code.CodeVerifier;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.pkcoder.authorization_server.utils.UserUtils.fromUserEntity;
import static java.time.LocalDateTime.now;

/**
 * @author PK Coder
 * @version 1.0
 * @project authorization-server
 * @since 09-03-2025
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class UserServiceImpl implements UserService, UserDetailsService {
    private final CodeVerifier codeVerifier;
    private final UserRepository userRepository;
    private final CacheStore<String, Integer> userCache;
    private final CredentialRepository credentialRepository;

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public User getUserByEmail(String email) throws UsernameNotFoundException {
        UserEntity userEntity = getUserEntityByEmail(email);
        CredentialEntity credentialEntity = getUserCredentialByUserId(userEntity.getId());
        return fromUserEntity(userEntity, userEntity.getRole(), credentialEntity);
    }

    @Override
    public void resetLoginAttempts(String email) {
        UserEntity userEntity = getUserEntityByEmail(email);
        userEntity.setLoginAttempts(0);
        userRepository.save(userEntity);
        userCache.evict(email);
    }

    @Override
    public void updateLoginAttempts(String email, LoginType loginType) throws UsernameNotFoundException {
        UserEntity userEntity = getUserEntityByEmail(email);
        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if (userCache.get(userEntity.getEmail()) == null) {
                    userEntity.setLoginAttempts(0);
                    userEntity.setAccountNonLocked(true);
                }
                userEntity.setLoginAttempts(userEntity.getLoginAttempts() + 1);
                userCache.put(userEntity.getEmail(), userEntity.getLoginAttempts());
                if (userCache.get(userEntity.getEmail()) > 5) {
                    userEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                userEntity.setAccountNonLocked(true);
                userEntity.setLoginAttempts(0);
                userEntity.setLastLogin(now());
                userCache.evict(userEntity.getEmail());
            }
        }
        userRepository.save(userEntity);
    }

    @Override
    public void setLastLogin(Long userId) {
        UserEntity userEntity = getUserEntityById(userId);
        userEntity.setLastLogin(now());
        userRepository.save(userEntity);
    }

    @Override
    public boolean verifyMfaQrCode(String email, String code) {
        UserEntity userEntity = getUserEntityByEmail(email);
        return codeVerifier.isValidCode(userEntity.getQrCodeSecret(), code);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername method for email: {}", email);
        updateLoginAttempts(email, LoginType.LOGIN_ATTEMPT);
        return getUserByEmail(email);
    }

    private CredentialEntity getUserCredentialByUserId(Long userId) {
        return credentialRepository.getCredentialByUserEntityId(userId)
                .orElseThrow(() -> new ApiException("User credentials not found"));
    }

    private UserEntity getUserEntityByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    private UserEntity getUserEntityById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ApiException("User not found"));
    }
}
