package com.pkcoder.api_gateway.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * @author PK Coder
 * @version 1.0
 * @project api-gateway
 * @since 10-03-2025
 */

@Component
public class JwtAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        String authorities = source.getClaimAsString("authorities");

        if (!StringUtils.hasText(authorities)) {
            return AuthorityUtils.NO_AUTHORITIES;
        }
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
    }
}
