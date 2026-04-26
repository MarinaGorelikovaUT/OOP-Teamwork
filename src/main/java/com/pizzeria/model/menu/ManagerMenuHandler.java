package com.pizzeria.model.menu;

import com.pizzeria.model.*;
import com.pizzeria.service.MenuService;
import com.pizzeria.service.OrderService;
import com.pizzeria.service.ReservationService;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ManagerMenuHandler implements MenuHandler {
    private Manager manager;
    private Table[] tables;
    private ReservationService reservationService;
    private MenuService menuService;
    private OrderService orderService;


    public ManagerMenuHandler(Table[] tables, ReservationService reservationService, MenuService menuService, OrderService orderService) {
        this.manager = new Manager("Juri");
        this.tables = tables;
        this.reservationService = reservationService;
        this.menuService = menuService;
        this.orderService = orderService;
    }

    @Override
    public void displayMenu() {
        System.out.println("1. Vaata laudade nimikirja");
        System.out.println("2. Broneeri laud");
        System.out.println("3. Vaata kõiki broneeritud laudu");
        System.out.println("4. Vaata menüüd");
        System.out.println("5. Tühista broneering");
        System.out.println("6. Vaata kõiki tellimusi");
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
            case 6:
                viewAllOrders(scanner);
                break;
            default:
                System.out.println("Vale valik!\n");
        }
    }

    // Kuvab kõik aktiivsed tellimused koos toodete ja kogusummaga
    private void viewAllOrders(Scanner scanner) {
        System.out.println("\n  KÕIK TELLIMUSED  \n");
        if (orderService.getAllOrders().isEmpty()) {
            System.out.println("Ühtegi tellimust pole!\n");
            waitForEnter(scanner);
            return;
        }
        for (Order order : orderService.getAllOrders()) {
            System.out.println("----------------------------------------");
            System.out.println("Tellimus nr: " + order.getOrderNumber());
            System.out.println("Laud: " + order.getTableNumber());
            System.out.println("Staatus: " + order.getStatus());
            System.out.println("Aeg: " + order.getFormattedTime());
            System.out.println("Tooted:");
            for (OrderItem item : order.getItems()) {
                System.out.println("  - " + item.getMenuItem().getName() +
                        " x" + item.getQuantity() +
                        " = " + String.format("%.2f", item.getTotalPrice()) + " €");
            }
            System.out.printf("Kogusumma: %.2f €%n", order.getTotalPrice());
            System.out.println("----------------------------------------\n");
        }
        waitForEnter(scanner);
    }

    // Только этот метод изменён — остальные не тронуты
    private void handleReservation(Scanner scanner) {
        System.out.println("  LAUDA BRONEERIMINE  \n");

        // 1. Nimi
        System.out.print("Nimi: ");
        String name = scanner.nextLine();

        // 2. Külaliste arv
        int count = 0;
        while (count < 1) {
            System.out.print("Inimeste arv: ");
            count = InputUtils.readInt(scanner);
            if (count < 1) {
                System.out.println("Külaliste arv peab olema vähemalt 1.\n");
            }
        }

        // 3. Broneeringu aja valik
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
                int aasta;
                while (true) {
                    System.out.print("Kuupäev (aasta, 2024-2099): ");
                    aasta = InputUtils.readInt(scanner);
                    if (aasta >= 2024 && aasta <= 2099) break;
                    System.out.println("Vale aasta! Sisesta vahemikus 2024-2099.\n");
                }
                int kuu;
                while (true) {
                    System.out.print("Kuupäev (kuu, 1-12): ");
                    kuu = InputUtils.readInt(scanner);
                    if (kuu >= 1 && kuu <= 12) break;
                    System.out.println("Vale kuu! Sisesta vahemikus 1-12.\n");
                }
                int paev;
                while (true) {
                    System.out.print("Kuupäev (päev, 1-31): ");
                    paev = InputUtils.readInt(scanner);
                    if (paev >= 1 && paev <= 31) break;
                    System.out.println("Vale päev! Sisesta vahemikus 1-31.\n");
                }
                int tunnid;
                while (true) {
                    System.out.print("Kellaaeg (tunnid, 0-23): ");
                    tunnid = InputUtils.readInt(scanner);
                    if (tunnid >= 0 && tunnid <= 23) break;
                    System.out.println("Vale tund! Sisesta vahemikus 0-23.\n");
                }
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

        // 4. Näita vabu laudu sellel ajal
        List<Table> availableTables = reservationService.getAvailableTablesForTime(tables, broneeringAeg, count);

        if (availableTables.isEmpty()) {
            System.out.println("\nKahjuks pole sellel ajal " + count + " külalisele sobivaid vabu laudu!\n");
            waitForEnter(scanner);
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        System.out.println("\nVabad lauad sellel ajal (" + broneeringAeg.format(formatter) + "):");
        for (int i = 0; i < availableTables.size(); i++) {
            Table t = availableTables.get(i);
            System.out.println((i + 1) + ". Laud " + t.getNumber() + " | Mahutavus: " + t.getCapibility());
        }

        // 5. Laua valimine
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

        // 6. Broneeri
        boolean ok = reservationService.addReservation(table, name, count, broneeringAeg);
        if (ok) {
            System.out.println("Broneering tehtud!\n");
        } else {
            System.out.println("Broneering ebaõnnestus! See laud on juba broneeritud sellel ajal.\n");
        }
        waitForEnter(scanner);
    }

    private void adminViewAllTables(Scanner scanner) {
        System.out.println("Broneeritud lauade nimekiri\n");

        boolean found = false;
        for (Reservation r : reservationService.getAllReservations()) {
            Table table = r.getTable();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            System.out.println("Laud " + table.getNumber() + " | Broneerija: " + r.getCustomer() + " | Külalisi: " + r.getCustomer_count() + " | Aeg: " + r.getTime().format(formatter));
            found = true;
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
                    System.out.println("Laud " + reservation.getTable().getNumber() + " | Broneerija: " + reservation.getCustomer() + " | Külalisi: " + reservation.getCustomer_count() + " | Aeg: " + reservation.getTime().format(formatter));
                }
            }
            waitForEnter(scanner);
        } else {
            waitForEnter(scanner);
        }
    }

    // Tühistab broneeringu
    private void handleUnbroneeri(Scanner scanner) {
        System.out.println("\n  BRONEERINGU TÜHISTAMINE  \n");
        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("Ühtegi broneeringut pole!\n");
            waitForEnter(scanner);
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            System.out.println((i + 1) + ". Laud " + r.getTable().getNumber() +
                    " | " + r.getCustomer() +
                    " | " + r.getCustomer_count() + " külalist" +
                    " | " + r.getTime().format(formatter));
        }
        System.out.print("\nVali broneeringu number (0 tagasi): ");
        int num = InputUtils.readInt(scanner);
        if (num == 0) return;
        if (num < 1 || num > reservations.size()) {
            System.out.println("Vale number!\n");
            waitForEnter(scanner);
            return;
        }
        Reservation toCancel = reservations.get(num - 1);
        boolean ok = reservationService.cancelReservation(toCancel.getTable());
        if (ok) {
            System.out.println("Broneering tühistatud! Laud " + toCancel.getTable().getNumber() + " on nüüd vaba.\n");
        } else {
            System.out.println("Ei leitud broneeringut!\n");
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
    //
}