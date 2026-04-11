package com.pizzeria.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Tellimus - sisaldab tellimuse infot, staatust ja tellitud tooteid
 */
public class Order {
    // Tellimuse võimalikud staatused
    public enum OrderStatus {
        NEW, IN_PROGRESS, READY, DELIVERED, PAID
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

    // Tagastab tellimuse loomise aja kujul pp.kk.aaaa tt:mm
    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return createdTime.format(formatter);
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

    // Arvutab tellimuse kogusumma
    public double getTotalPrice() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    // Kuvab tellimuse kokkuvõtte: number, laud, staatus ja aeg
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return "Tellimus #" + getOrderNumber() + " | Laud " + tableNumber + " | " + status + " | " + createdTime.format(formatter);}
}
