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
            System.out.println("Laud " + table.getNumber() + " Mahutavus: " + table.getCapibility() + " Staatus: " + table.getStatus() + "\n");
        }
        System.out.println();
    }

    private void handleGuestReservation(Scanner scanner) {
        System.out.println("  LAUDA BRONEERIMINE  \n");

        System.out.print("Sisesta oma nimi: ");
        String name = scanner.nextLine();

        System.out.print("Sisesta külaliste arv: ");
        int count = scanner.nextInt();
        scanner.nextLine();

        // Выбор времени бронирования
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
            viewAllTables();

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
                System.out.println("Broneering tehtud! Laud " + table.getNumber() + " broneeritud " + name + " nimele.\n");
            } else {
                System.out.println("Laud on juba broneeritud! Vali teine laud.\n");
            }
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