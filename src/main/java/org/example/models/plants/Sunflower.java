package org.example.models.plants;

import org.example.models.Player;

public class Sunflower extends Plant {
    private int cooldown = 5;
    private int cooldownDelay = 5;
    private int income = 15;

    public Sunflower() {
        super("Sunflower", 25, 20);
    }

    @Override
    public void act() {
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        if (player != null) player.addMoney(income);
        cooldown = cooldownDelay;
    }

    @Override
    public boolean isUpgraded(Player player){
        if(tier == 1&&player.getMoney()>=80){
            income +=15;
            tier++;
            player.setMoney(player.getMoney() - 80);
            return true;
        }else if(tier == 2&&player.getMoney()>=200){
            income +=15;
            tier++;
            player.setMoney(player.getMoney() - 200);
            return true;
        }else if(tier == 3&&player.getMoney()>=400){
            //1 in 8 chance to give 100 income;
            player.setMoney(player.getMoney() - 400);
            tier++;
            return true;
        } else{
            return false;
        }
    }
}