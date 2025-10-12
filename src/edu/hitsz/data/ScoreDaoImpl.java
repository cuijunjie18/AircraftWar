package edu.hitsz.data;

import java.io.FileWriter;
import java.io.IOException;

import edu.hitsz.data.ScoreData;

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class ScoreDaoImpl implements ScoreDao {
    @Override
    public void saveScoreData(ScoreData scoreData,String filename) {
        // FileWriter writer = new FileWriter(filename, true);
        // String logEntry = String.format("%s,%d,%s\n", scoreData.username, scoreData.score, scoreData.timestamp);
        // writer.write(logEntry);
        // writer.close();
        // 使用 try-with-resources 自动关闭资源，避免资源泄漏
        try (FileWriter writer = new FileWriter(filename, true)) {
            String logEntry = String.format("%s,%d,%s\n", 
                scoreData.username, scoreData.score, scoreData.timestamp);
            writer.write(logEntry); // 追加写入
            writer.close();
        } catch (IOException e) {
            System.err.println("保存成绩数据时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void showScoreRank(String filename) {
        // 读取文件内容，按分数排序并显示排名
        List<ScoreData> scoreList = new ArrayList<>();

        FileReader reader_file;
        BufferedReader reader;

        try {
            reader_file = new FileReader(filename);
            reader = new BufferedReader(reader_file);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    String timestamp = parts[2];
                    scoreList.add(new ScoreData(username, score, timestamp));
                }
            }
            reader.close();
            reader_file.close();
        } catch (IOException e) {
            System.err.println("读取成绩数据时发生错误: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        // 按分数降序排序
        scoreList.sort((a, b) -> b.score - a.score);
        
        // 显示排名
        System.out.println("*********************************");
        System.out.println("            得分排行榜              ");
        System.out.println("*********************************");
        for (int i = 0; i < scoreList.size(); i++) {
            ScoreData sd = scoreList.get(i);
            System.out.printf("第%d名：%s,%d,%s\n", i + 1, sd.username, sd.score, sd.timestamp);
        }
    }
}
