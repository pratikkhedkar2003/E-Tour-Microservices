package com.pkcoder.authorization_server.enumeration;

import lombok.Getter;

import static com.pkcoder.authorization_server.constant.ApiConstant.ADMIN_AUTHORITIES;
import static com.pkcoder.authorization_server.constant.ApiConstant.MANAGER_AUTHORITIES;
import static com.pkcoder.authorization_server.constant.ApiConstant.SUPER_ADMIN_AUTHORITIES;
import static com.pkcoder.authorization_server.constant.ApiConstant.USER_AUTHORITIES;

/**
 * @author PK Coder
 * @version 1.0
 * @project authorization-server
 * @since 09-03-2025
 */

@Getter
public enum Authority {
    USER(USER_AUTHORITIES),
    ADMIN(ADMIN_AUTHORITIES),
    SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES),
    MANAGER(MANAGER_AUTHORITIES);

    private final String value;

    Authority(String value) {
        this.value = value;
    }

}
