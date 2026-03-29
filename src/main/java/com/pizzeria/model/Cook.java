package com.pizzeria.model;

public class Cook extends User{
    public Cook(String name) {
        super(name, "Cook");

    }
    public void viewOrders(Table[] tables) {
        System.out.println("\nAktiivsed tellimused:");

        for (Table table : tables) {
            if (table.getStatus() == Table.TableStatus.BRONEERITUD) {
                System.out.println("Laud " + table.getNumber() + " tellimus ootel");
            }
        }
    }
}
