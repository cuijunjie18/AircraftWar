package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 程序入口
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = ImageManager.BACKGROUND_IMAGE_EASY.getWidth();
    public static final int WINDOW_HEIGHT = ImageManager.BACKGROUND_IMAGE_EASY.getHeight();

    public static void main(String[] args) {

        System.out.println("Hello Aircraft War");

        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("Aircraft War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        //设置窗口的大小和位置,居中放置
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 使用 CardLayout 管理菜单和游戏面板
        JPanel mainPanel = new JPanel(new CardLayout());
        MenuPanel menuPanel = new MenuPanel();
        Game gamePanel = new Game();

        mainPanel.add(menuPanel, "menu");
        mainPanel.add(gamePanel, "game");

        frame.add(mainPanel);
        frame.setVisible(true);

        // Game game = new Game();
        // frame.add(game);
        // frame.setVisible(true);
        // game.action();
    }
}
