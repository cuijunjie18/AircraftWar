package edu.hitsz.difficulty;

public abstract class Difficulty {
    public double eliteProbability;
    public double enemyCycle;
    public double enemyAbility;
    int improve_cycle;
    int cycle_counter;

    public Difficulty(){
        eliteProbability = 0.2;
        enemyCycle = 20.0;
        enemyAbility = 1.0;
        improve_cycle = 0;
        cycle_counter = 0;
    }

    public void improve_difficulty(){}
}