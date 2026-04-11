package com.pizzeria.model;

import com.pizzeria.service.ReservationService;

public class Guest extends User {
    
    public Guest(String name) {
        super(name, "Guest");
    }
    
    
    public void reserveTable(Table table, String customerName, int guestCount, ReservationService reservationService) {
        System.out.println("\n   LAUDA BRONEERIMINE  ");
        
        if (table.getStatus() != Table.TableStatus.VABA) {
            System.out.println("Laud " + table.getNumber() + " on juba broneeritud või hõivatud!\n");
            return;
        }
        
        boolean ok = reservationService.addReservation(
                table,
                customerName,
                guestCount,
                java.time.LocalDateTime.now()
        );
        
        if (ok) {
            System.out.println("Broneering tehtud! Laud " + table.getNumber() + " broneeritud " + customerName + " nimele.\n");
        } else {
            System.out.println("Broneering ebaõnnestus!\n");
        }
    }
}