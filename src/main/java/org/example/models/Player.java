package org.example.models;

import lombok.Getter;
import lombok.Setter;
import org.example.models.plants.*;

@Getter
@Setter
public class Player {
    private final Grid grid;
    private int money;

    public Player(Grid grid, int startingMoney) {
        this.grid = grid;
        this.money = startingMoney;
    }

    public int getMoney() { return money; }
    public void addMoney(int amount) { money += Math.max(0, amount); }
    public boolean canAfford(int cost) { return money >= cost; }

    public boolean place(Plant plant, int r, int c) {
        if (!canAfford(plant.getCost())) return false;
        if (!grid.placePlant(plant, r, c)) return false;
        plant.attach(grid, this);     // wire dependencies so act() can use grid/player
        money -= plant.getCost();
        return true;
    }
    public boolean upgradePlant(int r,int c, Player player) {return grid.upgradePlant(r,c,player);}

    public boolean placeSunflower(int r,int c){ return place(new Sunflower(), r, c); }
    public boolean placePeaShooter(int r,int c){ return place(new PeaShooter(), r, c); }
    public boolean placePotato(int r,int c){ return place(new PotatoWall(), r, c); }

    public boolean removePlant(int r,int c){ return grid.removePlant(r,c); }
    public Grid grid() { return grid; }
}
