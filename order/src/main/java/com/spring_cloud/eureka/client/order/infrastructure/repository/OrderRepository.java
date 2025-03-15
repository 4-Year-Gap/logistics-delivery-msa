package com.spring_cloud.eureka.client.order.infrastructure.repository;

import com.spring_cloud.eureka.client.order.domain.order.OrderEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, UUID> , OrderCustomRepository {

}
