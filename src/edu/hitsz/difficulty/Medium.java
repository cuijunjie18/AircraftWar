package edu.hitsz.difficulty;

public class Medium extends Difficulty {
    public Medium(){
        super();
        eliteProbability = 0.21;
        enemyCycle = 19.5;
        enemyAbility = 1.05;
        improve_cycle = 400;
        cycle_counter = 0;
    }

    @Override
    public void improve_difficulty(){
        cycle_counter++;
        if (cycle_counter == improve_cycle){
            cycle_counter = 0;
            eliteProbability += 0.1;
            enemyCycle -= 0.2;
            enemyAbility += 0.05;
            System.out.printf("提高难度! 精英敌机概率:%f,敌机周期:%f,敌机熟悉提升倍率:%f\n",
            eliteProbability,enemyCycle,enemyAbility);
        }
    }
}
