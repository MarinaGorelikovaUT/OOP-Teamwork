package com.pizzeria.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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



    public void saveReservations() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("reservations.ser"))) {
            oos.writeObject(reservations);
        } catch (IOException e) {
            System.out.println("Viga salvestamisel: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadReservations() {
        File file = new File("reservations.ser");
        if (!file.exists()) return;
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            reservations = (List<Reservation>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Viga laadimisel: " + e.getMessage());
        }
    }
}