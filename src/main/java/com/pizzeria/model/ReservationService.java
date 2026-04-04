package com.pizzeria.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pizzeria.model.Table.TableStatus.BRONEERITUD;

public class ReservationService {

    private List<Reservation> reservations;

    // Loob tühja broneeringute nimekirja
    public ReservationService() {
        this.reservations = new ArrayList<>();
    }

    // Lisab broneeringu, kui laud on vaba, mahutavus piisav ja aeg pole võetud
    public boolean addReservation(Table table, String customer, int guestCount, LocalDateTime time) {

        if (table.getStatus() == BRONEERITUD) {
            return false;
        }
        if (guestCount > table.getCapibility()) {
            return false;
        }
        for (Reservation reservation : reservations) {
            if (reservation.getTable().equals(table) && reservation.getTime().equals(time)) {
                return false;
            }
        }

        Reservation reservation = new Reservation(customer, guestCount, table, time);
        reservations.add(reservation);
        table.setStatus(BRONEERITUD);

        return true;
    }

    // Tagastab kõikide broneeringute nimekirja
    public List<Reservation> getAllReservations() {
        return reservations;
    }
}