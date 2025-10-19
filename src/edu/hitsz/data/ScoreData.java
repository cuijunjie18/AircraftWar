package edu.hitsz.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScoreData {
    public String username;
    public String timestamp;
    public int score;

    public ScoreData() {
        this.username = "testUserName";

        this.timestamp = 
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        this.score = 0;
    }

    public ScoreData(String username, int score, String timestamp) {
        this.username = username;
        this.timestamp = timestamp;
        this.score = score;
    }

    // 添加 getter 方法便于 JTable 使用
    public String getUsername() { return username; }
    public int getScore() { return score; }
    public String getTimestamp() { return timestamp; }
}
