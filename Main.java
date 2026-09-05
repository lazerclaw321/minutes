import java.util.ArrayList;

import javax.swing.JFrame;


public class Main {

    static final int panelWidth = 800;
    static final int panelHeight = 800;
    static final double scale = (double)panelWidth/500;

    static public Player player = new Player(100, 1);
    static public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    static public ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    static public int wave = 0;

    static public GamePanel panel = new GamePanel();
    static public WaveSpawner waves = new WaveSpawner();
    static public UpgradeManager upgrades = new UpgradeManager();
    static public boolean upgrading = false;
    
    static public boolean stopTime = false;
    static public boolean enemyStopTime = false;

    static int tempoCooldown = 2000;
    static int tempoDuration = 500;
    static int tempoCounter = 0;

    static String tempo = "";

    //tempo related
    static int baseFps = 120;
    static int fps = baseFps;
    static int playerTimeSpeed = 1;
    static int enemyTimeCounter = 0;
    static boolean noDamage = false;
    static Player backup = null;
    static Player backup2 = null;

    //bites the dust variables
    static ArrayList<int[]> damaged = new ArrayList<int[]>();
    static int timeCounter = 0;
    static int rewinded = -1; //-1 means inactive, 0 means active, 1 means damaging

    //heaven variables
    static int enemyTimeSpeed = 1;
    static int playerTimeCounter = -1;

    static long start;
    static long end;

    static String difficulty = "Minutes";

    public static void main(String[] args) {

        JFrame frame = new JFrame("Minutes");
        frame.setSize(panelWidth + frame.getInsets().left + frame.getInsets().right, panelHeight + frame.getInsets().top + frame.getInsets().bottom);
        frame.setResizable(false);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setSize(panelWidth + frame.getInsets().left + frame.getInsets().right, panelHeight + frame.getInsets().top + frame.getInsets().bottom);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.requestFocusInWindow();
        panel.setFocusable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        upgrades.initialize();
        upgrading = true;
        upgrades.upgrade(false, true);
        upgrading = true;
        upgrades.upgrade(true, false);
        upgrading = true;
        upgrades.upgrade(false, false);
        upgrading = true;
        upgrades.upgrade(false, false);
        upgrading = true;
        upgrades.upgrade(false, false);
        initiateTempo(tempo);

        while (true) {
            start = System.nanoTime();
            timeCounter++;
            enemyTimeCounter++;
            playerTimeCounter++;

            if (enemies.size() == 0 && !upgrading) {
                if (tempo == "Rail") {
                    backup = player.copy();
                }
                waves.nextWave();
            }
            if (upgrading) {
                upgrades.upgrade(false, false);
                continue;
            }

            if ((!enemyStopTime || stopTime) && (playerTimeCounter >= enemyTimeSpeed)) {
                player.runPlayer();
                playerTimeCounter = 0;
            }
            if (!stopTime && !enemyStopTime && enemyTimeCounter >= playerTimeSpeed) {
                for (int i = 0; i < projectiles.size(); i++) {
                    Projectile p = projectiles.get(i);
                    p.runProjectile();
                    if (p.health <= 0) {
                        projectiles.remove(p);
                        i--;
                    }
                }
            }
            if ((!stopTime || enemyStopTime) && enemyTimeCounter >= playerTimeSpeed) {
                enemyTimeCounter = 0;
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy e = enemies.get(i);
                    e.runEnemy();
                    if (e.health <= 0) {
                        e.death();
                        enemies.remove(e);
                        i--;
                    }
                }
            }
            runTempo(tempo);
            if (rewinded == 1) {
                for (int[] damage : damaged) {
                    if (timeCounter == damage[0]) {
                        player.health -= damage[1];
                    }
                }
            }
            end = System.nanoTime();
            if ((!enemyStopTime || (tempo == "Halt" || tempo == "Star" || tempo == "Gold")) && fps <= 2000) {
                while (end - start < 1000000000/fps) {
                    end = System.nanoTime();
                }
            }
            panel.repaint();
        }
    }

    public static void initiateTempo(String tempo) {
        
        if (tempo == "Halt") {
            tempoCooldown = 25*baseFps; //25s cooldown (20s after timestop is done)
            tempoDuration = 5*baseFps; //5s timestop
        }
         if (tempo == "Star") {
            tempoCooldown = 10*baseFps; //10s cooldown (8s after timestop is done)
            tempoDuration = 2*baseFps; //2s timestop
        }
        if (tempo == "Omit") {
            tempoCooldown = 20*baseFps; //20s cooldown (10s after skip time)
            tempoDuration = 10*baseFps; //10s time skipped
        }
        if (tempo == "Seer") {
            tempoCooldown = 5*baseFps; 
            tempoDuration = 1*baseFps; 
        }
        if (tempo == "Gold") {
            tempoCooldown = 13*baseFps;
            tempoDuration = 3*baseFps;
        }
        if (tempo == "Rush") {
            tempoCooldown = 10*baseFps;
            tempoDuration = 2*baseFps; //1 second to player
        }
        if (tempo == "Rust") {
            tempoCooldown = 20*baseFps;
            tempoDuration = 4*baseFps;
            baseFps = 360;
            fps = 360;
        }
        if (tempo == "Zion") {
            tempoCooldown = 80 * baseFps;
            tempoDuration = 40 * baseFps; // 20 seconds to player
        }
        if (tempo == "Edge") {
            tempoCooldown = 27 * baseFps;
            tempoDuration = 7 * baseFps;
        }
        if (tempo == "Cast") {
            tempoCooldown = 4*baseFps;
            tempoDuration = 1;
            backup = new Player(100, 1);
        }
        if (tempo == "Deed") {
            tempoCooldown = 12*baseFps;
            tempoDuration = 3*baseFps;
        }
        if (tempo == "Echo") {
            tempoCooldown = 15*baseFps;
            tempoDuration = 1;
        }
        if (tempo == "Rail") {
            tempoCooldown = 1000;
            tempoDuration = 1;
        }
        if (tempo == "Null") {
            tempoCooldown = baseFps;
            tempoDuration = 1;
        }
        if (tempo == "Fade") {
            tempoCooldown = 10*baseFps;
            tempoDuration = 2*baseFps;
        }
    }

    public static void runTempo(String tempo) {
        tempoCounter = Math.max(tempoCounter - 1, 0);
        if (tempo == "Halt" || tempo == "Star") {
            if (panel.keyHandler.spacePressed && tempoCounter <= 0) {
                tempoCounter = tempoCooldown;
                stopTime = true;
            }
            if (stopTime && tempoCounter <= tempoCooldown - tempoDuration ) {
                stopTime = false;
            }
        }
        if (tempo == "Wing") {
            if (panel.keyHandler.spacePressed) {
                fps = baseFps/4;
            }
            else if (!enemyStopTime && fps == baseFps/4) {
                fps = baseFps;
            }
        }
        if (tempo == "Fade") {
            tempoCounter = Math.min(tempoCounter + 2, tempoCooldown - tempoDuration);
            if (panel.keyHandler.spacePressed && tempoCounter - 1 > tempoCooldown/tempoDuration) {
                tempoCounter -= tempoCooldown / tempoDuration;
                for (Projectile p : projectiles) {
                    if (!p.playerTeam) {
                        p.moveInDirection(-60/(player.distanceFrom(p)), p.pointTowards(player.x, player.y));
                        if (player.distanceFrom(p) <= player.width + p.width) {
                            p.moveInDirection(-p.speed, p.pointTowards(player.x, player.y));
                        }
                    }
                }
                for (Enemy e : enemies) {
                    if (player.distanceFrom(e) <= player.width - 2) {
                        if (Math.random() <= 0.2) {
                            e.health--;
                        }
                    }
                    else {
                        e.moveInDirection(-60/(player.distanceFrom(e)), e.pointTowards(player.x, player.y));
                    }
                }
            }
        }
        if (tempo == "Rush") {
            tempoCounter = Math.min(tempoCounter + 2, tempoCooldown - tempoDuration);
            if (panel.keyHandler.spacePressed && tempoCounter > tempoCooldown/tempoDuration) {
                tempoCounter -= tempoCooldown / tempoDuration;
                fps = baseFps*2;
                playerTimeSpeed = 4;
            }
            else if (playerTimeSpeed == 4) {
                fps = baseFps;
                playerTimeSpeed = 1;
            }
        }
        if (tempo == "Zion") {
            if (panel.keyHandler.spacePressed && tempoCounter <= 0) {
                tempoCounter = tempoCooldown;
                fps = baseFps*2;
                playerTimeSpeed = 2;
            }
            if (tempoCounter <= tempoCooldown - tempoDuration && playerTimeSpeed == 2) {
                playerTimeSpeed = 1;
                fps = baseFps;
            }
        }
        if (tempo == "Edge") {
            if (panel.keyHandler.spacePressed && tempoCounter <= 0) {
                tempoCounter = tempoCooldown;
                playerTimeSpeed = 5;
            }
            if (tempoCounter <= tempoCooldown - tempoDuration) {
                playerTimeSpeed = 1;
            }
        }
        if (tempo == "Omit" || tempo == "Seer" || tempo == "Rule") {
            if (panel.keyHandler.spacePressed && tempoCounter <= 0) {
                tempoCounter = tempoCooldown;
                panel.keyHandler.spacePressed = false;
                noDamage = true;
            }
            if ((tempoCounter == tempoCooldown - tempoDuration || (tempoCounter < tempoCooldown - fps/2 && panel.keyHandler.spacePressed))) {
                noDamage = false;
                tempoCounter = Math.min(tempoCounter, tempoCooldown - tempoDuration);
            }
        }
        if (tempo == "Rust") {
            if (!((panel.keyHandler.aPressed||panel.keyHandler.wPressed)||(panel.keyHandler.sPressed||panel.keyHandler.dPressed))) {
                if (tempoCounter <= tempoCooldown - tempoDuration && (fps == baseFps*3 || fps == baseFps)) {
                    fps = baseFps*12;
                }
            }
            else {
                if (tempoCounter <= tempoCooldown - tempoDuration && (fps == baseFps/3 || fps == baseFps*12)) {
                    fps = baseFps;
                }
                playerTimeSpeed = 2;
            }
            if (panel.keyHandler.spacePressed && tempoCounter <= 0) {
                tempoCounter = tempoCooldown;
                fps = baseFps/3;
            }
        }
        if (tempo == "Gold") {
            while (!((panel.keyHandler.aPressed||panel.keyHandler.wPressed)||(panel.keyHandler.sPressed||panel.keyHandler.dPressed)) && !stopTime) {
                try {
                    Thread.sleep(1000/fps); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            runTempo("Halt");
        }
        if (tempo == "Cast") {
            if (panel.keyHandler.spacePressed && tempoCounter <= 0) {
                tempoCounter = tempoCooldown;
                Player temp = backup;
                backup = player;
                player = temp;
            }
        }
        if (tempo == "Deed") {
            if (panel.keyHandler.spacePressed && tempoCounter <= 0) {
                tempoCounter = tempoCooldown;
                backup = player;
                player = player.copy();
                player.health = 100;
                player.basicCounter = 0;
                player.heavyCounter = 0;
                player.defenseCounter = 0;
                player.specialCounter = 0;
            }
            if (tempoCounter <= tempoCooldown - tempoDuration) {
                if (backup != null) {
                    player = backup;
                    backup = null;
                }
            }
        }
        if (tempo == "Echo") {
            if (panel.keyHandler.spacePressed && tempoCounter <= 0) {
                tempoCounter = tempoCooldown;
                if (backup == null) {
                    backup = player.copy();
                }
                else {
                    player = backup;
                    backup = null;
                }
            }
        }
        if (tempo == "Rail") {
            tempoCounter++;
            if (panel.keyHandler.spacePressed && backup != null) {
                player = backup;
                backup = null;
                tempoCounter = 0;
                waves.wave--;
                enemies.clear();
                waves.nextWave();
            }
        }
        if (tempo == "Null") {
            if (panel.keyHandler.spacePressed && tempoCounter <= 0) {
                projectiles.clear();
                tempoCounter = tempoCooldown;
            }
        }
    }
}