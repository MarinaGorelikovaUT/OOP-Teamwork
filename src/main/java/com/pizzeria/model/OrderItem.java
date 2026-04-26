package com.pizzeria.model;
import java.io.Serializable;

/**
 * Tellimuse üksus - üks toode tellimusest koos kogusega
 */
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;
    //гарантирует, что сохранённый файл соответствует этому классу



    // Menüü element, mida telliti ja kogus
    private MenuItem menuItem;
    private int quantity;
    // Näitab, kas toode on uus lisand (lisatellimus)
    private boolean isNew;
    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.isNew = false;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    // Tagastab, kas toode on lisatellimus
    public boolean isNew() {
        return isNew;
    }

    // Määrab, kas toode on lisatellimus
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public int getQuantity() {
        return quantity;
    }

    // Arvutab ühe positsiooni koguhinna (hind × kogus)
    public double getTotalPrice() {
        return menuItem.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return menuItem.getName() + " x" + quantity + " = " + String.format("%.2f", getTotalPrice()) + " €";    }
}
