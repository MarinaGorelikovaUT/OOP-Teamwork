package com.pizzeria.model;

public class MenuItem {
    public enum Category {
        PIZZA, SIDE, DRINK, DESSERT
    }

    private String name;
    private String description;
    private double price;
    private Category category;
}
