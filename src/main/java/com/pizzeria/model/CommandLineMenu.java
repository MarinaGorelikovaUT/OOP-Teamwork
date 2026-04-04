package com.pizzeria.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class CommandLineMenu {
    private Scanner scanner;
    private Manager manager;
    private Waiter waiter;
    private Cook cook;
    private TableService tableService;
    private ReservationService reservationService;
    private Role role;

    public CommandLineMenu(Role role) {
        this.scanner = new Scanner(System.in);
        this.tableService = new TableService();
        this.manager = new Manager("Juri");
        this.waiter = new Waiter("Juuri2", null);
        this.cook = new Cook("Mari");
        this.reservationService = new ReservationService();
        this.role = role;
    }

    public void displayMainMenu() {
        System.out.println("\nPIZZERIA TELLIMUSSUSTEEM\n");

        if (role == Role.MANAGER) {
            System.out.println("1. Vaata laudade nimikirja");
            System.out.println("2. Broneeri laud");
        } else if (role == Role.WAITER) {
            System.out.println("4. Võta tellimus");
        } else if (role == Role.COOK) {
            System.out.println("5. Vaata aktiivseid tellimusi");
        }

        System.out.println("0. Välju");
        System.out.print("Vali tegevus: ");
    }

    public void processInput(int choice) {
        List<Table> tables = tableService.getAllTables();

        switch (choice) {
            case 1:
                if (role == Role.MANAGER) manager.viewAllTables(tables, reservationService);
                else System.out.println("Pole lubatud!");
                break;
            case 2:
                if (role == Role.MANAGER) handleReservation();
                else System.out.println("Pole lubatud!");
                break;
            case 4:
                if (role == Role.WAITER) handleOrder();
                else System.out.println("Pole lubatud!");
                break;
            case 5:
                if (role == Role.COOK) cook.viewOrders(tables);
                else System.out.println("Pole lubatud!");
                break;
            case 0:
                System.out.println("Väljun...");
                break;
            default:
                System.out.println("Vale valik!");
        }
    }

    private void handleReservation() {
        System.out.println("\nLauda broneerimine\n");

        List<Table> tables = tableService.getAllTables();
        boolean ok = false;

        System.out.print("Nimi: ");
        String name = scanner.nextLine();

        System.out.print("Inimeste arv: ");
        int count = scanner.nextInt();
        scanner.nextLine();

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

        while (!ok) {
            for (Table table : tables) {
                System.out.println(
                        "Laud " + table.getNumber() +
                                " | Mahutavus: " + table.getCapibility() +
                                " | Staatus: " + table.getStatus()
                );
            }

            System.out.print("Vali laud: ");
            int num = scanner.nextInt();
            scanner.nextLine();

            if (num < 1 || num > tables.size()) {
                System.out.println("Vale number!");
                continue;
            }

            Table table = tables.get(num - 1);

            ok = reservationService.addReservation(table, name, count, broneeringAeg);

            if (ok) {
                System.out.println("✅ Broneering tehtud!");
            } else {
                System.out.println("❌ Laud juba broneeritud või liiga väike! Vali teine laud.");
            }
        }
    }

    private void handleOrder() {
        List<Table> tables = tableService.getAllTables();

        System.out.print("Mis lauale tellimus: ");
        int num = scanner.nextInt();
        scanner.nextLine();

        if (num < 1 || num > tables.size()) {
            System.out.println("Vale number!");
            return;
        }

        waiter.takeOrder(tables.get(num - 1));
    }

    public void run() {
        boolean running = true;

        while (running) {
            displayMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) {
                running = false;
            } else {
                processInput(choice);
            }
        }

        scanner.close();
    }
}