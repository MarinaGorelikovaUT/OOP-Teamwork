package com.pizzeria.model.menu;

import com.pizzeria.model.*;
import com.pizzeria.service.MenuService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
        System.out.println("5. Tühista broneering");
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
            case 5:
                handleUnbroneeri(scanner);
                break;
            default:
                System.out.println("Vale valik!\n");
        }
    }

    private void handleReservation(Scanner scanner) {
        System.out.println("  LAUDA BRONEERIMINE  \n");

        // Laua valimine - kordame kuni valitakse vaba laud
        Table table = null;
        while (table == null) {
            for (Table t : tables) {
                System.out.println("Laud " + t.getNumber() + " | Mahutavus: " + t.getCapibility() + " | Staatus: " + t.getStatus());
            }

            System.out.print("\nVali laud (1-" + tables.length + "), 0 tagasi: ");
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
        System.out.print("Nimi: ");
        String name = scanner.nextLine();

        // Külaliste arv - kordame kuni sisestatakse õige arv
        int count = 0;
        while (count < 1 || count > table.getCapibility()) {
            System.out.print("Inimeste arv (max " + table.getCapibility() + "): ");
            count = InputUtils.readInt(scanner);

            if (count < 1 || count > table.getCapibility()) {
                System.out.println("Liiga palju külalisi! Laua mahutavus on " + table.getCapibility() + ". Proovi uuesti.\n");
            }
        }

        // Broneeringu aja valik
        System.out.println("Broneeringu aeg:");
        System.out.println("1. Broneeri kohe (praegune aeg)");
        System.out.println("2. Vali aeg");
        System.out.print("Vali: ");
        int timeChoice = InputUtils.readInt(scanner);

        LocalDateTime broneeringAeg;

        if (timeChoice == 1) {
            broneeringAeg = LocalDateTime.now();
        } else {
            broneeringAeg = null;
            while (broneeringAeg == null) {
                // Aasta
                int aasta;
                while (true) {
                    System.out.print("Kuupäev (aasta, 2024-2099): ");
                    aasta = InputUtils.readInt(scanner);
                    if (aasta >= 2024 && aasta <= 2099) break;
                    System.out.println("Vale aasta! Sisesta vahemikus 2024-2099.\n");
                }

                // Kuu
                int kuu;
                while (true) {
                    System.out.print("Kuupäev (kuu, 1-12): ");
                    kuu = InputUtils.readInt(scanner);
                    if (kuu >= 1 && kuu <= 12) break;
                    System.out.println("Vale kuu! Sisesta vahemikus 1-12.\n");
                }

                // Päev
                int paev;
                while (true) {
                    System.out.print("Kuupäev (päev, 1-31): ");
                    paev = InputUtils.readInt(scanner);
                    if (paev >= 1 && paev <= 31) break;
                    System.out.println("Vale päev! Sisesta vahemikus 1-31.\n");
                }

                // Tunnid
                int tunnid;
                while (true) {
                    System.out.print("Kellaaeg (tunnid, 0-23): ");
                    tunnid = InputUtils.readInt(scanner);
                    if (tunnid >= 0 && tunnid <= 23) break;
                    System.out.println("Vale tund! Sisesta vahemikus 0-23.\n");
                }

                // Minutid
                int minutid;
                while (true) {
                    System.out.print("Kellaaeg (minutid, 0-59): ");
                    minutid = InputUtils.readInt(scanner);
                    if (minutid >= 0 && minutid <= 59) break;
                    System.out.println("Vale minut! Sisesta vahemikus 0-59.\n");
                }

                try {
                    LocalDateTime kandidaat = LocalDateTime.of(aasta, kuu, paev, tunnid, minutid);
                    if (kandidaat.isBefore(LocalDateTime.now())) {
                        System.out.println("Viga: aeg on minevikus! Proovi uuesti.\n");
                    } else {
                        broneeringAeg = kandidaat;
                    }
                } catch (Exception e) {
                    System.out.println("Vale kuupäev! (nt 31. veebruar ei eksisteeri) Proovi uuesti.\n");
                }
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            System.out.println("Laud " + table.getNumber() + " | Broneerija: " + r.getCustomer() + " | Külalisi: " + r.getCustomer_count() + " | Aeg: " + r.getTime().format(formatter));            found = true;
        }

        if (!found) {
            System.out.println("Ühtegi broneeringut pole!\n");
        }
        System.out.println("Broneeringuid kokku: " + reservationService.getAllReservations().size() + "\n");

        System.out.print("Sisesta nimi otsimiseks või vajuta Enter tagasi: ");
        String name = scanner.nextLine();

        if (!name.isEmpty()) {
            List<Reservation> reservations = reservationService.searchByName(name);
            if (reservations.isEmpty()) {
                System.out.println("Broneeringut ei leitud!\n");
            } else {
                for (Reservation reservation : reservations) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                    System.out.println("Laud " + reservation.getTable().getNumber() + " | Broneerija: " + reservation.getCustomer() + " | Külalisi: " + reservation.getCustomer_count() + " | Aeg: " + reservation.getTime().format(formatter));                }
            }
            waitForEnter(scanner);
        } else {
            waitForEnter(scanner);
        }
    }

    // Tühistab broneeringu
    private void handleUnbroneeri(Scanner scanner) {
        System.out.println("\n  BRONEERINGU TÜHISTAMINE  \n");
        boolean found = false;
        for (Table t : tables) {
            if (t.getStatus() == Table.TableStatus.BRONEERITUD) {
                String name = "";
                for (Reservation r : reservationService.getAllReservations()) {
                    if (r.getTable().getNumber() == t.getNumber()) {
                        name = r.getCustomer();
                        break;
                    }
                }
                System.out.println(t.getNumber() + ". Laud " + t.getNumber() + " - " + name);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Ühtegi broneeringut pole!\n");
            waitForEnter(scanner);
            return;
        }
        System.out.print("\nVali laud (0 tagasi): ");
        int num = InputUtils.readInt(scanner);
        if (num == 0) return;
        if (num >= 1 && num <= tables.length) {
            boolean ok = reservationService.cancelReservation(tables[num - 1]);
            if (ok) {
                System.out.println("Broneering tühistatud! Laud " + num + " on nüüd vaba.\n");
            } else {
                System.out.println("Ei leitud broneeringut!\n");
            }
        } else {
            System.out.println("Vale number!\n");
        }
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