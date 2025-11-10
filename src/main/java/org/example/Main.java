package org.example;

import org.example.models.Game;

public class Main {
    public static void main(String[] args) {
        new Game(6, 12, 1000, 1).startGame(System.in, System.out);
    }
}