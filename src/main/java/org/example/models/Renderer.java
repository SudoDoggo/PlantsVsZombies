package org.example.models;

import org.example.models.plants.Plant;
import org.example.models.plants.PeaShooter;
import org.example.models.plants.PotatoWall;
import org.example.models.plants.Sunflower;
import org.example.models.zombies.Zombie;

public class Renderer {
    public static void print(Grid grid, int money, int lives) {
        int rows = grid.getRows(), cols = grid.getCols();
        int[] preview = grid.getSpawnPreviewCounts();
        boolean showPreview = preview != null;

        //Zombie Col
        System.out.print("   ");
        for (int c = 0; c < cols; c++) System.out.print(c % 10);
        if (showPreview) System.out.print(" S");
        System.out.println();

        char[][] m = new char[rows][cols];
        for (int r = 0; r < rows; r++) for (int c = 0; c < cols; c++) m[r][c] = '.';

        for (Plant p : grid.plants()) {
            char ch = 'A';
            if (p instanceof Sunflower) ch = 'S';
            else if (p instanceof PeaShooter) ch = 'P';
            else if (p instanceof PotatoWall) ch = 'O';
            m[p.getRow()][p.getCol()] = ch;
        }

        int[][] cnt = new int[rows][cols];
        for (Zombie z : grid.zombies()) {
            if (z.isDead()) continue;
            if (z.getCol() >= 0 && z.getCol() < cols) cnt[z.getRow()][z.getCol()]++;
        }

        for (int r = 0; r < rows; r++) {
            System.out.printf("%2d ", r);
            for (int c = 0; c < cols; c++) {
                char ch = m[r][c];
                if (cnt[r][c] == 1) ch = 'Z';
                else if (cnt[r][c] > 1) ch = Character.forDigit(Math.min(9, cnt[r][c]), 10);
                System.out.print(ch);
            }
            if (showPreview) {
                int val = Math.max(0, Math.min(9, preview[r]));
                char pv = val == 0 ? ' ' : (val == 1 ? '1' : Character.forDigit(val, 10));
                System.out.print(" " + pv);
            }
            System.out.println();
        }
        System.out.println("Legend: . empty | S Sunflower | P PeaShooter | O Potato | Z zombie | Right 'S' number = zombies in that row this round");
        System.out.println("Money: " + money + " | Lives: " + lives);
        System.out.flush();
    }
}
