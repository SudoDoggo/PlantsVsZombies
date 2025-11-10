package org.example.models.plants;

import org.example.models.Player;

public class PotatoWall extends Plant {

    public PotatoWall() {
        super("Potato", 20, 200);
    }
    @Override public void act() {}

    @Override
    public boolean isUpgraded(Player player){
        if(tier == 1&&player.getMoney()>=80){
            hp *=2;
            tier++;
            player.setMoney(player.getMoney() - 80);
            return true;
        }else if(tier == 2&&player.getMoney()>=200){
            hp *=2;
            tier++;
            player.setMoney(player.getMoney() - 200);
            return true;
        }else if(tier == 3&&player.getMoney()>=400){
            //burn effect
            player.setMoney(player.getMoney() - 400);
            tier++;
            return true;
        } else{
            return false;
        }
    }
}