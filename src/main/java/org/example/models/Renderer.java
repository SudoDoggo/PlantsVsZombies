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

        // Column header (still single char per col)
        System.out.print("   ");
        for (int c = 0; c < cols; c++) System.out.print(c % 10);
        if (showPreview) System.out.print(" S");
        System.out.println();

        // Use String[][] instead of char[][]
        String[][] m = new String[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) m[r][c] = ".";
        }

        for (Plant p : grid.getPlantsValue()) {
            String sym = " ";
            if (p instanceof Sunflower) {
                if (p.getTier() == 1) sym = "S";
                else sym = "S" + p.getTier();
            } else if (p instanceof PeaShooter) {
                if (p.getTier() == 1) sym = "P";
                else sym = "P" + p.getTier();
            } else if (p instanceof PotatoWall) {
                if (p.getTier() == 1) sym = "O";
                else sym = "O" + p.getTier();
            }
            m[p.getRow()][p.getCol()] = sym;
        }

        int[][] cnt = new int[rows][cols];
        for (Zombie z : grid.getZombies()) {
            if (z.isDead()) continue;
            if (z.getCol() >= 0 && z.getCol() < cols) {
                cnt[z.getRow()][z.getCol()]++;
            }
        }

        for (int r = 0; r < rows; r++) {
            System.out.printf("%2d ", r);
            for (int c = 0; c < cols; c++) {
                String cell = m[r][c];
                if (cnt[r][c] == 1) {
                    cell = "Z";
                } else if (cnt[r][c] > 1) {
                    cell = Integer.toString(Math.min(9, cnt[r][c]));
                }
                System.out.print(cell);
            }
            if (showPreview) {
                int val = Math.max(0, Math.min(9, preview[r]));
                String pv = (val == 0) ? " " : Integer.toString(val);
                System.out.print(" " + pv);
            }
            System.out.println();
        }

        System.out.println("Legend: . empty | S Sunflower | P PeaShooter | O Potato | Z zombie | Right 'S' number = zombies in that row this round");
        System.out.println("Money: " + money + " | Lives: " + lives);
        System.out.flush();
    }
}
