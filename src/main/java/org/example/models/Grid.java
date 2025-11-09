package org.example.models;

import lombok.Getter;
import lombok.Setter;
import org.example.models.plants.Plant;
import org.example.models.zombies.Zombie;

import java.util.*;
@Getter
@Setter
public class Grid {
    private final int rows, cols;
    private final Map<String, Plant> plants = new HashMap<>();
    private final List<Zombie> zombies = new ArrayList<>();
    private int[] spawnPreviewCounts;


    public boolean inBounds(int r,int c){return r>=0 && r<rows && c>=0 && c<cols;}
    public boolean isEmpty(int r,int c){ return !plants.containsKey(placement(r,c)); }
    public Plant getPlant(int r,int c){ return plants.get(placement(r,c)); }

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public Collection<Plant> getPlantsValue() {
        return plants.values();
    }

    private static String placement(int r, int c){
        return r + "," + c;
    }
    public boolean placePlant(Plant p,int r,int c){
        if (!inBounds(r,c) || !isEmpty(r,c)) return false;
        p.setPosition(r,c);
        plants.put(placement(r,c), p);
        return true;
    }

    public boolean removePlant(int r,int c){return plants.remove(placement(r, c)) != null;}

    public void spawnZombie(int row, int startCol, int hp, int speed, int bounty){
        zombies.add(new Zombie(row, startCol, hp, speed, bounty));
    }
    public int advanceAllZombies() {
        int reached = 0;
        for (Zombie z : zombies) {
            if (z.isDead()) continue;
            int r = z.getRow();
            int nextCol = z.getCol() - z.getSpeed();

            if (nextCol >= 0) {
                Plant plant = getPlant(r, nextCol);
                if (plant != null) {
                    plant.receiveDamage(z.getAttack());
                    if (plant.isDestroyed()) removePlant(r, nextCol);
                    continue;
                }
            }
            if (z.move()) reached++;
        }
        return reached;
    }

    public int collectBountiesAndCleanup() {
        int bounty = 0;
        Iterator<Zombie> it = zombies.iterator();
        while (it.hasNext()) {
            Zombie z = it.next();
            if (z.isDead()) { bounty += z.getBounty(); it.remove(); }
        }
        return bounty;
    }

    public void setSpawnPreviewCounts(int[] counts) {
        if (counts != null && counts.length != rows)
            throw new IllegalArgumentException("spawnPreviewCounts length must equal rows");
        this.spawnPreviewCounts = (counts == null) ? null : Arrays.copyOf(counts, counts.length);
    }
    public void decrementPreviewAtRow(int row) {
        if (spawnPreviewCounts != null && row >= 0 && row < rows && spawnPreviewCounts[row] > 0) {
            spawnPreviewCounts[row]--;
        }
    }
}
