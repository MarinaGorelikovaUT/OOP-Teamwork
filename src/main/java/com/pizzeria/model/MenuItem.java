package com.pizzeria.model;

/**
 * Menüü element - pizzeria toode koos nime, kirjelduse, hinna ja kategooriaga
 */
public class MenuItem {
    // Toote kategooria (pitsa, lisand, jook, magustoit)
    public enum Category {
        PIZZA, SIDE, DRINK, DESSERT
    }

    private String name;
    private String description;
    private double price;
    private Category category;

    public MenuItem(String name, String description, double price, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "[" + category + "] " + name + " - hind: " + price + "€" + "\n" + description;
    }
}