package com.springcloud.company.product.service;

import com.springcloud.company.product.entity.Product;
import com.springcloud.company.product.repository.ProductRockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockService {
    private final ProductRockRepository productRockRepository;

    @Transactional
    public void updateStockInTransaction(UUID productId, int newStock) {
        Product product = productRockRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        product.updateQuantity(newStock);
    }
}
