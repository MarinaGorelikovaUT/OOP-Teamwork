package com.pizzeria.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tellimus - sisaldab tellimuse infot, staatust ja tellitud tooteid
 */
public class Order {
    // Tellimuse võimalikud staatused
    public enum OrderStatus {
        NEW, IN_PROGRESS, READY, DELIVERED
    }

    private int orderNumber;
    private int tableNumber;
    private OrderStatus status;
    private LocalDateTime createdTime;
    private List<OrderItem> items;

    // Automaatne loendur tellimuse numbri genereerimiseks
    private static int counter = 1;


    public Order(int tableNumber) {
        this.tableNumber = tableNumber;
        this.orderNumber = counter++;
        this.status = OrderStatus.NEW;
        this.createdTime = LocalDateTime.now();
        this.items = new ArrayList<>();
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void addItem(OrderItem item){
        items.add(item);
    }

    @Override
    public String toString() {
        return "Tellimus #" + getOrderNumber() + " | Laud " + tableNumber + " | " + status + " | " + createdTime;    }
}
