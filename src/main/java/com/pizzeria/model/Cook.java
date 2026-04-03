package com.pizzeria.model;

import com.pizzeria.service.OrderService;
import java.util.List;

public class Cook extends User {
    
    public Cook(String name) {
        super(name, "Cook");
    }   
    public void viewActiveOrders(OrderService orderService) {
        System.out.println("   AKTIIVSED TELLIMUSED  \n");
        List<Order> allOrders = orderService.getAllOrders();
        List<Order> activeOrders = orderService.getActiveOrders();
        
        if (activeOrders.isEmpty()) {
            System.out.println("Hetkel pole ühtegi aktiivset tellimust.\n");
            return;
        }
        
        for (Order order : activeOrders) {
            System.out.println("----------------------------------------");
            System.out.println("Tellimus nr: " + order.getOrderNumber());
            System.out.println("Laud: " + order.getTableNumber());
            System.out.println("Staatus: " + order.getStatus());
            System.out.println("Aeg: " + order.getCreatedTime());
            System.out.println("Tooted:");
            
            double total = 0;
            for (OrderItem item : order.getItems()) {
                System.out.println("  - " + item.toString());
                total += item.getTotalPrice();
            }
            System.out.println("Kogusumma: " + total + "€");
            System.out.println("----------------------------------------\n");
        }
    }
    
    public void completeOrder(OrderService orderService, int orderNumber) {
        System.out.println("  TELLIMUSE LÕPETAMINE  \n");
        System.out.println("Otsin tellimust nr: " + orderNumber);
        
        List<Order> allOrders = orderService.getAllOrders();
        boolean found = false;
        
        for (Order order : allOrders) {
            if (order.getOrderNumber() == orderNumber) {
                found = true;
                
                
                if (order.getStatus() == Order.OrderStatus.READY) {
                    System.out.println("Tellimus nr " + orderNumber + " on juba valmis!\n");
                    return;
                }
                
                if (order.getStatus() != Order.OrderStatus.IN_PROGRESS) {
                    System.out.println("Tellimus nr " + orderNumber + " ei ole töös (staatus: " + order.getStatus() + "). Palun oodake kuni tellimus on töös.\n");
                    return;
                }
                
                orderService.updateStatus(order, Order.OrderStatus.READY);
                System.out.println("Tellimus nr " + orderNumber + " on nüüd valmis!\n");
                return;
            }
        }
        
        if (!found) {
            System.out.println("Tellimust nr " + orderNumber + " ei leitud!\n");
        }
    }
    
    // LISA SEE MEETOD - tellimuse kättetoimetamine
    public void deliverOrder(OrderService orderService, int orderNumber) {
        System.out.println("  TELLIMUSE KÄTTETOIMETAMINE   \n");
        System.out.println("Otsin tellimust nr: " + orderNumber);
        
        List<Order> allOrders = orderService.getAllOrders();
        
        for (Order order : allOrders) {
            if (order.getOrderNumber() == orderNumber) {
                if (order.getStatus() != Order.OrderStatus.READY) {
                    System.out.println("Tellimus nr " + orderNumber + " pole veel valmis! Praegune staatus: " + order.getStatus() + "\n");
                    return;
                }
                
                orderService.updateStatus(order, Order.OrderStatus.DELIVERED);
                System.out.println("Tellimus nr " + orderNumber + " on kätte toimetatud!\n");
                
                // Vabasta laud
                List<Table> tables = null; // See tuleb parameetrina anda
                System.out.println("Laud " + order.getTableNumber() + " on nüüd vaba!\n");
                return;
            }
        }
        
        System.out.println("Tellimust nr " + orderNumber + " ei leitud!\n");
    }
}