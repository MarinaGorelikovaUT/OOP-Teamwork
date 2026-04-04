package com.pizzeria.model.menu;

import com.pizzeria.model.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class GuestMenuHandler implements MenuHandler {
    private Guest guest;
    private Table[] tables;
    private ReservationService reservationService;

    public GuestMenuHandler(Table[] tables, ReservationService reservationService) {
        this.guest = new Guest("Külaline");
        this.tables = tables;
        this.reservationService = reservationService;
    }

    @Override
    public void displayMenu() {
        System.out.println("1. Vaata laudu");
        System.out.println("2. Broneeri laud");
    }

    @Override
    public void handleInput(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                viewAllTables();
                waitForEnter(scanner);
                break;
            case 2:
                handleGuestReservation(scanner);
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

        // 1. Выбор стола — повторяем пока не выберут свободный
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

        // 2. Имя
        System.out.print("Sisesta oma nimi: ");
        String name = scanner.nextLine();

        // 3. Количество гостей — повторяем пока не введут правильное число
        int count = 0;
        while (count < 1 || count > table.getCapibility()) {
            System.out.print("Sisesta külaliste arv (max " + table.getCapibility() + "): ");
            count = scanner.nextInt();
            scanner.nextLine();
            if (count < 1 || count > table.getCapibility()) {
                System.out.println("Liiga palju külalisi! Laua mahutavus on " + table.getCapibility() + ". Proovi uuesti.\n");
            }
        }


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