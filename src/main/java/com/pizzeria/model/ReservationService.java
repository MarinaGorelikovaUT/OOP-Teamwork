package com.pizzeria.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    private List<Reservation> reservations;

    public ReservationService() {
        this.reservations = new ArrayList<>();
    }

    public boolean addReservation(Table table, String customer, int guestCount, LocalDateTime time) {
        for (Reservation reservation : reservations) {
            if (reservation.getTable().equals(table) && reservation.getTime().equals(time)) {
                return false;
            }
        }


        Reservation reservation = new Reservation(customer, guestCount, table, time);
        reservations.add(reservation);

        table.setStatus(Table.TableStatus.BRONEERITUD);

        return true;
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }
}