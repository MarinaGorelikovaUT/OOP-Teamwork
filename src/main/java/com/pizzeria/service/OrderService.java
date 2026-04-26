package com.pizzeria.service;

import com.pizzeria.model.MenuItem;
import com.pizzeria.model.Order;
import com.pizzeria.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

import java.io.*;

import static com.pizzeria.model.Order.OrderStatus.*;

/**
 * Tellimuste haldamise teenus - loob tellimusi, lisab tooteid ja muudab staatuseid
 */
public class OrderService {
    // Kõik tellimused
    private List<Order> orders;


    // File where orders are saved between restarts
    // Fail, kuhu tellimused salvestatakse programmide taaskäivituste vahel
    private static final String SAVE_FILE = "orders.ser";

    public OrderService() {
        this.orders = new ArrayList<>();
    }

    // Loob uue tellimuse antud laua jaoks
    public Order createOrder(int tableNumber) {
        Order order = new Order(tableNumber);
        orders.add(order);
        saveOrders(); // save after creating a new order
        return order;
    }

    // Lisab toote olemasolevale tellimusele
    public OrderItem addItem(Order order, MenuItem menuItem, int quantity) {
        OrderItem item = new OrderItem(menuItem, quantity);
        order.addItem(item);
        saveOrders();
        return item;
    }

    // Muudab tellimuse staatust
    public void updateStatus(Order order, Order.OrderStatus newStatus) {
        order.setStatus(newStatus);
        saveOrders();
    }

    // Tagastab kõik tellimused
    public List<Order> getAllOrders() {
        return orders;
    }

    // Tagastab aktiivsed tellimused
    public List<Order> getActiveOrders() {
        ArrayList<Order> activeOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == NEW || order.getStatus() == IN_PROGRESS) {
                activeOrders.add(order);
            }
        }
        return activeOrders;
    }

    // Sulgeb tellimuse, märgib staatuseks PAID ja eemaldab tellimuste nimekirjast
    public void closeOrder(Order order) {
        order.setStatus(Order.OrderStatus.PAID);
        orders.remove(order);
        saveOrders();
    }

    // Saves all orders to disk using Java serialization
// Salvestab kõik tellimused kettale Java serialiseerimise abil
    public void saveOrders() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(orders);
        } catch (IOException e) {
            System.out.println("Viga tellimuste salvestamisel: " + e.getMessage());
        }
    }

    // Loads orders from disk on program startup
// Also restores the order number counter to avoid duplicate numbers
// Laadib tellimused kettalt programmi käivitamisel
// Taastab ka tellimuse numbri loenduri, et vältida duplikaate
    @SuppressWarnings("unchecked")
    public void loadOrders() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return; // no save file yet - start fresh

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            orders = (List<Order>) ois.readObject();

            // Find the highest order number and set counter above it
            // so new orders never get the same number as existing ones
            int maxNumber = 0;
            for (Order o : orders) {
                if (o.getOrderNumber() > maxNumber) {
                    maxNumber = o.getOrderNumber();
                }
            }
            Order.setCounter(maxNumber + 1);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Viga tellimuste laadimisel: " + e.getMessage());
        }
    }
}
