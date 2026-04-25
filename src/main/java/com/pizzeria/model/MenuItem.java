package com.pizzeria.model;
import java.io.Serializable;

/**
 * Menüü element - pizzeria toode koos nime, kirjelduse, hinna ja kategooriaga
 */
public class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;
    // Версионный ID для сериализации - гарантирует, что сохранённый файл соответствует этому классу
    // Versioonitunnus serialiseerimiseks - tagab, et salvestatud fail vastab sellele klassile
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

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return name + " - hind: " + price + " €" + "\n" + description;
    }
}