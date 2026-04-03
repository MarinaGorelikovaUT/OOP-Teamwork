package com.pizzeria.model.menu;

import com.pizzeria.model.*;
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
        viewAllTables();
        
        System.out.print("Vali laud (1-" + tables.length + "), 0 tagasi: ");
        int num = scanner.nextInt();
        scanner.nextLine();
        
        if (num == 0) return;
        if (num < 1 || num > tables.length) {
            System.out.println("Vale number!\n");
            return;
        }
        
        Table table = tables[num - 1];
        
        if (table.getStatus() != Table.TableStatus.VABA) {
            System.out.println("Laud on juba broneeritud või hõivatud!\n");
            waitForEnter(scanner);
            return;
        }
        
        System.out.print("Sisesta oma nimi: ");
        String name = scanner.nextLine();
        
        System.out.print("Sisesta külaliste arv: ");
        int count = scanner.nextInt();
        scanner.nextLine();
        
        guest.reserveTable(table, name, count, reservationService);
        
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