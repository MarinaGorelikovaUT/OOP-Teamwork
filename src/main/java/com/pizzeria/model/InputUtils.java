package com.pizzeria.model;

import java.util.Scanner;

// Loeb kasutaja sisendi ja kontrollib, et tegemist on arvuga
public class InputUtils {
    public static int readInt(Scanner sc) {
        while (true) {
            String input = sc.nextLine();
            try {
                int number = Integer.parseInt(input);
                return number;
            } catch (NumberFormatException e) {
                System.out.println("Palun sisestage number.");
            }
        }
    }
}
