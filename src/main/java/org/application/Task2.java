package org.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Task2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Задание 2 - GC");
        List<Integer> longs = new ArrayList<>();

        while (true) {
            printGCMenu();
            int command = checkIntInput(scanner);
            switch (command) {
                case 1 -> {
                    for (int i = 0; i <= 10_000_000; i++) {
                        longs.add(i);
                    }
                }
                case 2 -> {
                    longs = new ArrayList<>();
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Извините, такая команда отсутствует :с");
            }
        }
    }

    private static int checkIntInput(Scanner scanner) {
        int num;
        do {
            if (scanner.hasNextInt()) {
                num = scanner.nextInt();
                break;
            } else {
                System.out.print("Не могу распознать число. Введите числовое значение: ");
                scanner.nextLine();
            }
        } while (true);
        return num;
    }

    private static void printGCMenu() {
        System.out.println(
                """
                        Garbage Collector
                        Пожалуйста, выберите одну из цифр (0-2) для задания:
                        1 - Добавить объекты
                        2 - Удалить объекты
                        0 - Выход
                                                
                        Ввод:"""
        );
    }
}
