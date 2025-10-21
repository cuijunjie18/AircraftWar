package edu.hitsz.difficulty;

public class Hard extends Difficulty{
    public Hard(){
        super();
        eliteProbability = 0.25;
        enemyCycle = 19.0;
        enemyAbility = 1.10;
        improve_cycle = 350;
        cycle_counter = 0;
    }

    @Override
    public void improve_difficulty(){
        cycle_counter++;
        if (cycle_counter == improve_cycle){
            cycle_counter = 0;
            eliteProbability += 0.2;
            enemyCycle -= 0.3;
            enemyAbility += 0.08;
            System.out.printf("提高难度! 精英敌机概率:%f,敌机周期:%f,敌机熟悉提升倍率:%f\n",
            eliteProbability,enemyCycle,enemyAbility);
        }
    }
}
