package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private JComboBox<String> soundComboBox;
    private String selectedDifficulty = "EASY"; // 默认简单模式
    private boolean isSoundOn = true; // 默认开音效

    public MenuPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));

        // 难度按钮
        JButton easyBtn = new JButton("简单模式");
        JButton normalBtn = new JButton("普通模式");
        JButton hardBtn = new JButton("困难模式");

        // 音效设置
        JLabel soundLabel = new JLabel("音效");
        soundComboBox = new JComboBox<>(new String[]{"开", "关"});

        // 设置按钮大小
        Dimension btnSize = new Dimension(200, 50);
        easyBtn.setPreferredSize(btnSize);
        normalBtn.setPreferredSize(btnSize);
        hardBtn.setPreferredSize(btnSize);

        // 添加间距
        add(Box.createVerticalStrut(50));
        add(easyBtn);
        add(Box.createVerticalStrut(30));
        add(normalBtn);
        add(Box.createVerticalStrut(30));
        add(hardBtn);
        add(Box.createVerticalGlue());
        add(Box.createVerticalStrut(30));
        JPanel soundPanel = new JPanel();
        soundPanel.add(soundLabel);
        soundPanel.add(soundComboBox);
        add(soundPanel);
        add(Box.createVerticalStrut(20));

        // 为按钮添加事件
        easyBtn.addActionListener(e -> {
            selectedDifficulty = "EASY";
            switchToGame();
        });
        normalBtn.addActionListener(e -> {
            selectedDifficulty = "MEDIUM";
            switchToGame();
        });
        hardBtn.addActionListener(e -> {
            selectedDifficulty = "HARD";
            switchToGame();
        });

        // 音效状态监听
        soundComboBox.addActionListener(e -> {
            isSoundOn = "开".equals(soundComboBox.getSelectedItem());
        });
    }

    private void switchToGame() {
        // 获取主容器的 CardLayout 并切换卡片
        Container parent = getParent();
        if (parent instanceof JPanel) {
            JPanel mainPanel = (JPanel) parent;
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "game");

            // 可选：通知 Game 设置当前配置
            Component[] components = mainPanel.getComponents();
            for (Component comp : components) {
                if (comp instanceof Game) {
                    Game game = (Game) comp;
                    game.setDifficulty(selectedDifficulty);
                    game.setSoundEnabled(isSoundOn);
                    game.action(); // 或者调用 action() 启动游戏逻辑
                }
            }
        }
    }
}