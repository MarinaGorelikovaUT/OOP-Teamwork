package com.pizzeria.model.menu;

import com.pizzeria.model.*;
import com.pizzeria.service.MenuService;
import java.time.LocalDateTime;
import java.util.Scanner;

public class ManagerMenuHandler implements MenuHandler {
    private Manager manager;
    private Table[] tables;
    private ReservationService reservationService;
    private MenuService menuService;

    public ManagerMenuHandler(Table[] tables, ReservationService reservationService, MenuService menuService) {
        this.manager = new Manager("Juri");
        this.tables = tables;
        this.reservationService = reservationService;
        this.menuService = menuService;
    }

    @Override
    public void displayMenu() {
        System.out.println("1. Vaata laudade nimikirja");
        System.out.println("2. Broneeri laud");
        System.out.println("3. Vaata kõiki broneeritud laudu");
        System.out.println("4. Vaata menüüd");
    }

    @Override
    public void handleInput(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                manager.viewAllTables(java.util.Arrays.asList(tables), reservationService);
                waitForEnter(scanner);
                break;
            case 2:
                handleReservation(scanner);
                break;
            case 3:
                adminViewAllTables(scanner);
                break;
            case 4:
                menuService.printMenuWithCategoryChoice(scanner);
                break;
            default:
                System.out.println("Vale valik!\n");
        }
    }

    private void handleReservation(Scanner scanner) {
        System.out.println("  LAUDA BRONEERIMINE  \n");

        System.out.print("Nimi: ");
        String name = scanner.nextLine();

        System.out.print("Inimeste arv: ");
        int count = scanner.nextInt();
        scanner.nextLine();


        // Broneeringu aja valimine
        System.out.print("Kuupäev (aasta): ");
        int aasta = scanner.nextInt();
        System.out.print("Kuupäev (kuu): ");
        int kuu = scanner.nextInt();
        System.out.print("Kuupäev (päev): ");
        int paev = scanner.nextInt();
        System.out.print("Kellaaeg (tunnid): ");
        int tunnid = scanner.nextInt();
        System.out.print("Kellaaeg (minutid): ");
        int minutid = scanner.nextInt();
        scanner.nextLine();

        LocalDateTime broneeringAeg = LocalDateTime.of(aasta, kuu, paev, tunnid, minutid);

        boolean ok = false;
        while (!ok) {
            for (Table table : tables) {
                System.out.println("Laud " + table.getNumber() + " | Mahutavus: " + table.getCapibility() + " | Staatus: " + table.getStatus() + "\n");
            }

            System.out.print("Vali laud (1-" + tables.length + "), 0 tagasi: ");
            int num = scanner.nextInt();
            scanner.nextLine();

            if (num == 0) return;
            if (num < 1 || num > tables.length) {
                System.out.println("Vale number!\n");
                continue;
            }

            Table table = tables[num - 1];

            // Проверка вместимости
            if (count > table.getCapibility()) {
                System.out.println("Liiga palju külalisi! Laua mahutavus on " + table.getCapibility() + ". Vali teine laud.\n");
                continue;
            }

            ok = reservationService.addReservation(table, name, count, broneeringAeg);

            if (ok) {
                System.out.println("Broneering tehtud!\n");
            } else {
                System.out.println("Laud juba broneeritud! Vali teine laud.\n");
            }
        }

        waitForEnter(scanner);
    }

    private void adminViewAllTables(Scanner scanner) {
        System.out.println("Broneeritud lauade summa\n");

        boolean found = false;
        for (Reservation r : reservationService.getAllReservations()) {
            Table table = r.getTable();
            System.out.println("Laud " + table.getNumber() + " | Broneerija: " + r.getCustomer() + " | Mahutavus: " + table.getCapibility() + " kohta");
            found = true;
        }

        if (!found) {
            System.out.println("Ühtegi broneeringut pole!\n");
        }
        System.out.println("Broneeringuid kokku: " + reservationService.getAllReservations().size() + "\n");

        waitForEnter(scanner);
    }

    private void waitForEnter(Scanner scanner) {
        System.out.println("\nVajuta Enter jätkamiseks...");
        scanner.nextLine();
    }

    @Override
    public String getRoleName() {
        return "MANAGER";
    }
}