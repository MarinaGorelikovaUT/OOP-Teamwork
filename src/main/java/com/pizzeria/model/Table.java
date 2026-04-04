package com.pizzeria.model;
import java.io.Serializable; 

public class Table implements Serializable {
    private static final long serialVersionUID = 1L;
    private int number;
    private int capacity;


    // сделал enum чтобы были только фиксированые наборы значений
    public enum TableStatus {
        VABA, HOIVATUD, BRONEERITUD
    }

    private  TableStatus status;


    public Table(int capacity, int number) {
        this.number = number;
        this.capacity = capacity;
        this.status= TableStatus.VABA;

    }



    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapibility() {
        return capacity;
    }

    public void setCapibility(int capacity) {
        this.capacity = capacity;
    }


    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Table{" +
                ", number=" + number +
                "capacity=" + capacity +
                ", status=" + status;
    }
}
