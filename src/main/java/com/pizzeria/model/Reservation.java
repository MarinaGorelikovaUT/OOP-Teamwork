package com.pizzeria.model;

import java.time.LocalDateTime;

public class Reservation {

    private Table table;
    private String customer;
    private int customer_count;
    private LocalDateTime time;

    // Loob uue broneeringu kliendi nime, külaliste arvu, laua ja ajaga
    public Reservation(String customer, int customer_count, Table table, LocalDateTime time) {
        this.customer = customer;
        this.customer_count = customer_count;
        this.table = table;
        this.time = time;
    }

    // Tagastab kliendi nime
    public String getCustomer() {
        return customer;
    }

    // Määrab kliendi nime
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    // Tagastab külaliste arvu
    public int getCustomer_count() {
        return customer_count;
    }

    // Määrab külaliste arvu
    public void setCustomer_count(int customer_count) {
        this.customer_count = customer_count;
    }

    // Tagastab broneeritud laua
    public Table getTable() {
        return table;
    }

    // Määrab broneeritud laua
    public void setTable(Table table) {
        this.table = table;
    }

    // Tagastab broneeringu aja
    public LocalDateTime getTime() {
        return time;
    }

    // Määrab broneeringu aja
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    // Tagastab broneeringu info tekstina
    @Override
    public String toString() {
        return "Reservation{" +
                "customer='" + customer + '\'' +
                ", table=" + table +
                ", customer_count=" + customer_count +
                ", time=" + time +
                '}';
    }
}