package com.example.inventoryservice.controller;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventories() {
        return ResponseEntity.ok(inventoryService.getAllInventories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        return inventoryService.getInventoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Inventory> getInventoryByProductId(@PathVariable Long productId) {
        return inventoryService.getInventoryByProductId(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/check/{productId}/{quantity}")
    public ResponseEntity<Boolean> checkAvailability(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        return ResponseEntity.ok(inventoryService.checkAvailability(productId, quantity));
    }

    @GetMapping("/reserve/{productId}/{quantity}")
    public ResponseEntity<Boolean> reserveStock(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        return ResponseEntity.ok(inventoryService.reserveStock(productId, quantity));
    }

    @PostMapping("/release/{productId}/{quantity}")
    public ResponseEntity<Boolean> releaseStock(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        return ResponseEntity.ok(inventoryService.releaseStock(productId, quantity));
    }

    @PostMapping("/confirm/{productId}/{quantity}")
    public ResponseEntity<Boolean> confirmReservation(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        return ResponseEntity.ok(inventoryService.confirmReservation(productId, quantity));
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@Valid @RequestBody Inventory inventory) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventoryService.createInventory(inventory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(
            @PathVariable Long id,
            @Valid @RequestBody Inventory inventory) {
        try {
            return ResponseEntity.ok(inventoryService.updateInventory(id, inventory));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/restock/{productId}")
    public ResponseEntity<Inventory> restockInventory(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        try {
            return ResponseEntity.ok(inventoryService.restockInventory(productId, quantity));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}

