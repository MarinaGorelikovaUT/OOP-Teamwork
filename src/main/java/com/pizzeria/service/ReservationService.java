package com.pizzeria.service;

import com.pizzeria.model.Reservation;
import com.pizzeria.model.Table;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pizzeria.model.Table.TableStatus.BRONEERITUD;
import static com.pizzeria.model.Table.TableStatus.VABA;

public class ReservationService {

    private List<Reservation> reservations;

    // Loob tühja broneeringute nimekirja
    public ReservationService() {
        this.reservations = new ArrayList<>();
    }

    // Lisab broneeringu, kui laud on vaba, mahutavus piisav ja aeg pole võetud
    public boolean addReservation(Table table, String customer, int guestCount, LocalDateTime time) {
        for (Reservation r : reservations) {
            if (r.getTable().equals(table) &&
                    time.isBefore(r.getTime().plusHours(2)) &&
                    r.getTime().isBefore(time.plusHours(2))) {
                return false;
            }
        }
        if (guestCount > table.getCapibility()) {
            return false;
        }
        Reservation reservation = new Reservation(customer, guestCount, table, time);
        reservations.add(reservation);
        saveReservations();
        return true;
    }

    // Tagastab vabad lauad antud ajal ja külaliste arvuga
    public List<Table> getAvailableTablesForTime(Table[] tables, LocalDateTime time, int guestCount) {
        List<Table> available = new ArrayList<>();
        for (Table table : tables) {
            if (table.getCapibility() < guestCount) continue;
            boolean isTaken = false;
            for (Reservation r : reservations) {
                if (r.getTable().equals(table) &&
                        time.isBefore(r.getTime().plusHours(2)) &&
                        r.getTime().isBefore(time.plusHours(2))) {
                    isTaken = true;
                    break;
                }
            }
            if (!isTaken) {
                available.add(table);
            }
        }
        return available;
    }

    // Tagastab kõikide broneeringute nimekirja
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

    public void updateTableStatuses(Table[] tables) {
        // Remove reservations that ended more than 2 hours ago
        reservations.removeIf(r -> LocalDateTime.now().isAfter(r.getTime().plusHours(2)));
        saveReservations();
        for (Reservation r : reservations) {
            if (!LocalDateTime.now().isBefore(r.getTime()) &&
                    LocalDateTime.now().isBefore(r.getTime().plusHours(2))) {
                r.getTable().setStatus(Table.TableStatus.BRONEERITUD);
            } else {
                r.getTable().setStatus(Table.TableStatus.VABA);
            }
        }


    }

    // Tühistab broneeringu
    public boolean cancelReservation(Table table) {
        Reservation toRemove = null;
        for (Reservation r : reservations) {
            if (r.getTable().getNumber() == table.getNumber()) {
                toRemove = r;
                break;
            }
        }
        if (toRemove != null) {
            reservations.remove(toRemove);
            table.setStatus(Table.TableStatus.VABA);
            saveReservations();
            return true;
        }
        return false;
    }

    // Leiab broneeringud nime järgi
    public List<Reservation> searchByName(String name) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getCustomer().equalsIgnoreCase(name))
                result.add(r);
        }
        return result;
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