package com.pizzeria.model;

import com.pizzeria.service.MenuService;
import com.pizzeria.service.OrderService;
import com.pizzeria.model.menu.*;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;


public class CommandLineMenu {
    private Scanner scanner;
    private Map<Role, MenuHandler> handlers;
    private Role currentRole;
    private boolean running;

    public CommandLineMenu(Role role, Table[] tables, ReservationService reservationService, OrderService orderService) {
        this.scanner = new Scanner(System.in);
        this.currentRole = role;
        this.running = true;
        
        MenuService menuService = new MenuService();
        
        this.handlers = new HashMap<>();
        handlers.put(Role.MANAGER, new ManagerMenuHandler(tables, reservationService, menuService));
        handlers.put(Role.WAITER, new WaiterMenuHandler(tables, reservationService, menuService, orderService));
        handlers.put(Role.COOK, new CookMenuHandler(orderService));
        handlers.put(Role.GUEST, new GuestMenuHandler(tables, reservationService, menuService));
    }

    public void run() {
        System.out.println("Programm käivitatud!\n");
        System.out.println("Salvestatud broneeringuid: " + getReservationCount() + "\n");
        
        MenuHandler currentHandler = handlers.get(currentRole);
        
        while (running) {
            System.out.println("  PIZZERIA TELLIMUSSUSTEEM  \n");
            System.out.println("Roll: " + currentHandler.getRoleName());
            System.out.println("--------------------------------");
            
            currentHandler.displayMenu();
            System.out.println("0. Logi välja");
            System.out.print("\nVali tegevus: ");
            
           int choice = InputUtils.readInt(scanner);
                
           if (choice == 0) {
               running = false;
               System.out.println("\nLogitakse välja...");
           } else {
               currentHandler.handleInput(choice, scanner);
           }
        }
    }
    
    private int getReservationCount() {
        if (handlers.containsKey(Role.MANAGER)) {
            ManagerMenuHandler m = (ManagerMenuHandler) handlers.get(Role.MANAGER);
        }
        return 0;
    }
}