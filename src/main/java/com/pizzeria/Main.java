//to run program enter in terminal
// mvn exec:java -Dexec.mainClass="com.pizzeria.Main"

package com.pizzeria;

import com.pizzeria.model.CommandLineMenu;
import com.pizzeria.model.Role;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("PIZZERIA SÜSTEEM\n");
        System.out.println("Vali roll:");
        System.out.println("1. Manager");
        System.out.println("2. Ettekandja");
        System.out.println("3. Kokk");
        System.out.print("Sisesta number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Role role;

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
            default:
                role = Role.WAITER; // fallback
                break;
        }

        System.out.println("Valitud roll: " + role);

        CommandLineMenu menu = new CommandLineMenu(role);
        menu.run();

        scanner.close();
    }
}