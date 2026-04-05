//selleks et kaivitada
// mvn exec:java -Dexec.mainClass="com.pizzeria.Main"

package com.pizzeria;
import com.pizzeria.service.OrderService;
import com.pizzeria.model.Table;
import com.pizzeria.model.ReservationService;
import com.pizzeria.model.Role;
import com.pizzeria.model.CommandLineMenu;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        Table[] tables = new Table[6];
        for (int i = 0; i < tables.length; i++) {
            tables[i] = new Table(6, i + 1);
        }
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
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
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