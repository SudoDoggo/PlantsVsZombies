package org.example.models.plants;

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
}