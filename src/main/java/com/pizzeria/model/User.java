package com.pizzeria.model;

public class User {
    protected String name;
    protected String role;


    public User(String name, String role) {
        this.name = name;
        this.role = role;

    }
    
    public String getName() {
        return name;
    }

    public String getRole(){
        return role;
    }

    public void displayInfo() {
        System.out.println("Nimi: " + name + " Roll: " + role);
        
    }
}
