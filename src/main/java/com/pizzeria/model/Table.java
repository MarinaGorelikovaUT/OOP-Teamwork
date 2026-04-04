package com.pizzeria.model;

public class Table {

    private int number;
    private int capacity;

    // Laua võimalikud staatused
    public enum TableStatus {
        VABA, HOIVATUD, BRONEERITUD
    }

    private TableStatus status;

    // Loob uue laua numbri ja mahutavusega, vaikimisi staatus on VABA
    public Table(int capacity, int number) {
        this.number = number;
        this.capacity = capacity;
        this.status = TableStatus.VABA;
    }

    // Tagastab laua numbri
    public int getNumber() {
        return number;
    }

    // Määrab laua numbri
    public void setNumber(int number) {
        this.number = number;
    }

    // Tagastab laua mahutavuse
    public int getCapibility() {
        return capacity;
    }

    // Määrab laua mahutavuse
    public void setCapibility(int capacity) {
        this.capacity = capacity;
    }

    // Tagastab laua praeguse staatuse
    public TableStatus getStatus() {
        return status;
    }

    // Muudab laua staatust
    public void setStatus(TableStatus status) {
        this.status = status;
    }

    // Tagastab laua info tekstina
    @Override
    public String toString() {
        return "Table{" +
                ", number=" + number +
                ", capacity=" + capacity +
                ", status=" + status + "}";
    }
}