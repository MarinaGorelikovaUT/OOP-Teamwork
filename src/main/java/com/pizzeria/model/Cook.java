package com.pizzeria.model;

import java.util.List;

public class Cook extends User {
    public Cook(String name) {
        super(name, "Cook");
    }

    public void viewOrders(List<Table> tables) {
        System.out.println("\nAktiivsed tellimused:");

        for (Table table : tables) {
            if (table.getStatus() == Table.TableStatus.BRONEERITUD) {
                System.out.println("Laud " + table.getNumber() + " tellimus ootel");
            }
        }
    }
}