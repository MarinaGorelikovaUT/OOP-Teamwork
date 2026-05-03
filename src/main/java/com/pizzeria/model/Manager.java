package com.pizzeria.model;

import com.pizzeria.service.ReservationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Manager extends User {

    public Manager(String name) {
        super(name, "Manager");
    }

    public void viewAllTables(List<Table> tables, ReservationService reservationService) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        System.out.println("\nKuupäev: " + now.format(dateFormatter));
        System.out.println("\nLaudade nimekiri");

        List<Reservation> allReservations = reservationService.getAllReservations();

        for (Table table : tables) {
            // Kogume kõik selle laua broneeringud ja sorteerime aja järgi
            List<Reservation> tableReservations = new ArrayList<>();
            for (Reservation res : allReservations) {
                if (res.getTable().getNumber() == table.getNumber()) {
                    tableReservations.add(res);
                }
            }
            tableReservations.sort((a, b) -> a.getTime().compareTo(b.getTime()));

            // Arvutame dünaamilise staatuse praeguse aja põhjal
            String dynamicStatus = computeStatus(tableReservations, now);

            System.out.println(
                    "Laud " + table.getNumber() + " | Mahutavus: " + table.getCapibility() + " | Staatus: " + dynamicStatus);

            // Kuvame ainult tänased broneeringud koos algus- ja lõpuajaga
            for (Reservation res : tableReservations) {
                if (!res.getTime().toLocalDate().equals(now.toLocalDate())) continue;
                LocalDateTime start = res.getTime();
                LocalDateTime end = start.plusHours(2);
                System.out.println("  Broneeritud: " + res.getCustomer() +
                        " (" + res.getCustomer_count() + " külalist)" +
                        " | " + start.format(timeFormatter) + " - " + end.format(timeFormatter) +
                        " | " + start.format(dateFormatter));
            }
        }
    }

    // Tagastab laua staatuse praeguse aja põhjal:
    // HÕIVATUD - broneering on praegu aktiivne (aeg on käes, pole veel lõppenud)
    // VABA     - aktiivseid broneeringuid pole
    public static String computeStatus(List<Reservation> tableReservations, LocalDateTime now) {
        for (Reservation res : tableReservations) {
            LocalDateTime start = res.getTime();
            LocalDateTime end = start.plusHours(2);

            // Broneering on praegu aktiivne
            if (!now.isBefore(start) && now.isBefore(end)) {
                return "HÕIVATUD";
            }
        }
        return "VABA";
    }
}