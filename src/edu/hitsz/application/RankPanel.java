package edu.hitsz.application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import edu.hitsz.data.*;

import java.util.LinkedList;
import java.util.List;

public class RankPanel extends JPanel {

    private JTable rankTable;
    private DefaultTableModel tableModel;
    private String difficulty; // 当前难度，如 "EASY"
    private String filename = "score.txt";
    private JButton deleteBtn;

    public RankPanel(String difficulty) {
        this.difficulty = difficulty;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));

        // 标题标签
        JLabel titleLabel = new JLabel("难度：" + difficulty, JLabel.LEFT);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // 排行榜标题
        JLabel rankTitle = new JLabel("排行榜", JLabel.CENTER);
        rankTitle.setForeground(Color.RED);
        rankTitle.setFont(new Font("微软雅黑", Font.BOLD, 20));
        add(rankTitle, BorderLayout.CENTER);

        // 表格模型
        String[] columnNames = {"名次", "玩家名", "得分", "记录时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 不可编辑
            }
        };

        rankTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(rankTable);
        add(scrollPane, BorderLayout.CENTER);

        // 操作按钮
        deleteBtn = new JButton("删除选中记录");
        deleteBtn.addActionListener(e -> deleteSelectedRecord());
        add(deleteBtn, BorderLayout.SOUTH);

        // 加载数据
        loadScores();
    }

    // 加载并显示排行榜
    public void loadScores() {
        tableModel.setRowCount(0); // 清空旧数据

        List<ScoreData> scoreList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
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
        } catch (IOException e) {
            System.err.println("读取成绩数据时发生错误: " + e.getMessage());
            return;
        }

        // 按分数降序排序
        scoreList.sort((a, b) -> b.score - a.score);

        // 填充表格
        for (int i = 0; i < scoreList.size(); i++) {
            ScoreData sd = scoreList.get(i);
            Object[] row = {i + 1, sd.username, sd.score, sd.timestamp};
            tableModel.addRow(row);
        }
    }

    // 删除选中记录
    private void deleteSelectedRecord() {
        int selectedRow = rankTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一条记录！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "是否确定删除选中的玩家？",
                "选择一个选项",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // 从表格中移除该行
            tableModel.removeRow(selectedRow);
            // 重写整个文件
            rewriteScoreFile();
        }
    }

    // 重写 score.txt 文件
    private void rewriteScoreFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String username = (String) tableModel.getValueAt(i, 1);
                int score = (int) tableModel.getValueAt(i, 2);
                String timestamp = (String) tableModel.getValueAt(i, 3);
                writer.println(username + "," + score + "," + timestamp);
            }
        } catch (IOException e) {
            System.err.println("写入成绩数据时发生错误: " + e.getMessage());
        }
    }

    // 设置当前难度并刷新数据（用于切换难度时）
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        loadScores();
    }

    // 获取当前选中行的玩家名（用于删除后刷新或调试）
    public String getSelectedUsername() {
        int row = rankTable.getSelectedRow();
        if (row != -1) {
            return (String) tableModel.getValueAt(row, 1);
        }
        return null;
    }
}