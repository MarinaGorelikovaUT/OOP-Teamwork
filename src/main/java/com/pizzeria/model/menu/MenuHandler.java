package com.pizzeria.model.menu;

import java.util.Scanner;

public interface MenuHandler {
    void displayMenu();
    void handleInput(int choice, Scanner scanner);
    String getRoleName();

    // Ootab, kuni kasutaja vajutab Enter
    default void waitForEnter(Scanner scanner) {
        System.out.println("\nVajuta Enter jätkamiseks...");
        scanner.nextLine();
    }
}