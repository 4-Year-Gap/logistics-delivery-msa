package com.springcloud.company;

import com.springcloud.company.company.entity.Company;
import com.springcloud.company.company.entity.CompanyType;
import com.springcloud.company.company.repository.CompanyRepository;
import com.springcloud.company.company.service.CompanyService;
import com.springcloud.company.product.entity.Product;
import com.springcloud.company.product.infrastructure.dto.OrderCreateEvent;
import com.springcloud.company.product.repository.ProductRepository;
import com.springcloud.company.product.repository.ProductRockRepository;
import com.springcloud.company.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceConcurrencyTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRockRepository productRockRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    private UUID companyId;
    private UUID productId;
    private final int INITIAL_STOCK = 100;
    private final int THREADS = 10;
    private final int ORDERS_PER_THREAD = 10;

    @BeforeEach
    public void setup() {
        // 테스트용 회사 생성
        // 테스트용 회사 엔티티 생성
        Company company = Company.create(
                "Test Company",
                UUID.randomUUID(),
                CompanyType.RECEIVINGCOMPANY,
                "테스트 회사 주소",
                UUID.randomUUID()
        );

        companyRepository.save(company);
        // company 객체의 다른 필요한 필드 설정
        companyId = company.getId();

        // 테스트용 상품 생성
        UUID userId = UUID.randomUUID();
        Product product = Product.create(
                "테스트 상품",
                10000,
                INITIAL_STOCK,
                userId,
                company
        );
        Product savedProduct = productRockRepository.save(product);
        productId = savedProduct.getId();
    }

    @Test
    public void testConcurrentStockDecrease() throws InterruptedException {
        // Given
        int quantityPerOrder = -1; // 각 주문마다 1개씩 감소
        int totalOrders = THREADS * ORDERS_PER_THREAD;
        int expectedFinalStock = INITIAL_STOCK + (quantityPerOrder * totalOrders);

        // When
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
        CountDownLatch latch = new CountDownLatch(THREADS);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < THREADS; i++) {
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < ORDERS_PER_THREAD; j++) {
                        OrderCreateEvent orderCreateEvent = new OrderCreateEvent(
                                UUID.randomUUID(),  // orderId
                                UUID.randomUUID(),
                                UUID.randomUUID(),
                                productId,          // productId
                                quantityPerOrder    // quantity (negative for decrease)
                        );

                        try {
                            productService.updateStock(orderCreateEvent);
                            successCount.incrementAndGet();
                        } catch (IllegalArgumentException e) {
                            // 재고 부족 예외 포착
                            failureCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드 완료 대기
        executorService.shutdown();

        // Then
        Product finalProduct = productRockRepository.findById(productId).orElseThrow();
        System.out.println("Initial stock: " + INITIAL_STOCK);
        System.out.println("Final stock: " + finalProduct.getStock());
        System.out.println("Successful updates: " + successCount.get());
        System.out.println("Failed updates: " + failureCount.get());

        assertEquals(expectedFinalStock, finalProduct.getStock(),
                "Final stock should be equal to initial stock minus successful decrements");
        assertEquals(totalOrders, successCount.get(),
                "All order operations should succeed");
        assertEquals(0, failureCount.get(),
                "No operations should fail due to pessimistic locking");
    }

    @Test
    public void testConcurrentStockDecreaseWithInsufficientStock() throws InterruptedException {
        // Given
        int quantityPerOrder = -20; // 각 주문마다 20개씩 감소 (일부는 재고 부족으로 실패해야 함)

        // When
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
        CountDownLatch latch = new CountDownLatch(THREADS);
        List<Exception> exceptions = new ArrayList<>();
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < THREADS; i++) {
            executorService.submit(() -> {
                try {
                    OrderCreateEvent orderCreateEvent = new OrderCreateEvent(
                            UUID.randomUUID(),  // orderId
                            UUID.randomUUID(),
                            UUID.randomUUID(),
                            productId,          // productId
                            quantityPerOrder    // quantity (negative for decrease)
                    );

                    try {
                        productService.updateStock(orderCreateEvent);
                        successCount.incrementAndGet();
                    } catch (IllegalArgumentException e) {
                        // 재고 부족 예외 포착
                        failureCount.incrementAndGet();
                        exceptions.add(e);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드 완료 대기
        executorService.shutdown();

        // Then
        Product finalProduct = productRockRepository.findById(productId).orElseThrow();
        System.out.println("Initial stock: " + INITIAL_STOCK);
        System.out.println("Final stock: " + finalProduct.getStock());
        System.out.println("Successful updates: " + successCount.get());
        System.out.println("Failed updates: " + failureCount.get());

        // 성공한 트랜잭션 수만큼만 재고가 감소
        int expectedStock = Math.max(0, INITIAL_STOCK + (quantityPerOrder * successCount.get()));
        assertEquals(expectedStock, finalProduct.getStock(),
                "Final stock should reflect only successful operations");
        assertEquals(THREADS, successCount.get() + failureCount.get(),
                "Total operations should equal thread count");
    }
}