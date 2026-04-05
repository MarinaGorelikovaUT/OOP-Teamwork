package com.pizzeria.model.menu;

import com.pizzeria.model.*;
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
        LocalDateTime broneeringAeg = null;
        while (broneeringAeg == null) {

            // Aasta
            int aasta = 0;
            while (aasta < 2026 || aasta > 2099) {
                System.out.print("Kuupäev (aasta, 2026-2099): ");
                aasta = scanner.nextInt();
                scanner.nextLine();
                if (aasta < 2026 || aasta > 2099)
                    System.out.println("Vale aasta! Sisesta vahemikus 2026-2099.\n");
            }

            // Kuu
            int kuu = 0;
            while (kuu < 1 || kuu > 12) {
                System.out.print("Kuupäev (kuu, 1-12): ");
                kuu = scanner.nextInt();
                scanner.nextLine();
                if (kuu < 1 || kuu > 12)
                    System.out.println("Vale kuu! Sisesta vahemikus 1-12.\n");
            }

            // Päev
            int paev = 0;
            while (paev < 1 || paev > 31) {
                System.out.print("Kuupäev (päev, 1-31): ");
                paev = scanner.nextInt();
                scanner.nextLine();
                if (paev < 1 || paev > 31)
                    System.out.println("Vale päev! Sisesta vahemikus 1-31.\n");
            }

            // Tunnid
            int tunnid = 0;
            while (tunnid < 0 || tunnid > 23) {
                System.out.print("Kellaaeg (tunnid, 0-23): ");
                tunnid = scanner.nextInt();
                scanner.nextLine();
                if (tunnid < 0 || tunnid > 23)
                    System.out.println("Vale tund! Sisesta vahemikus 0-23.\n");
            }

            // Minutid
            int minutid = -1;
            while (minutid < 0 || minutid > 59) {
                System.out.print("Kellaaeg (minutid, 0-59): ");
                minutid = scanner.nextInt();
                scanner.nextLine();
                if (minutid < 0 || minutid > 59)
                    System.out.println("Vale minut! Sisesta vahemikus 0-59.\n");
            }

            try {
                LocalDateTime kandidaat = LocalDateTime.of(aasta, kuu, paev, tunnid, minutid);
                if (kandidaat.isBefore(LocalDateTime.now())) {
                    System.out.println("Viga: broneeringu aeg on minevikus! Proovi uuesti.\n");
                } else {
                    broneeringAeg = kandidaat;
                }
            } catch (Exception e) {
                System.out.println("Vale kuupäev! (nt 31. veebruar ei eksisteeri) Proovi uuesti.\n");
            }
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