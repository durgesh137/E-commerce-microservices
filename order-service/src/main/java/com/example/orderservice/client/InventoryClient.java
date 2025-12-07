package com.example.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/api/inventory/check/{productId}/{quantity}")
    Boolean checkAvailability(@PathVariable Long productId, @PathVariable Integer quantity);

    @GetMapping("/api/inventory/reserve/{productId}/{quantity}")
    Boolean reserveStock(@PathVariable Long productId, @PathVariable Integer quantity);
}

