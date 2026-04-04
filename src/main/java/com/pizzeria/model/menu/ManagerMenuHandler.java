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

        // 1. Выбор стола — повторяем пока не выберут свободный
        Table table = null;
        while (table == null) {
            for (Table t : tables) {
                System.out.println("Laud " + t.getNumber() + " | Mahutavus: " + t.getCapibility() + " | Staatus: " + t.getStatus());
            }

            System.out.print("\nVali laud (1-" + tables.length + "), 0 tagasi: ");
            int num = scanner.nextInt();
            scanner.nextLine();

            if (num == 0) return;
            if (num < 1 || num > tables.length) {
                System.out.println("Vale number! Proovi uuesti.\n");
                continue;
            }

            Table selected = tables[num - 1];
            if (selected.getStatus() != Table.TableStatus.VABA) {
                System.out.println("Laud on juba broneeritud või hõivatud! Vali teine laud.\n");
                continue;
            }

            table = selected;
        }

        // 2. Имя
        System.out.print("Nimi: ");
        String name = scanner.nextLine();

        // 3. Количество гостей — повторяем пока не введут правильное число
        int count = 0;
        while (count < 1 || count > table.getCapibility()) {
            System.out.print("Inimeste arv (max " + table.getCapibility() + "): ");
            count = scanner.nextInt();
            scanner.nextLine();
            if (count < 1 || count > table.getCapibility()) {
                System.out.println("Liiga palju külalisi! Laua mahutavus on " + table.getCapibility() + ". Proovi uuesti.\n");
            }
        }

        // 4. Опция — сейчас или выбрать время
        System.out.println("Broneeringu aeg:");
        System.out.println("1. Broneeri kohe (praegune aeg)");
        System.out.println("2. Vali aeg");
        System.out.print("Vali: ");
        int timeChoice = scanner.nextInt();
        scanner.nextLine();

        LocalDateTime broneeringAeg;

        if (timeChoice == 1) {
            broneeringAeg = LocalDateTime.now();
        } else {
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

            try {
                broneeringAeg = LocalDateTime.of(aasta, kuu, paev, tunnid, minutid);
            } catch (Exception e) {
                System.out.println("Vale kuupäev või kellaaeg! Proovi uuesti.\n");
                waitForEnter(scanner);
                return;
            }
        }

        boolean ok = reservationService.addReservation(table, name, count, broneeringAeg);

        if (ok) {
            System.out.println("Broneering tehtud!\n");
        } else {
            System.out.println("Broneering ebaõnnestus!\n");
        }

        waitForEnter(scanner);
    }

    private void adminViewAllTables(Scanner scanner) {
        System.out.println("Broneeritud lauade nimekiri\n");

        boolean found = false;
        for (Reservation r : reservationService.getAllReservations()) {
            Table table = r.getTable();
            System.out.println("Laud " + table.getNumber() + " | Broneerija: " + r.getCustomer() + " | Külalisi: " + r.getCustomer_count() + " | Aeg: " + r.getTime());
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