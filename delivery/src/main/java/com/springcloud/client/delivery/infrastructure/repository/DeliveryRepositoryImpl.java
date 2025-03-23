package com.springcloud.client.delivery.infrastructure.repository;



import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springcloud.client.delivery.domain.delivery.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.UUID;



@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements CustomDeliveryRepository{


    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Delivery> search(Integer userId, String role, Pageable pageable) {



        return null;
    }

    @Override
    public Delivery findByOrderIdAndConectionHub(UUID hubId, UUID orderId) {
        return null;
    }
}
