package com.pizzeria.model;

import java.util.List;

public class Waiter extends User {
    private ReservationService reservationService;
    
    public Waiter(String name, ReservationService reservationService) {
        super(name, "Waiter");
        this.reservationService = reservationService;
    }
    
    public void takeOrder(Table table){
        if (!isTableReserved(table)) {
            System.out.println("Viga: Laud " + table.getNumber() + " pole broneeritud!");
            return;
        }
        
        System.out.println("Tellimus võetud lauale " + table.getNumber());
        // Tellimuse loomise loogika (tuleb hiljem)
    }
    
    private boolean isTableReserved(Table table) {
        if (reservationService == null) {
            return false;
        }
        
        List<Reservation> allReservations = reservationService.getAllReservations();
        
        for (Reservation reservation : allReservations) {
            if (reservation.getTable() == table) {
                return true;
            }
        }
        return false;
    }
}