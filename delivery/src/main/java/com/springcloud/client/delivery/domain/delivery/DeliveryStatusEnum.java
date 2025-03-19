package com.springcloud.client.delivery.domain.delivery;

public enum DeliveryStatusEnum {
    NOT_ACCEPTED("Not Accepted"),
    ACCEPTED("Accepted"),
    IN_TRANSIT("허브 이동중"),
    IN_DELIVER("업체 배송중"),
    CANCELED("취소"),
    COMPLETE("완료"),
    WAITING("대기중");
    private final String displayName;

    DeliveryStatusEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}