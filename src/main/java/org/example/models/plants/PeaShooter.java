package org.example.models.plants;

import org.example.models.Player;
import org.example.models.zombies.Zombie;

public class PeaShooter extends Plant {
    private int range = 10;
    private int damage = 6;
    private int cooldown = 0;
    private int fireDelay = 2;

    public PeaShooter() {
        super("PeaShooter", 20, 40);
    }

    @Override
    public void act() {
        if (cooldown > 0) {
            cooldown--; return;
        }
        if (grid == null) return;

        Zombie nearestZombie = null;
        int nearRestCol = 15;
        for (Zombie z : grid.getZombies()) {
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

    @Override
    public boolean isUpgraded(Player player){
        if(tier == 1&&player.getMoney()>=80){
            damage *=2;
            tier++;
            player.setMoney(player.getMoney() - 80);
            return true;
        }else if(tier == 2&&player.getMoney()>=200){
            damage *=2;
            tier++;
            player.setMoney(player.getMoney() - 200);
            return true;
        }else if(tier == 3&&player.getMoney()>=400){
            //freeze
            tier++;
            player.setMoney(player.getMoney() - 400);
            return true;
        } else{
            return false;
        }
    }
}