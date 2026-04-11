package com.pizzeria.service;

import com.pizzeria.model.Table;

import java.util.ArrayList;
import java.util.List;

public class TableService {

    private List<Table> tabels;

    // Loob teenuse ja lisab vaikimisi lauad restorani
    public TableService() {
        this.tabels = new ArrayList<>();
        this.tabels.add(new Table(6, 1));
        this.tabels.add(new Table(6, 2));
        this.tabels.add(new Table(6, 3));
        this.tabels.add(new Table(6, 4));
        this.tabels.add(new Table(2, 5));
        this.tabels.add(new Table(2, 6));
        this.tabels.add(new Table(2, 7));
    }

    // Tagastab kõikide laudade nimekirja
    public List<Table> getAllTables() {
        return tabels;
    }

    // Tagastab ainult vabade laudade nimekirja
    public List<Table> getAvailableTables() {
        List<Table> availableTables = new ArrayList<>();

        for (Table table : tabels) {
            if (table.getStatus() == Table.TableStatus.VABA) {
                availableTables.add(table);
            }
        }

        return availableTables;
    }
}