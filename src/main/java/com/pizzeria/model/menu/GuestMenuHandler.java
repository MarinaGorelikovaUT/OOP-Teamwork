package com.pizzeria.model.menu;

import com.pizzeria.model.*;
import java.time.LocalDateTime;
import java.util.Scanner;
import com.pizzeria.service.MenuService;
import com.pizzeria.service.ReservationService;

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
                // Kontrolli, et menuService ei ole null
                if (menuService != null) {
                    menuService.printMenuWithCategoryChoice(scanner);
                } else {
                    System.out.println("Menüü teenus pole saadaval!\n");
                }
                waitForEnter(scanner);
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
            int num = InputUtils.readInt(scanner);

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
            count = InputUtils.readInt(scanner);
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
                System.out.print("Kuupäev (aasta): ");
                aasta = InputUtils.readInt(scanner);
                if (aasta < 2026 || aasta > 2099)
                    System.out.println("Vale aasta! Sisesta vahemikus 2026-2099.\n");
            }

            // Kuu
            int kuu = 0;
            while (kuu < 1 || kuu > 12) {
                System.out.print("Kuupäev (kuu): ");
                kuu = InputUtils.readInt(scanner);
                if (kuu < 1 || kuu > 12)
                    System.out.println("Vale kuu! Sisesta vahemikus 1-12.\n");
            }

            // Päev
            int paev = 0;
            while (paev < 1 || paev > 31) {
                System.out.print("Kuupäev (päev): ");
                paev = InputUtils.readInt(scanner);
                if (paev < 1 || paev > 31)
                    System.out.println("Vale päev! Sisesta vahemikus 1-31.\n");
            }

            // Tunnid
            int tunnid = -1;
            while (tunnid < 0 || tunnid > 23) {
                System.out.print("Kellaaeg (tunnid): ");
                tunnid = InputUtils.readInt(scanner);
                if (tunnid < 0 || tunnid > 23)
                    System.out.println("Vale tund! Sisesta vahemikus 0-23.\n");
            }

            // Minutid
            int minutid = -1;
            while (minutid < 0 || minutid > 59) {
                System.out.print("Kellaaeg (minutid): ");
                minutid = InputUtils.readInt(scanner);
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
            System.out.println("\nBroneering tehtud!");
            System.out.println("  Laud " + table.getNumber());
            System.out.println("  Broneerija: " + name);
            System.out.println("  Külalisi: " + count);
            System.out.println("  Aeg: " + broneeringAeg.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));            System.out.println();
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