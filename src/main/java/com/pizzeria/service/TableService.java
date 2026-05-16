package com.pizzeria.service;

import com.pizzeria.model.Table;

import java.util.ArrayList;
import java.util.List;

public class TableService {

    private List<Table> tables;

    // Loob teenuse ja lisab vaikimisi lauad restorani
    public TableService() {
        this.tables = new ArrayList<>();
        this.tables.add(new Table(6, 1));
        this.tables.add(new Table(6, 2));
        this.tables.add(new Table(6, 3));
        this.tables.add(new Table(6, 4));
        this.tables.add(new Table(2, 5));
        this.tables.add(new Table(2, 6));
        this.tables.add(new Table(2, 7));
    }

    // Tagastab kõikide laudade nimekirja
    public List<Table> getAllTables() {
        return tables;
    }

    // Tagastab ainult vabade laudade nimekirja
    public List<Table> getAvailableTables() {
        List<Table> availableTables = new ArrayList<>();

        for (Table table : tables) {
            if (table.getStatus() == Table.TableStatus.VABA) {
                availableTables.add(table);
            }
        }

        return availableTables;
    }
}