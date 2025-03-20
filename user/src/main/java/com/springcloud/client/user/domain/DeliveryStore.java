package com.springcloud.client.user.domain;

public interface DeliveryStore {

    DeliveryDriver save(DeliveryDriver driver);

    void save(DeliveryAssignment assignment);
}