package com.pkcoder.authorization_server.utils;

import com.pkcoder.authorization_server.entity.CredentialEntity;
import com.pkcoder.authorization_server.entity.RoleEntity;
import com.pkcoder.authorization_server.entity.UserEntity;
import com.pkcoder.authorization_server.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;

/**
 * @author PK Coder
 * @version 1.0
 * @project authorization-server
 * @since 09-03-2025
 */

public class UserUtils {

    public static User getUser(Authentication authentication) {
        if (authentication instanceof OAuth2AuthorizationCodeRequestAuthenticationToken) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication.getPrincipal();
            return (User) usernamePasswordAuthenticationToken.getPrincipal();
        }
        return (User) authentication.getPrincipal();
    }

    public static User fromUserEntity(UserEntity userEntity, RoleEntity role, CredentialEntity credentialEntity) {
        return User.builder()
                .userUuid(userEntity.getUserUuid())
                .email(userEntity.getEmail())
                .mfa(userEntity.isMfa())
                .accountNonExpired(userEntity.isAccountNonExpired())
                .accountNonLocked(userEntity.isAccountNonLocked())
                .enabled(userEntity.isEnabled())
                .roleEntity(role)
                .credentialEntity(credentialEntity)
                .build();
    }
}
