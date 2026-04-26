package com.pizzeria.model.menu;

import com.pizzeria.model.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
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

    private void handleGuestReservation(Scanner scanner) {
        System.out.println("  LAUDA BRONEERIMINE  \n");

        // 1. Nimi
        System.out.print("Sisesta oma nimi: ");
        String name = scanner.nextLine();

        // 2. Laua tüüp (2 või 6 kohta)
        int selectedCapacity = 0;
        while (selectedCapacity == 0) {
            System.out.println("\nVali laua tüüp:");
            System.out.println("1. Väike laud (2 kohta)");
            System.out.println("2. Suur laud (6 kohta)");
            System.out.print("Vali: ");
            int typeChoice = InputUtils.readInt(scanner);
            if (typeChoice == 1) {
                selectedCapacity = 2;
            } else if (typeChoice == 2) {
                selectedCapacity = 6;
            } else {
                System.out.println("Vale valik! Palun vali 1 või 2.\n");
            }
        }

        // 3. Külaliste arv (kontroll vastavalt valitud laua mahutavusele)
        int count = 0;
        while (count < 1 || count > selectedCapacity) {
            System.out.print("Sisesta külaliste arv (1-" + selectedCapacity + "): ");
            count = InputUtils.readInt(scanner);
            if (count < 1 || count > selectedCapacity) {
                System.out.println("Külaliste arv peab olema vahemikus 1-" + selectedCapacity + ".\n");
            }
        }

        // 4. Broneeringu aeg
        LocalDateTime broneeringAeg = null;
        while (broneeringAeg == null) {
            int aasta = 0;
            while (aasta < 2026 || aasta > 2099) {
                System.out.print("Kuupäev (aasta): ");
                aasta = InputUtils.readInt(scanner);
                if (aasta < 2026 || aasta > 2099)
                    System.out.println("Vale aasta! Sisesta vahemikus 2026-2099.\n");
            }
            int kuu = 0;
            while (kuu < 1 || kuu > 12) {
                System.out.print("Kuupäev (kuu): ");
                kuu = InputUtils.readInt(scanner);
                if (kuu < 1 || kuu > 12)
                    System.out.println("Vale kuu! Sisesta vahemikus 1-12.\n");
            }
            int paev = 0;
            while (paev < 1 || paev > 31) {
                System.out.print("Kuupäev (päev): ");
                paev = InputUtils.readInt(scanner);
                if (paev < 1 || paev > 31)
                    System.out.println("Vale päev! Sisesta vahemikus 1-31.\n");
            }
            // Restoran töötab 10:00-22:00, viimane broneering 20:00 (broneering kestab 2 tundi)
            int tunnid = -1;
            while (tunnid < 10 || tunnid > 20) {
                System.out.print("Kellaaeg (tunnid, 10-20): ");
                tunnid = InputUtils.readInt(scanner);
                if (tunnid < 10 || tunnid > 20)
                    System.out.println("Vale tund! Restoran töötab 10:00-22:00, viimane broneering on kell 20:00.\n");
            }
            int minutid = -1;
            while (minutid < 0 || minutid > 59 || (tunnid == 20 && minutid > 0)) {
                System.out.print("Kellaaeg (minutid): ");
                minutid = InputUtils.readInt(scanner);
                if (tunnid == 20 && minutid > 0) {
                    System.out.println("Viimane broneering on kell 20:00! Minutid peavad olema 0.\n");
                } else if (minutid < 0 || minutid > 59) {
                    System.out.println("Vale minut! Sisesta vahemikus 0-59.\n");
                }
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

        // 5. Näita vabu laudu sellel ajal (ainult valitud mahutavusega)
        List<Table> allAvailable = reservationService.getAvailableTablesForTime(tables, broneeringAeg, count);
        List<Table> availableTables = new ArrayList<>();
        for (Table t : allAvailable) {
            if (t.getCapibility() == selectedCapacity) {
                availableTables.add(t);
            }
        }

        if (availableTables.isEmpty()) {
            System.out.println("\nKahjuks pole sellel ajal vabu " + selectedCapacity + "-kohalisi laudu!\n");
            waitForEnter(scanner);
            return;
        }

        System.out.println("\nVabad lauad sellel ajal:");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        for (int i = 0; i < availableTables.size(); i++) {
            Table t = availableTables.get(i);
            System.out.println((i + 1) + ". Laud " + t.getNumber() + " | Mahutavus: " + t.getCapibility());
        }

        // 6. Laua valimine
        Table table = null;
        while (table == null) {
            System.out.print("\nVali laud (1-" + availableTables.size() + "), 0 tagasi: ");
            int num = InputUtils.readInt(scanner);
            if (num == 0) return;
            if (num < 1 || num > availableTables.size()) {
                System.out.println("Vale number! Proovi uuesti.\n");
                continue;
            }
            table = availableTables.get(num - 1);
        }

        // 7. Broneeri
        boolean ok = reservationService.addReservation(table, name, count, broneeringAeg);
        if (ok) {
            System.out.println("\nBroneering tehtud!");
            System.out.println("  Laud " + table.getNumber());
            System.out.println("  Broneerija: " + name);
            System.out.println("  Külalisi: " + count);
            System.out.println("  Aeg: " + broneeringAeg.format(formatter));
            System.out.println();
        } else {
            System.out.println("Broneering ebaõnnestus! See laud on juba broneeritud sellel ajal.\n");
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