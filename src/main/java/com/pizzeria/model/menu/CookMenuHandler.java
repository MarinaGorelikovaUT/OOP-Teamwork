package com.pizzeria.model.menu;

import com.pizzeria.model.*;
import com.pizzeria.service.OrderService;
import java.util.List;
import java.util.Scanner;

public class CookMenuHandler implements MenuHandler {
    private Cook cook;
    private OrderService orderService;
    
    public CookMenuHandler(OrderService orderService) {
        this.cook = new Cook("Mari");
        this.orderService = orderService;
    }
    
    @Override
    public void displayMenu() {
        System.out.println("1. Vaata aktiivseid tellimusi");
        System.out.println("2. Lõpeta tellimus (märgi valmis)");
    }

    //
    @Override
    public void handleInput(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                cook.viewActiveOrders(orderService);
                waitForEnter(scanner);
                break;
            case 2:
                handleCompleteOrder(scanner);
                break;
            default:
                System.out.println("Vale valik!\n");
        }
    }
    
    private void handleCompleteOrder(Scanner scanner) {
        System.out.println("  TELLIMUSE LÕPETAMINE  \n");
        
        List<Order> activeOrders = orderService.getActiveOrders();
        
        if (activeOrders.isEmpty()) {
            System.out.println("Hetkel pole ühtegi aktiivset tellimust.\n");
            return;
        }
        
        System.out.println("Aktiivsed tellimused:");
        for (Order order : activeOrders) {
            System.out.println(" Tellimus nr: " + order.getOrderNumber() + " Laud: " + order.getTableNumber());
        }
        
        System.out.print("\nSisesta tellimuse number, mille soovid lõpetada (0 tagasi): ");
        int orderNum = InputUtils.readInt(scanner);
        
        if (orderNum == 0) return;
        
        cook.completeOrder(orderService, orderNum);
        waitForEnter(scanner);
    }
    
    private void waitForEnter(Scanner scanner) {
        System.out.println("\nVajuta Enter jätkamiseks...");
        scanner.nextLine();
    }
    
    @Override
    public String getRoleName() {
        return "COOK";
    }
}