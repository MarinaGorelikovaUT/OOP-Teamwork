package com.pizzeria.model.menu;

import com.pizzeria.model.*;
import com.pizzeria.service.MenuService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class GuestMenuHandler implements MenuHandler {
    private Guest guest;
    private Table[] tables;
    private ReservationService reservationService;
    private MenuService menuService;

    public GuestMenuHandler(Table[] tables, ReservationService reservationService, MenuService menuService) {
        this.guest = new Guest("Külaline");
        this.tables = tables;
        this.reservationService = reservationService;
        this.menuService = menuService;
    }

    @Override
    public void displayMenu() {
        System.out.println("1. Broneeri laud");
        System.out.println("2. Vaata menüüd");
    }

    @Override
    public void handleInput(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                handleGuestReservation(scanner);
                break;
            case 2:
                menuService.printMenuWithCategoryChoice(scanner);
                break;
            default:
                System.out.println("Vale valik!\n");
        }
    }

    private void viewAllTables() {
        System.out.println("  LAUAD \n");
        for (Table table : tables) {
            System.out.println("Laud " + table.getNumber() + " | Mahutavus: " + table.getCapibility() + " | Staatus: " + table.getStatus());
        }
        System.out.println();
    }

    private void handleGuestReservation(Scanner scanner) {
        System.out.println("  LAUDA BRONEERIMINE  \n");

        //  Laua valimine - kordame kuni valitakse vaba laud
        Table table = null;
        while (table == null) {
            viewAllTables();

            System.out.print("Vali laud (1-" + tables.length + "), 0 tagasi: ");
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

        // Nimi
        System.out.print("Sisesta oma nimi: ");
        String name = scanner.nextLine();

        // Külaliste arv - kordame kuni sisestatakse õige arv

        int count = 0;
        while (count < 1 || count > table.getCapibility()) {
            System.out.print("Sisesta külaliste arv (max " + table.getCapibility() + "): ");
            count = scanner.nextInt();
            scanner.nextLine();
            if (count < 1 || count > table.getCapibility()) {
                System.out.println("Liiga palju külalisi! Laua mahutavus on " + table.getCapibility() + ". Proovi uuesti.\n");
            }
        }


// Broneeringu aja sisestamine
        System.out.println("\nSisesta broneeringu aeg:");
        System.out.print("Aasta: ");
        int aasta = scanner.nextInt();
        System.out.print("Kuu: ");
        int kuu = scanner.nextInt();
        System.out.print("Päev: ");
        int paev = scanner.nextInt();
        System.out.print("Tund (0-23): ");
        int tunnid = scanner.nextInt();
        System.out.print("Minut (0-59): ");
        int minutid = scanner.nextInt();
        scanner.nextLine();

        LocalDateTime broneeringAeg;
        try {
            broneeringAeg = LocalDateTime.of(aasta, kuu, paev, tunnid, minutid);
        } catch (Exception e) {
            System.out.println("Vale kuupäev või kellaaeg! Proovi uuesti.\n");
            waitForEnter(scanner);
            return;
        }

        boolean ok = reservationService.addReservation(table, name, count, broneeringAeg);

        if (ok) {
            System.out.println("Broneering tehtud! Laud " + table.getNumber() + " broneeritud " + name + " nimele.\n");
        } else {
            System.out.println("Broneering ebaõnnestus!\n");
        }

        waitForEnter(scanner);
    }

    private void waitForEnter(Scanner scanner) {
        System.out.println("\nVajuta Enter jätkamiseks...");
        scanner.nextLine();
    }

    @Override
    public String getRoleName() {
        return "GUEST";
    }
}