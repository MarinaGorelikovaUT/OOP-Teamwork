package com.pizzeria.model;

import java.util.List;

import com.pizzeria.service.OrderService;
import com.pizzeria.service.ReservationService;

public class Waiter extends User {
    private ReservationService reservationService;
    
    public Waiter(String name, ReservationService reservationService) {
        super(name, "Waiter");
        this.reservationService = reservationService;
    }
    
    public void takeOrder(Table table, OrderService orderService) {
        System.out.println("  TELLIMUSE VÕTMINE \n");
        System.out.println("Ettekandja: " + name);
        System.out.println("Laud: " + table.getNumber());
        
        
        boolean isReserved = false;
        String customerName = "";
        int guestCount = 0;
        
        for (Reservation r : reservationService.getAllReservations()) {
            if (r.getTable().getNumber() == table.getNumber()) {
                isReserved = true;
                customerName = r.getCustomer();
                guestCount = r.getCustomer_count();
                break;
            }
        }
        
        if (isReserved) {
            System.out.println("Broneerija: " + customerName);
            System.out.println("Külaliste arv: " + guestCount);
            System.out.println("Laud on BRONEERITUD");
        } else {
            System.out.println("Laud ei ole broneeritud");
        }
        
        
        if (table.getStatus() == Table.TableStatus.VABA) {
            table.setStatus(Table.TableStatus.HOIVATUD);
            System.out.println("\nLaud " + table.getNumber() + " on nüüd hõivatud (ilma broneeringuta)");
        } else if (table.getStatus() == Table.TableStatus.BRONEERITUD) {
            table.setStatus(Table.TableStatus.HOIVATUD);
            System.out.println("\nLaud " + table.getNumber() + " on nüüd hõivatud (broneeritud klient)");
        } else {
            System.out.println("\nLaud " + table.getNumber() + " on juba hõivatud");
        }
        
      
        List<Order> allOrders = orderService.getAllOrders();
        for (Order order : allOrders) {
            if (order.getTableNumber() == table.getNumber()) {
                if (order.getStatus() == Order.OrderStatus.NEW) {
                    orderService.updateStatus(order, Order.OrderStatus.IN_PROGRESS);
                    System.out.println("\nTellimuse staatus muudetud: NEW -> IN_PROGRESS");
                }
                break;
            }
        }
      
        
        System.out.println("------------------------\n");
    }
    
    public void unbroneeriLaud(Table table) {
        System.out.println("   BRONEERINGU TÜHISTAMINE  \n");
        
        if (table.getStatus() != Table.TableStatus.BRONEERITUD) {
            System.out.println("Viga: Laud " + table.getNumber() + " pole broneeritud!\n");
            return;
        }
        
        List<Reservation> allReservations = reservationService.getAllReservations();
        Reservation toRemove = null;
        String customerName = "";
        
        for (Reservation r : allReservations) {
            if (r.getTable().getNumber() == table.getNumber()) {
                toRemove = r;
                customerName = r.getCustomer();
                break;
            }
        }
        
        if (toRemove != null) {
            allReservations.remove(toRemove);
            System.out.println("Broneering eemaldatud: " + customerName);
        }
        
        table.setStatus(Table.TableStatus.VABA);
        System.out.println("Laud " + table.getNumber() + " on nüüd vaba!\n");
        
        reservationService.saveReservations();
    }
}