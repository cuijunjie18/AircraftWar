package edu.hitsz.application;

import edu.hitsz.Music.*;
import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.*;
import edu.hitsz.factory.*;
import edu.hitsz.data.*;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class Game extends JPanel {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    // 飞行物
    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets; // 设置敌机子弹列表
    private final List<BaseProp> props; // 设置道具列表

    private int prop_class_num = 4; // 当前道具种类数

    // 音乐
    public boolean MusicOpen = false;
    private LoopMusic background_music; // 背景音乐使用循环播放
    private String boom_music;
    private String shoot_music;
    private String get_prop_music;
    private String game_over_music;

    // 难度
    private String difficulty = "EASY"; // 默认简单难度
    public BufferedImage background_image;

    // 工厂
    EliteEnemyFactory elite_enemy_factory;
    MobEnemyFactory mob_enemy_factory;
    SuperEliteEnemyFactory super_elite_enemy_factory;
    BossEnemyFactory boss_enemy_factory;
    PropBloodFactory prop_blood_factory;
    PropBombFactory prop_bomb_factory;
    PropBulletFactory prop_bullet_factory;
    PropBulletPlusFactory prop_bullet_plus_factory;

    /**
     * 屏幕中出现的敌机最大数量
     */
    private int enemyMaxNumber = 8;
    private int boss_exist_flag = 0; // 是否已经生成Boss
    private int boss_kill_count = 0; // 已击毁Boss数量

    Random random_factory = new Random(); // 生成随机数

    /**
     * 敌机生成概率
     * 70% 生成普通敌机
     * 20% 生成精英敌机
     * 10% 生成超级精英敌机
     */
    private int EnemyRate = 10;

    /**
     * 道具生成概率
     * 30% 生成血量道具
     * 30% 生成炸弹道具
     * 30% 生成子弹道具(两种)
     * 10% 不生成道具
     */
    private int PropRate = 10;


    /**
     * 当前得分
     */
    private int score = 0;
    /**
     * 当前时刻
     */
    private int time = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;
    public ScoreDaoImpl scoreDao;
    public ScoreData scoreDate;

    public void setDifficulty(String selectedDifficulty){
        this.difficulty = selectedDifficulty;

        // 设置对应难度的背景
        switch (this.difficulty) {
            case "EASY":
                this.background_image = ImageManager.BACKGROUND_IMAGE_EASY;
                break;
            case "MEDIUM":
                this.background_image = ImageManager.BACKGROUND_IMAGE_MEDIUM;
                break;
            case "HARD":
                this.background_image = ImageManager.BACKGROUND_IMAGE_HARD;
                break;
            default:
                this.background_image = ImageManager.BACKGROUND_IMAGE_EASY; // 默认简单难度
                break;
        }
    }

    public void setSoundEnabled(boolean isSoundOn){
        this.MusicOpen = isSoundOn;
        // 是否播放声音
        if (MusicOpen) background_music.start();
    }

    public Game() {
        heroAircraft = HeroAircraft.getInstance(); // 单例模式

        // 工厂初始化
        elite_enemy_factory = new EliteEnemyFactory();
        mob_enemy_factory = new MobEnemyFactory();
        super_elite_enemy_factory = new SuperEliteEnemyFactory();
        boss_enemy_factory = new BossEnemyFactory();
        prop_blood_factory = new PropBloodFactory();
        prop_bomb_factory = new PropBombFactory();
        prop_bullet_factory = new PropBulletFactory();
        prop_bullet_plus_factory = new PropBulletPlusFactory();

        // 飞行物列表初始化
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        // 保存数据初始化
        scoreDao = new ScoreDaoImpl();
        scoreDate = new ScoreData();

        // 音效初始化
        background_music = new LoopMusic("src/videos/bgm.wav");
        boom_music = "src/videos/bomb_explosion.wav";
        shoot_music = "src/videos/bullet_hit.wav";
        get_prop_music = "src/videos/get_supply.wav";
        game_over_music = "src/videos/game_over.wav";

        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;


            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                // 新敌机产生

                if (enemyAircrafts.size() < enemyMaxNumber) {
                    final int random_num = random_factory.nextInt(EnemyRate);

                    // 根据随机数生成普通、精英敌机、超级精英敌机
                    if (random_num == 0){
                        enemyAircrafts.add(super_elite_enemy_factory.createEnemy());
                    }else if (random_num == 1 || random_num == 2){
                        enemyAircrafts.add(elite_enemy_factory.createEnemy());
                    }
                    else{
                        enemyAircrafts.add(mob_enemy_factory.createEnemy());
                    }
                }

                // 每200分产生一个Boss, 且场上只能有一个Boss
                if (score >= (boss_kill_count + 1) * 200 && score != 0 && boss_exist_flag == 0){
                    enemyAircrafts.add(boss_enemy_factory.createEnemy());
                    boss_exist_flag = 1;
                }

                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 道具移动
            propsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            //每个时刻重绘界面
            repaint();

            // 游戏结束检查英雄机是否存活
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
                gameOverFlag = true;
                System.out.println("Game Over!");
                if (MusicOpen){
                    background_music.looping = false;
                    new MusicThread(game_over_music).start();
                }

                // 切换到排行榜界面
                Container parent = getParent();
                if (parent instanceof JPanel) {
                    JPanel mainPanel = (JPanel) parent;
                    CardLayout cl = (CardLayout) mainPanel.getLayout();

                    // 如果还没有创建 RankPanel，就创建它
                    Component[] components = mainPanel.getComponents();
                    RankPanel rankPanel = null;
                    for (Component comp : components) {
                        if (comp instanceof RankPanel) {
                            rankPanel = (RankPanel) comp;
                            break;
                        }
                    }

                    if (rankPanel == null) {
                        rankPanel = new RankPanel(difficulty); // 传入当前难度
                        mainPanel.add(rankPanel, "rank");
                    }

                    // 切换到排行榜
                    cl.show(mainPanel, "rank");

                    String username = JOptionPane.showInputDialog(
                                    this,
                                    "游戏结束，你的得分为 " + score + "。\n请输入名字记录得分：",
                                    "输入",
                                    JOptionPane.QUESTION_MESSAGE);

                    // 保存分数和用户名
                    scoreDate.score = score;
                    scoreDate.username = username;
                    scoreDao.saveScoreData(scoreDate, "score.txt");
                    // scoreDao.showScoreRank("score.txt");
                    
                    // 刷新排行榜数据（因为刚保存了新分数）
                    rankPanel.loadScores();
                }
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // 敌机射击
        for (AbstractAircraft enemy : enemyAircrafts) {
            if (enemy instanceof MobEnemy) continue; // 普通敌机不射击
            enemyBullets.addAll(enemy.shoot());
        }

        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());

        // 检查英雄机的火力道具持续时间
        heroAircraft.checkShootModeDuration();
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (BaseProp prop : props){
            prop.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void generate_prop(int x,int y){
        final int random_num = random_factory.nextInt(PropRate);
        if (random_num >= 0 && random_num < 3){ // 30% 生成血量道具
            props.add(prop_blood_factory.createProp(x, y));
        } else if (random_num >= 3 && random_num < 6){ // 30% 生成炸弹道具
            props.add(prop_bomb_factory.createProp(x, y));
        } else if (random_num >= 6 && random_num < 9){ // 30% 生成子弹道具(两种)
            int bullet_type = random_factory.nextInt(2);
            if (bullet_type == 0){
                props.add(prop_bullet_factory.createProp(x, y));
            } else if (bullet_type == 1){
                props.add(prop_bullet_plus_factory.createProp(x, y));
            }
        } else{ // 10% 不生成道具
            return;
        }
    }
    private void crashCheckAction() {
        // 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets){
            if (bullet.notValid()) continue;
            if (heroAircraft.crash(bullet)){
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    if (MusicOpen) new MusicThread(shoot_music).start();
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {                        
                        // TODO 获得分数，产生道具补给
                        int prop_x = enemyAircraft.getLocationX();
                        int prop_y = enemyAircraft.getLocationY();
                        if (enemyAircraft instanceof MobEnemy){
                            score += 10;
                        }else if (enemyAircraft instanceof EliteEnemy){
                            score += 20;
                            generate_prop(prop_x, prop_y);
                        }
                        else if (enemyAircraft instanceof SuperEliteEnemy){
                            score += 30;
                            generate_prop(prop_x, prop_y);
                        }
                        if (enemyAircraft instanceof BossEnemy){ // 生成 <= 3个道具
                            score += 50;
                            generate_prop(prop_x, prop_y);
                            generate_prop((prop_x + 100) % Main.WINDOW_WIDTH, prop_y + 20);
                            generate_prop((prop_x + 200) % Main.WINDOW_WIDTH, prop_y + 50);
                            boss_exist_flag = 0; // Boss被击毁，标志复位
                            boss_kill_count += 1;
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // Todo: 我方获得道具，道具生效
        for (BaseProp prop : props){
            if (prop.notValid()) continue;
            if (heroAircraft.crash(prop)){
                prop.vanish();
                if (MusicOpen) new MusicThread(get_prop_music).start();
                if (prop instanceof PropBlood){
                    heroAircraft.increaseHp(30);
                } else if (prop instanceof PropBullet){
                    heroAircraft.changeShootMode("SCATTER");
                } else if (prop instanceof PropBulletPlus){
                    heroAircraft.changeShootMode("WAVE");
                } else if (prop instanceof PropBomb){
                    if (MusicOpen) new MusicThread(boom_music).start();
                    prop.action();
                }
            }
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(this.background_image, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(this.background_image, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        // 绘制道具
        paintImageWithPositionRevised(g, props);

        paintImageWithPositionRevised(g, enemyAircrafts);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);


        
        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }


}
