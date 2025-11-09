package org.example.models.plants;


import lombok.Getter;
import lombok.Setter;
import org.example.models.Grid;
import org.example.models.Player;

@Getter
@Setter
public abstract class Plant {
    protected int row, col;
    protected final String name;
    protected final int cost;
    protected final int maxHp;
    protected int hp;
    protected Grid grid;
    protected Player player;

    protected Plant(String name, int cost, int maxHp) {
        this.name = name;
        this.cost = cost;
        this.maxHp = maxHp;
        this.hp = this.maxHp;
    }
    public void setPosition(int r,int c){
        this.row = r;
        this.col = c;
    }

    public void receiveDamage(int dmg){
        hp -= dmg;
    }

    public boolean isDestroyed(){
        return hp <= 0;
    }

    public void attach(Grid grid, Player player) {
        this.grid = grid;
        this.player = player;
    }
    public abstract void act();
}