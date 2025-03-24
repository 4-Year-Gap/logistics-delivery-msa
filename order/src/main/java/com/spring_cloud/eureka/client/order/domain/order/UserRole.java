package com.spring_cloud.eureka.client.order.domain.order;

import lombok.Getter;

@Getter
public enum UserRole {

    MASTER(Authority.MASTER),
    HUB_MANAGER(Authority.HUB_MANAGER),
    DELIVERY_STAFF(Authority.DELIVERY_STAFF),
    COMPANY_MANAGER(Authority.COMPANY_MANAGER);

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String MASTER = "ROLE_MASTER";
        public static final String HUB_MANAGER = "ROLE_HUB_MANAGER";
        public static final String DELIVERY_STAFF = "ROLE_DELIVERY_STAFF";
        public static final String COMPANY_MANAGER = "ROLE_COMPANY_MANAGER";
    }
}