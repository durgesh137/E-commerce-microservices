package com.example.orderservice.service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "orderService", fallbackMethod = "getAllOrdersFallback")
    public List<Order> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "orderService", fallbackMethod = "getOrderByIdFallback")
    public Optional<Order> getOrderById(Long id) {
        log.info("Fetching order with id: {}", id);
        return orderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUserId(Long userId) {
        log.info("Fetching orders for user: {}", userId);
        return orderRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(OrderStatus status) {
        log.info("Fetching orders with status: {}", status);
        return orderRepository.findByStatus(status);
    }

    @Transactional
    @CircuitBreaker(name = "orderService", fallbackMethod = "createOrderFallback")
    @Retry(name = "orderService")
    public Order createOrder(Order order) {
        log.info("Creating new order for user: {}", order.getUserId());

        // Validate and reserve inventory
        for (OrderItem item : order.getItems()) {
            try {
                Boolean available = inventoryClient.checkAvailability(item.getProductId(), item.getQuantity());
                if (!Boolean.TRUE.equals(available)) {
                    throw new RuntimeException("Product " + item.getProductId() + " is not available in requested quantity");
                }
                inventoryClient.reserveStock(item.getProductId(), item.getQuantity());
            } catch (Exception e) {
                log.error("Failed to check/reserve inventory for product: {}", item.getProductId(), e);
                throw new RuntimeException("Inventory service unavailable", e);
            }
        }

        // Calculate total
        BigDecimal total = order.getItems().stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);

        order.setStatus(OrderStatus.CONFIRMED);
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(Long id, OrderStatus status) {
        log.info("Updating order {} status to: {}", id, status);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        log.info("Deleting order with id: {}", id);
        orderRepository.deleteById(id);
    }

    // Fallback methods
    private List<Order> getAllOrdersFallback(Exception e) {
        log.error("Fallback: Unable to fetch orders", e);
        return List.of();
    }

    private Optional<Order> getOrderByIdFallback(Long id, Exception e) {
        log.error("Fallback: Unable to fetch order with id: {}", id, e);
        return Optional.empty();
    }

    private Order createOrderFallback(Order order, Exception e) {
        log.error("Fallback: Unable to create order", e);
        throw new RuntimeException("Service temporarily unavailable. Please try again later.");
    }
}

