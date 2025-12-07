package com.example.inventoryservice.service;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "getAllInventoriesFallback")
    public List<Inventory> getAllInventories() {
        log.info("Fetching all inventories");
        return inventoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Inventory> getInventoryById(Long id) {
        log.info("Fetching inventory with id: {}", id);
        return inventoryRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "getInventoryByProductIdFallback")
    @Retry(name = "inventoryService")
    public Optional<Inventory> getInventoryByProductId(Long productId) {
        log.info("Fetching inventory for product: {}", productId);
        return inventoryRepository.findByProductId(productId);
    }

    @Transactional(readOnly = true)
    public boolean checkAvailability(Long productId, Integer quantity) {
        log.info("Checking availability for product: {} quantity: {}", productId, quantity);
        return inventoryRepository.findByProductId(productId)
                .map(inventory -> inventory.hasAvailableStock(quantity))
                .orElse(false);
    }

    @Transactional
    public boolean reserveStock(Long productId, Integer quantity) {
        log.info("Reserving stock for product: {} quantity: {}", productId, quantity);
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(productId);

        if (inventoryOpt.isEmpty()) {
            log.warn("Inventory not found for product: {}", productId);
            return false;
        }

        Inventory inventory = inventoryOpt.get();
        if (!inventory.hasAvailableStock(quantity)) {
            log.warn("Insufficient stock for product: {}. Available: {}, Requested: {}",
                    productId, inventory.getAvailableQuantity(), quantity);
            return false;
        }

        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventoryRepository.save(inventory);
        log.info("Successfully reserved {} units for product: {}", quantity, productId);
        return true;
    }

    @Transactional
    public boolean releaseStock(Long productId, Integer quantity) {
        log.info("Releasing reserved stock for product: {} quantity: {}", productId, quantity);
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(productId);

        if (inventoryOpt.isEmpty()) {
            return false;
        }

        Inventory inventory = inventoryOpt.get();
        inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - quantity));
        inventoryRepository.save(inventory);
        return true;
    }

    @Transactional
    public boolean confirmReservation(Long productId, Integer quantity) {
        log.info("Confirming reservation for product: {} quantity: {}", productId, quantity);
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(productId);

        if (inventoryOpt.isEmpty()) {
            return false;
        }

        Inventory inventory = inventoryOpt.get();
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - quantity));
        inventoryRepository.save(inventory);
        return true;
    }

    @Transactional
    public Inventory createInventory(Inventory inventory) {
        log.info("Creating new inventory for product: {}", inventory.getProductId());
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory updateInventory(Long id, Inventory inventoryDetails) {
        log.info("Updating inventory with id: {}", id);
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));

        inventory.setQuantity(inventoryDetails.getQuantity());
        inventory.setWarehouseLocation(inventoryDetails.getWarehouseLocation());
        inventory.setReorderLevel(inventoryDetails.getReorderLevel());

        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory restockInventory(Long productId, Integer quantity) {
        log.info("Restocking product: {} with quantity: {}", productId, quantity);
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventory.setLastRestocked(LocalDateTime.now());

        return inventoryRepository.save(inventory);
    }

    @Transactional
    public void deleteInventory(Long id) {
        log.info("Deleting inventory with id: {}", id);
        inventoryRepository.deleteById(id);
    }

    // Fallback methods
    private List<Inventory> getAllInventoriesFallback(Exception e) {
        log.error("Fallback: Unable to fetch inventories", e);
        return List.of();
    }

    private Optional<Inventory> getInventoryByProductIdFallback(Long productId, Exception e) {
        log.error("Fallback: Unable to fetch inventory for product: {}", productId, e);
        return Optional.empty();
    }
}

