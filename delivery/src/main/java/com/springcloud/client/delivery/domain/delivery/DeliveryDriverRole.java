package com.springcloud.client.delivery.domain.delivery;

public enum DeliveryDriverRole {

    HUB(DeliveryDriverRole.Authority.HUB),
    COMPANY(DeliveryDriverRole.Authority.COMPANY);

    private final String authority;

    DeliveryDriverRole(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String HUB = "ROLE_HUB";
        public static final String COMPANY = "ROLE_COMPANY";
    }
}