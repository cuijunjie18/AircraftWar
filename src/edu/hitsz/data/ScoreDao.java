package edu.hitsz.data;

import edu.hitsz.data.ScoreData;

public interface ScoreDao {
    public abstract void saveScoreData(ScoreData scoreData,String filename);
    public abstract void showScoreRank(String filename);
}
