package com.pizzeria.service;

import com.pizzeria.model.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Menüü haldamise teenus - sisaldab kõiki pizzeria tooteid ja nende haldamise meetodeid
 */
public class MenuService {
    private List<MenuItem> menu;

    public MenuService() {
        // Kõik menüü tooted
        this.menu = new ArrayList<>();

        // Pitsad
        menu.add(new MenuItem("Margherita", "Pehme mozzarella , juust, tomatikaste", 7.50, MenuItem.Category.PIZZA));
        menu.add(new MenuItem("Capricciosa", "Artišokk, sriracha kaste, päikesekuivatatud tomat, juust, šampinjonid, peekon, täistera põhi, valge kaste", 9.90, MenuItem.Category.PIZZA));
        menu.add(new MenuItem("Pepperoni", "Pepperoni, juust, tomatikaste", 8.90, MenuItem.Category.PIZZA));
        menu.add(new MenuItem("Quattro Stagioni", "Pepperoni, juust, sink, šampinjonid, paprika, ananass, tomatikaste", 8.30, MenuItem.Category.PIZZA));
        menu.add(new MenuItem("Mexicana", "Pepperoni, juust, jalapeno, oliivid, tex-mex kaste, küüslauk", 8.90, MenuItem.Category.PIZZA));
        menu.add(new MenuItem("Tropicana", "Sinihallitusjuust, juust, sink, ananass, tomatikaste", 8.30, MenuItem.Category.PIZZA));
        menu.add(new MenuItem("Salame", "Juust, oliivid, salaami, tomatikaste", 9.20, MenuItem.Category.PIZZA));
        menu.add(new MenuItem("Vegetaria", "Pehme mozzarella , päikesekuivatatud tomat, tomatikaste, tomat, rukola", 7.90, MenuItem.Category.PIZZA));
        menu.add(new MenuItem("Napoletana", "Tuunikala, juust, oliivid, sibul, tomatikaste", 9.90, MenuItem.Category.PIZZA));
        menu.add(new MenuItem("4 Formaggi", "Cheddar, pehme mozzarella , kadaka suitsujuust, juust, valge kaste", 8.20, MenuItem.Category.PIZZA));

        // Lisandid
        menu.add(new MenuItem("Küüslauk", "", 0.30, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Rukola", "", 0.30, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Tomat", "", 0.90, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Tomatikaste", "", 0.90, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Sibul", "", 0.90, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Paprika", "", 0.90, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Valge kaste", "", 1.20, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Tex-mex kaste", "", 1.20, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Chipotle Dip", "", 1.20, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Küüslauk Dip", "", 1.20, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Ranch Dip", "", 1.20, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Juust", "", 1.50, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Šampinjonid", "", 1.50, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Kana", "", 1.50, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Jalapeno", "", 1.50, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Sink", "", 1.50, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Oliivid", "", 1.50, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Salaami", "", 1.50, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Ananass", "", 1.50, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Cheddar", "", 1.90, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Vegan juust", "", 1.90, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Tuunikala", "", 1.90, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Pepperoni", "", 1.90, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Krevetid", "", 1.90, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Mozzarella", "", 1.90, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Sinihallitusjuust", "", 1.90, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Kadaka suitsujuust", "", 1.90, MenuItem.Category.SIDE));
        menu.add(new MenuItem("Päikesekuivatatud tomat", "", 1.90, MenuItem.Category.SIDE));

        // Joogid
        menu.add(new MenuItem("Coca Cola", "0.5 l", 2.90, MenuItem.Category.DRINK));
        menu.add(new MenuItem("Coca Cola Zero", "0.5 l", 2.90, MenuItem.Category.DRINK));
        menu.add(new MenuItem("Fanta", "0.5 l", 2.90, MenuItem.Category.DRINK));
        menu.add(new MenuItem("Sprite", "0.5 l", 2.90, MenuItem.Category.DRINK));
        menu.add(new MenuItem("Vesi mulliga", "0.5 l", 1.90, MenuItem.Category.DRINK));
        menu.add(new MenuItem("Vesi mullita", "0.5 l", 1.90, MenuItem.Category.DRINK));

        // Magus
        menu.add(new MenuItem("New York Cheesecake", "toorjuust, küpsis, vaarikamoos", 5.90, MenuItem.Category.DESSERT));
        menu.add(new MenuItem("Vaarika-Marshmallow pizza", "vaarikamoos, marshmallow, mascarpone, valge shokolaad", 8.90, MenuItem.Category.DESSERT));
        menu.add(new MenuItem("Nutella-Banaani pizza", "banaan, mascarpone, Nutella, kookos", 8.90, MenuItem.Category.DESSERT));
        menu.add(new MenuItem("Jäätis", "3 palli: shokolaadi, vanilje, maasika", 4.90, MenuItem.Category.DESSERT));
    }

    // Tagastab kõik menüü tooted
    public List<MenuItem> getAllItems() {
        return menu;
    }

    // Tagastab tooted valitud kategooria järgi
    public List<MenuItem> getByCategory(MenuItem.Category category) {
        ArrayList<MenuItem> result = new ArrayList<>();
        for (MenuItem menuItem : menu) {
            if (menuItem.getCategory() == category)
                result.add(menuItem);
        } return result;
    }

    // Kuvab kogu menüü konsoolis
    public void printMenu() {
        MenuItem.Category currentCategory = null;
        int i = 1;
        for (MenuItem menuItem : menu) {
            if (menuItem.getCategory() != currentCategory) {
                currentCategory = menuItem.getCategory();
                System.out.println("\n=== " + currentCategory + " ===");
            }
            System.out.println(i + ". " + menuItem);
            System.out.println();
            i++;
        }
    }

    // Otsib toodet nime järgi
    public MenuItem getByName(String name) {
        for (MenuItem menuItem : menu) {
            if (menuItem.getName().equalsIgnoreCase(name))
                return menuItem;
        }
        return null;
    }

    // Otsib toodet numbri järgi
    public MenuItem getByIndex(int index) {
        if (index < 0 || index >= menu.size()) return null;
        return menu.get(index);
    }

    // // Kuvab menüü kategooriate kaupa, võimaldab kasutajal valida kategooria või kogu menüü
    public void printMenuWithCategoryChoice(Scanner scanner) {
        boolean browsing = true;
        while (browsing) {
            System.out.println(
                    "Vali kategooria:\n" +
                            "1. Pizza\n" +
                            "2. Lisandid\n" +
                            "3. Joogid\n" +
                            "4. Magustoidud\n" +
                            "5. Kogu menüü\n" +
                            "0. Tagasi"
            );

            int choice = scanner.nextInt();
            scanner.nextLine();
            List<MenuItem> items = new ArrayList<>();

            switch (choice) {
                case 0: browsing = false; break;
                case 1:
                    items = getByCategory(MenuItem.Category.PIZZA);
                    System.out.println("\n=== PIZZA ===");
                    break;
                case 2:
                    items = getByCategory(MenuItem.Category.SIDE);
                    System.out.println("\n=== LISANDID ===");
                    break;
                case 3:
                    items = getByCategory(MenuItem.Category.DRINK);
                    System.out.println("\n=== JOOGID ===");
                    break;
                case 4:
                    items = getByCategory(MenuItem.Category.DESSERT);
                    System.out.println("\n=== MAGUS ===");
                    break;
                case 5:
                    printMenu();
                    continue;
                default:
                    System.out.println("Vale valik!\n");
                    return;
            }

            int i = 1;
            for (MenuItem menuItem : items) {
                System.out.println(i + ". " + menuItem);
                System.out.println();
                i++;
            }
        }
    }
}