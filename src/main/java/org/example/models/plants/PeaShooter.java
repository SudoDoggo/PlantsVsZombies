package org.example.models.plants;

import org.example.models.zombies.Zombie;

public class PeaShooter extends Plant {
    private int range = 10;
    private int damage = 6;
    private int cooldown = 0;
    private int fireDelay = 2;

    public PeaShooter() {
        super("PeaShooter", 20, 100);
    }

    @Override
    public void act() {
        if (cooldown > 0) {
            cooldown--; return;
        }
        if (grid == null) return;

        Zombie nearestZombie = null;
        int nearRestCol = 15;
        for (Zombie z : grid.zombies()) {
            if (z.isDead() || z.getRow() != row) continue;
            int dc = z.getCol() - col;
            if (dc >= 0 && dc <= range && z.getCol() < nearRestCol) {
                nearRestCol = z.getCol();
                nearestZombie = z;
            }
        }
        if (nearestZombie != null) {
            nearestZombie.takeDamage(damage);
            cooldown = fireDelay;
        }
    }
}