package com.pizzeria.model;

import com.pizzeria.service.ReservationService;

import java.util.List;

public class Manager extends User {

    public Manager(String name) {
        super(name, "Manager");
    }

    public void viewAllTables(List<Table> tables, ReservationService reservationService) {
        System.out.println("\nLaudade nimekiri");

        List<Reservation> allReservations = reservationService.getAllReservations();

        for (Table table : tables) {
            System.out.println(
                    "Laud " + table.getNumber() + " | Mahutavus: " + table.getCapibility() + " | Staatus: " + table.getStatus());
            for (Reservation res : allReservations) {
                if (res.getTable().equals(table)) {
                    System.out.println("  Broneeritud: " + res.getCustomer() +
                            " (" + res.getCustomer_count() + " külalist)");
                    break;
                }
            }
        }
    }
}