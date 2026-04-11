//selleks et kaivitada
// mvn exec:java -Dexec.mainClass="com.pizzeria.Main"

package com.pizzeria;
import com.pizzeria.model.*;
import com.pizzeria.service.OrderService;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        TableService tableService = new TableService();
        Table[] tables = tableService.getAllTables().toArray(new Table[0]);
        ReservationService reservationService = new ReservationService();
        OrderService orderService = new OrderService();

        while (true) {
            System.out.println("  ---PIZZERIA SÜSTEEM---   \n");
            System.out.println("Vali roll:");
            System.out.println("1. Manager");
            System.out.println("2. Ettekandja");
            System.out.println("3. Kokk");
            System.out.println("4. Külaline (broneerimiseks)");
            System.out.println("9. Välju programmist");
            System.out.print("Sinu valik: ");

            int choice = InputUtils.readInt(scanner);

            if (choice == 9) {
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

            System.out.println("\nTere, " + role + "! Programm käivitub...\n");

            CommandLineMenu menu = new CommandLineMenu(role, tables, reservationService, orderService);
            menu.run();

            System.out.println("\nNaasid rolli valimise menüüsse...\n");
        }

        scanner.close();
    }
}