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
            default:
                System.out.println("Vale valik!\n");
        }
    }
    
    private void handleOrder(Scanner scanner) {
        // 1. Vali laud
        System.out.println("\n=== LAUAD ===");
        for (Table t : tables) {
            String info = t.getNumber() + ". " + t.getStatus();
            if (t.getStatus() == Table.TableStatus.BRONEERITUD) {
                for (Reservation r : reservationService.getAllReservations()) {
                    if (r.getTable().getNumber() == t.getNumber()) {
                        info += " - " + r.getCustomer();
                        break;
                    }
                }
            }
            System.out.println(info);
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
        
        // 2. Loo tellimus
        Order order = orderService.createOrder(table.getNumber());
        System.out.println("\nTellimus nr " + order.getOrderNumber() + " lauale " + table.getNumber());
        
        // 3. Lisa tooteid
        boolean done = false;
        while (!done) {
            System.out.println("\n[0] Lõpeta  [M] Menüü  [V] Vaata  [X] Eemalda");
            System.out.print("> ");
            String cmd = scanner.nextLine().toUpperCase();
            
            switch (cmd) {
                case "0":
                    if (order.getItems().isEmpty()) {
                        System.out.println("Tellimus tühi! Lisa tooteid.");
                    } else {
                        done = true;
                    }
                    break;
                case "M":
                    menuService.printMenuWithCategoryChoice(scanner);
                    break;
                case "V":
                    viewOrder(order);
                    break;
                case "X":
                    removeItem(order, scanner);
                    break;
                default:
                    // Proovi lisada: "1 2" = toode 1, kogus 2
                    String[] parts = cmd.split(" ");
                    if (parts.length == 2) {
                        try {
                            int itemId = Integer.parseInt(parts[0]);
                            int qty = Integer.parseInt(parts[1]);
                            MenuItem item = menuService.getByIndex(itemId - 1);
                            if (item != null && qty > 0) {
                                orderService.addItem(order, item, qty);
                                System.out.println("+ " + item.getName() + " x" + qty);
                            } else {
                                System.out.println("Vale toote number!");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Kasuta: number kogus (nt: 1 2)");
                        }
                    } else {
                        System.out.println("Kasuta: 0, M, V, X või 'number kogus'");
                    }
            }
        }
        
        // 4. Kinnita tellimus - näita enne kinnitamist
        System.out.println("\n    KINNITA TELLIMUS   ");
        viewOrder(order);
        System.out.print("Kas oled kindel? (J/E): ");
        
        if (scanner.nextLine().toUpperCase().equals("J")) {
            waiter.takeOrder(table, orderService);
            System.out.println("Tellimus nr " + order.getOrderNumber() + " saadetud kokale!");
        } else {
            System.out.println("Tellimus tühistatud.");
            orderService.getAllOrders().remove(order);
        }
        
        waitForEnter(scanner);
    }
    
    private void viewOrder(Order order) {
        System.out.println("\n--- TELLIMUS NR " + order.getOrderNumber() + " ---");
        if (order.getItems().isEmpty()) {
            System.out.println("(tühi)");
        } else {
            double total = 0;
            for (int i = 0; i < order.getItems().size(); i++) {
                OrderItem item = order.getItems().get(i);
                double price = item.getMenuItem().getPrice() * item.getQuantity();
                System.out.println((i+1) + ". " + item.getMenuItem().getName() + " x" + item.getQuantity() + " = " + price + "euro");
                total += price;
            }
            System.out.println("────────────────────────");
            System.out.println("Kokku: " + total + "euro");
        }
        System.out.println();
    }
    
    private void removeItem(Order order, Scanner scanner) {
        if (order.getItems().isEmpty()) {
            System.out.println("Tellimus tühi!");
            return;
        }
        viewOrder(order);
        System.out.print("Eemalda number: ");
        int idx = scanner.nextInt();
        scanner.nextLine();
        if (idx >= 1 && idx <= order.getItems().size()) {
            OrderItem removed = order.getItems().remove(idx - 1);
            System.out.println("Eemaldatud: " + removed.getMenuItem().getName());
        } else {
            System.out.println("Vale number!");
        }
    }
    
    private void handleUnbroneeri(Scanner scanner) {
        System.out.println("\n    BRONEERINGU TÜHISTAMINE   ");
        boolean found = false;
        for (Table t : tables) {
            if (t.getStatus() == Table.TableStatus.BRONEERITUD) {
                String name = "";
                for (Reservation r : reservationService.getAllReservations()) {
                    if (r.getTable().getNumber() == t.getNumber()) {
                        name = r.getCustomer();
                        break;
                    }
                }
                System.out.println(t.getNumber() + ". Laud " + t.getNumber() + " - " + name);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Ühtegi broneeringut pole!");
            waitForEnter(scanner);
            return;
        }
        
        System.out.print("\nVali laud (0 tagasi): ");
        int num = scanner.nextInt();
        scanner.nextLine();
        if (num == 0) return;
        if (num >= 1 && num <= tables.length) {
            waiter.unbroneeriLaud(tables[num - 1]);
        }
        waitForEnter(scanner);
    }
    
    private void waitForEnter(Scanner scanner) {
        System.out.println("\nVajuta Enter...");
        scanner.nextLine();
    }
    
    @Override
    public String getRoleName() {
        return "WAITER";
    }
}