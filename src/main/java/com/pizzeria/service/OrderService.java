package com.pizzeria.service;

import com.pizzeria.model.MenuItem;
import com.pizzeria.model.Order;
import com.pizzeria.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

import static com.pizzeria.model.Order.OrderStatus.*;

/**
 * Tellimuste haldamise teenus - loob tellimusi, lisab tooteid ja muudab staatuseid
 */
public class OrderService {
    // Kõik tellimused
    private List<Order> orders;

    public OrderService() {
        this.orders = new ArrayList<>();
    }

    // Loob uue tellimuse antud laua jaoks
    public Order createOrder(int tableNumber) {
        Order order = new Order(tableNumber);
        orders.add(order);
        return order;
    }

    // Lisab toote olemasolevale tellimusele
    public void addItem(Order order, MenuItem menuItem, int quantity) {
        order.addItem(new OrderItem(menuItem, quantity));
    }

    // Muudab tellimuse staatust
    public void updateStatus(Order order, Order.OrderStatus newStatus) {
        order.setStatus(newStatus);
    }

    // Tagastab kõik tellimused
    public List<Order> getAllOrders() {
        return orders;
    }

    // Tagastab aktiivsed tellimused
    public List<Order> getActiveOrders() {
        ArrayList<Order> activeOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == NEW || order.getStatus() == IN_PROGRESS)
            activeOrders.add(order);
        } return activeOrders;
    }

    // Sulgeb tellimuse, märgib staatuseks PAID ja eemaldab tellimuste nimekirjast
    public void closeOrder(Order order) {
        order.setStatus(Order.OrderStatus.PAID);
        orders.remove(order);
    }
}
