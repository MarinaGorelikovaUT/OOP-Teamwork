package com.pizzeria.model;
import com.pizzeria.model.Table;
import java.time.LocalDateTime;

public class Reservation {


    private Table table;
    private String customer;
    private  int customer_count;

    private LocalDateTime time;


    public Reservation(String customer, int customer_count, Table table, LocalDateTime time) {
        this.customer = customer;
        this.customer_count = customer_count;
        this.table = table;
        this.time = time;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getCustomer_count() {
        return customer_count;
    }

    public void setCustomer_count(int customer_count) {
        this.customer_count = customer_count;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

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
