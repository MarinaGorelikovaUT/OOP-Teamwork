package com.pizzeria.model.menu;

import java.util.Scanner;

public interface MenuHandler {
    void displayMenu();
    void handleInput(int choice, Scanner scanner);
    String getRoleName();
    //
}