package org.application;

import java.util.HashMap;
import java.util.Map;

public class Task1 {
    public static void main(String[] args) {
        System.out.println("Задание 1 - JIT");
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 100_000; i++) {
            map.put(i, "value" + i);
        }

        for (int i = 0; i < 100_000; i++) {
            map.remove(i);
        }
    }
}
