package com.pizzeria.model.menu;

import com.pizzeria.model.*;
import java.time.LocalDateTime;
import java.util.Scanner;
import com.pizzeria.service.MenuService;

public class GuestMenuHandler implements MenuHandler {
    private Guest guest;
    private Table[] tables;
    private ReservationService reservationService;
    private MenuService menuService;

    // PARANDUS 1: Konstruktor võtab menuService parameetri
    public GuestMenuHandler(Table[] tables, ReservationService reservationService, MenuService menuService) {
        this.guest = new Guest("Külaline");
        this.tables = tables;
        this.reservationService = reservationService;
        this.menuService = menuService;  // NÜÜD ÕIGE
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
                // PARANDUS 3: Kontrolli, et menuService ei ole null
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
        System.out.println("\n=== LAUAD ===\n");
        for (Table table : tables) {
            String status = "";
            if (table.getStatus() == Table.TableStatus.VABA) {
                status = "VABA";
            } else if (table.getStatus() == Table.TableStatus.BRONEERITUD) {
                status = "BRONEERITUD";
            } else {
                status = "HOIVATUD";
            }
            System.out.println("Laud " + table.getNumber() + " | Mahutavus: " + table.getCapibility() + " | Staatus: " + status);
        }
        System.out.println();
    }

    private void handleGuestReservation(Scanner scanner) {
        System.out.println("\n=== LAUDA BRONEERIMINE ===\n");

        System.out.print("Sisesta oma nimi: ");
        String name = scanner.nextLine();

        System.out.print("Sisesta külaliste arv: ");
        int count = scanner.nextInt();
        scanner.nextLine();

        // Broneeringu aja valimine
        System.out.println("\n--- BRONEERINGU AEG ---");
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

            // Kontrolli mahutavust
            if (count > table.getCapibility()) {
                System.out.println("Liiga palju külalisi! Laua mahutavus on " + table.getCapibility() + ". Vali teine laud.\n");
                continue;
            }

            // Kontrolli, kas laud on vaba
            if (table.getStatus() != Table.TableStatus.VABA) {
                System.out.println("Laud on juba broneeritud või hõivatud! Vali teine laud.\n");
                continue;
            }

            ok = reservationService.addReservation(table, name, count, broneeringAeg);

            if (ok) {
                System.out.println("\n✓ Broneering tehtud!");
                System.out.println("  Laud " + table.getNumber());
                System.out.println("  Broneerija: " + name);
                System.out.println("  Külalisi: " + count);
                System.out.println("  Aeg: " + broneeringAeg);
                System.out.println();
            } else {
                System.out.println("Broneering ebaõnnestus! Laud on juba broneeritud.\n");
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