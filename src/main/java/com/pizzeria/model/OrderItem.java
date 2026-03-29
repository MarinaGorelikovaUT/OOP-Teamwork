package com.pizzeria.model;

/**
 * Tellimuse üksus - üks toode tellimusest koos kogusega
 */
public class OrderItem {
    // Menüü element, mida telliti ja kogus
    private MenuItem menuItem;
    private int quantity;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
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
        return menuItem.getName() + " x" + quantity + " = " + getTotalPrice() + "€";
    }
}
