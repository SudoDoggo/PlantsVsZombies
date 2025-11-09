package org.example.models.zombies;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Zombie {
    private int row;
    private int col;
    private int hp;
    private int speed;
    private int bounty;
    private int attack;

    public Zombie(int row, int startCol, int hp, int speed, int bounty) {
        this(row, startCol, hp, speed, bounty, 10);
    }
    public Zombie(int row, int startCol, int hp, int speed, int bounty, int attack) {
        this.row = row;
        this.col = startCol;
        this.hp = hp;
        this.speed = speed;
        this.bounty = bounty;
        this.attack = attack;
    }

    public boolean move() {
        col -= speed;
        return col < 0;
    }
    public void takeDamage(int dmg){
         hp-=dmg;
    }
    public boolean isDead() {
        return hp <= 0;
    }

}
