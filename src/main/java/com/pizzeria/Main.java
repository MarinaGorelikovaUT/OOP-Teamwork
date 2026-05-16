package com.pizzeria;
import com.pizzeria.model.*;
import com.pizzeria.service.OrderService;
import com.pizzeria.service.ReservationService;
import com.pizzeria.service.TableService;

import java.util.Scanner;
//


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        TableService tableService = new TableService();
        Table[] tables = tableService.getAllTables().toArray(new Table[0]);
        ReservationService reservationService = new ReservationService();
        reservationService.loadReservations();
        OrderService orderService = new OrderService();
        orderService.loadOrders(); // Laadib salvestatud tellimused eelmisest sessioonist

        // Taasta laua staatus aktiivsete tellimuste põhjal
        for (Order order : orderService.getAllOrders()) {
            for (Table table : tables) {
                if (table.getNumber() == order.getTableNumber()) {
                    table.setStatus(Table.TableStatus.HOIVATUD);
                    break;
                }
            }
        }

        while (true) {
            System.out.println("\n════════════════════════════════");
            System.out.println("        PIZZERIA SÜSTEEM        ");
            System.out.println("════════════════════════════════");
            System.out.println("  Vali roll:");
            System.out.println("════════════════════════════════");
            System.out.println("  [1] Manager");
            System.out.println("  [2] Ettekandja");
            System.out.println("  [3] Kokk");
            System.out.println("  [4] Külaline (broneerimiseks)");
            System.out.println("  [0] Välju programmist");
            System.out.println("════════════════════════════════");
            System.out.print("  Sinu valik: ");

            int choice = InputUtils.readInt(scanner);

            if (choice == 0) {
                System.out.println("Programm sulgub...");
                break;
            }

            Role role = null;


            switch (choice) {
                case 1:
                    role = Role.MANAGER;
                    break;
                case 2:
                    role = Role.WAITER;
                    break;
                case 3:
                    role = Role.COOK;
                    break;
                case 4:
                    role = Role.GUEST;
                    break;
                default:
                    System.out.println("Vale valik! Proovi uuesti.\n");
                    continue;
            }
            reservationService.updateTableStatuses(tables);
            CommandLineMenu menu = new CommandLineMenu(role, tables, reservationService, orderService);
            menu.run();

            System.out.println("\nNaasid rolli valimise menüüsse...\n");
        }

        scanner.close();
    }
}