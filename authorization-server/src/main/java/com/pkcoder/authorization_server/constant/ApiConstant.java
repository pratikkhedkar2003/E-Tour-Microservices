package com.pkcoder.authorization_server.constant;

/**
 * @author PK Coder
 * @version 1.0
 * @project authorization-server
 * @since 09-03-2025
 */

public final class ApiConstant {
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String AUTHORITY_DELIMITER = ",";
    public static final String USER_AUTHORITIES = "user-profile:read,user-profile:update,tour:read,tour-review:create,booking:create,booking:read";
    public static final String ADMIN_AUTHORITIES = "user:create,user:read,user:update,tour:create,tour:read,tour:update,tour:delete,booking:create,booking:read,booking:update,booking:delete";
    public static final String SUPER_ADMIN_AUTHORITIES = "user:create,user:read,user:update,user:delete,tour:create,tour:read,tour:update,tour:delete,booking:create,booking:read,booking:update,booking:delete";
    public static final String MANAGER_AUTHORITIES = "tour:create,tour:read,tour:update,tour:delete,booking:create,booking:read,booking:update,booking:delete";

    public static final int PASSWORD_EXPIRY_DAYS = 90;

    private ApiConstant() {

    }
}
