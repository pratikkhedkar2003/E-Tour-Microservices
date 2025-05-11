package com.pkcoder.authorization_server.model;

import com.pkcoder.authorization_server.entity.CredentialEntity;
import com.pkcoder.authorization_server.entity.RoleEntity;
import lombok.*;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.StringJoiner;

import static com.pkcoder.authorization_server.constant.ApiConstant.*;
import static java.time.LocalDateTime.now;

/**
 * @author PK Coder
 * @version 1.0
 * @project authorization-server
 * @since 09-03-2025
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails, CredentialsContainer {
    private String userUuid;
    private String email;
    private boolean mfa;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private RoleEntity roleEntity;
    private CredentialEntity credentialEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(new StringJoiner(AUTHORITY_DELIMITER)
                .add(ROLE_PREFIX + roleEntity.getName())
                .add(roleEntity.getAuthorities().getValue())
                .toString()
        );
    }

    @Override
    public String getPassword() {
        return this.credentialEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialEntity.getUpdatedAt().plusDays(PASSWORD_EXPIRY_DAYS).isAfter(now());
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void eraseCredentials() {
        this.credentialEntity = null;
    }
}
