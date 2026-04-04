package com.pizzeria.model.menu;

import com.pizzeria.model.*;
import com.pizzeria.service.MenuService;
import com.pizzeria.service.OrderService;
import java.util.Scanner;

public class WaiterMenuHandler implements MenuHandler {
    private Waiter waiter;
    private Table[] tables;
    private ReservationService reservationService;
    private MenuService menuService;
    private OrderService orderService;
    
    public WaiterMenuHandler(Table[] tables, ReservationService reservationService, MenuService menuService, OrderService orderService) {
        this.waiter = new Waiter("Juuri2", reservationService);
        this.tables = tables;
        this.reservationService = reservationService;
        this.menuService = menuService;
        this.orderService = orderService;
    }
    
    @Override
    public void displayMenu() {
        System.out.println("1. Võta tellimus");
        System.out.println("2. Tühista broneering");
        System.out.println("3. Vaata menüüd");
        System.out.println("4. Teeninda lauda");
    }
    
    @Override
    public void handleInput(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                handleOrder(scanner);
                break;
            case 2:
                handleUnbroneeri(scanner);
                break;
            case 3:
                menuService.printMenuWithCategoryChoice(scanner);
                waitForEnter(scanner);
                break;
            case 4:
                handleTableService(scanner);
                waitForEnter(scanner);
                break;
            default:
                System.out.println("Vale valik!\n");
        }
    }
    
    private void handleOrder(Scanner scanner) {
        System.out.println("   TELLIMUSE VÕTMINE   \n");
        
        System.out.println("Kõik lauad:");
        for (Table table : tables) {
            String info = "Laud " + table.getNumber() + " Staatus: " + table.getStatus();
            if (table.getStatus() == Table.TableStatus.BRONEERITUD) {
                for (Reservation r : reservationService.getAllReservations()) {
                    if (r.getTable().getNumber() == table.getNumber()) {
                        info += " Broneerija: " + r.getCustomer();
                        break;
                    }
                }
            }
            System.out.println(info);
        }
        
        System.out.print("\nMis lauale tellimus (0 tagasi): ");
        int num = scanner.nextInt();
        scanner.nextLine();
        
        if (num == 0) return;
        if (num < 1 || num > tables.length) {
            System.out.println("Vale number!\n");
            return;
        }
        
        Table table = tables[num - 1];
        
        Order newOrder = orderService.createOrder(table.getNumber());
        System.out.println("Uus tellimus lauale " + table.getNumber() + "\n");
        
        boolean ordering = true;
        while (ordering) {
            menuService.printMenu();
            
            System.out.print("\nSisesta toote number (0 lõpetamiseks): ");
            int itemNum = scanner.nextInt();
            scanner.nextLine();
            
            if (itemNum == 0) {
                ordering = false;
                System.out.println("Tellimus lõpetatud!\n");
            } else {
                MenuItem selectedItem = menuService.getByIndex(itemNum - 1);
                if (selectedItem != null) {
                    System.out.print("Mitu: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    
                    orderService.addItem(newOrder, selectedItem, quantity);
                    System.out.println("Lisatud: " + selectedItem.getName() + " x" + quantity + "\n");
                } else {
                    System.out.println("Vale number!\n");
                }
            }
        }
        
        System.out.println("  TELLIMUSE KOKKUVÕTE  \n");
        System.out.println("Laud: " + table.getNumber());
        System.out.println("Tooted:");
        
        double total = 0;
        for (OrderItem item : newOrder.getItems()) {
            double itemTotal = item.getMenuItem().getPrice() * item.getQuantity();
            System.out.println("  - " + item.getMenuItem().getName() + " x" + item.getQuantity() + " = " + itemTotal + "euro");
            total += itemTotal;
        }
        System.out.println("Kogusumma: " + total + "euro\n");
        
        waiter.takeOrder(table, orderService);
        
        System.out.println("Tellimus saadetud kokale!\n");
        waitForEnter(scanner);
    }
    
    private void handleUnbroneeri(Scanner scanner) {
        System.out.println("  BRONEERINGU TÜHISTAMINE  ");
        
        System.out.println("Broneeritud lauad:");
        boolean hasReservations = false;
        
        for (Table table : tables) {
            if (table.getStatus() == Table.TableStatus.BRONEERITUD) {
                String customerName = "";
                for (Reservation r : reservationService.getAllReservations()) {
                    if (r.getTable().getNumber() == table.getNumber()) {
                        customerName = r.getCustomer();
                        break;
                    }
                }
                System.out.println("Laud " + table.getNumber() + " - " + customerName);
                hasReservations = true;
            }
        }
        
        if (!hasReservations) {
            System.out.println("Ühtegi broneeringut pole!\n");
            waitForEnter(scanner);
            return;
        }
        
        System.out.print("\nVali laua number (0 tagasi): ");
        int num = scanner.nextInt();
        scanner.nextLine();
        
        if (num == 0) return;
        if (num < 1 || num > tables.length) {
            System.out.println("Vale number!\n");
            return;
        }
        
        Table table = tables[num - 1];
        waiter.unbroneeriLaud(table);
        
        waitForEnter(scanner);
    }
    
    private void waitForEnter(Scanner scanner) {
        System.out.println("\nVajuta Enter jätkamiseks...");
        scanner.nextLine();
    }
    
    @Override
    public String getRoleName() {
        return "WAITER";
    }

    // Kuvab hõivatud lauad ja võimaldab valida teenindatava laua
    private void handleTableService(Scanner scanner) {
        System.out.println("   LAUDADE TEENINDAMINE   \n");

        boolean hasOccupied = false;
        for (Table table : tables) {
            if (table.getStatus() == Table.TableStatus.HOIVATUD) {
                System.out.println("Laud " + table.getNumber());
                hasOccupied = true;
            }
        }

        if (!hasOccupied) {
            System.out.println("Ühtegi hõivatud lauda pole!\n");
            return;
        }

        System.out.print("\nVali laud (0 tagasi): ");
        int num = scanner.nextInt();
        scanner.nextLine();

        if (num == 0) return;
        if (num < 1 || num > tables.length) {
            System.out.println("Vale number!\n");
            return;
        }

        Table table = tables[num - 1];

        // Otsib aktiivse tellimuse valitud laua järgi
        Order tableOrder = null;
        for (Order order : orderService.getAllOrders()) {
            if (order.getTableNumber() == table.getNumber()) {
                tableOrder = order;
                break;
            }
        }

        if (tableOrder == null) {
            System.out.println("Sellel laual pole aktiivseid tellimusi!\n");
            return;
        }

        // Näita tellimuse infot
        System.out.println("\nLaud " + table.getNumber() + " | Tellimus nr: " + tableOrder.getOrderNumber() + " | Staatus: " + tableOrder.getStatus());
        System.out.println("Tooted:");
        double total = 0;
        for (OrderItem item : tableOrder.getItems()) {
            System.out.println("  - " + item.toString());
            total += item.getTotalPrice();
        }
        System.out.println("Kogusumma: " + total + "€\n");

        // Tegevuste menüü
        System.out.println("1. Esita arve");
        System.out.println("2. Klient soovib juurde tellida");
        System.out.println("0. Tagasi");
        System.out.print("\nVali tegevus: ");

        int action = scanner.nextInt();
        scanner.nextLine();

        switch (action) {
            // Esitab arve, ootab kliendi makse kinnitust ja sulgeb tellimuse
            case 1:
                System.out.println("Arve:");
                System.out.println("Kogusumma: " + total + "€");
                System.out.print("\nKlient maksis? (J/E): ");
                String confirm = scanner.nextLine();
                if (confirm.equalsIgnoreCase("J")) {
                    orderService.closeOrder(tableOrder);
                    table.setStatus(Table.TableStatus.VABA);
                    System.out.println("Tellimus suletud! Laud " + table.getNumber() + " on nüüd vaba.\n");
                }
                break;

            // Võimaldab kliendil juurde tellida
            case 2:
                System.out.println("Dozakaz - tuleb lisada\n");
                break;
            case 0:
                return;
            default:
                System.out.println("Vale valik!\n");
        }
    }
}