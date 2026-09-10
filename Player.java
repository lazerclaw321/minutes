import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class Player extends Collidable {
    
    String host = "Sailor";

    int stun;
    final int maxHealth = 100;
    String frame = "Idle";
    ArrayList<String> upgrades = new ArrayList<String>();
    int deathTimer = 240;

    ArrayList<String> attacks = new ArrayList<String>();
    ArrayList<int[]> attackCounters = new ArrayList<int[]>();

    int basicDamage = 10;
    int basicStagger = 20;
    int basicSize = width;
    int basicStun = 0;

    int heavyDamage = 20;
    double heavySizeMult = 1.5;
    int heavyStun = 60;
    int heavyLifetime = 200;

    int defenseDuration = 100;
    double defenseSpeedMult = 2;

    int specialScaling = 6;
    int specialSpeed = 0;
    int specialLifetime = 30;

    int figure8Counter = 0;

    int defenseTimer = -1;
    boolean immune = false;

    int confusionTimer = 0;

    public Player(int health, double speed) {
        this.health = health;
        this.speed = speed;
        this.baseSpeed = speed;
        initializeAttacks();
    }

    public Player copy() {
        Player p = new Player(health, baseSpeed);
        p.x = x;
        p.y = y;
        for (String upgrade : upgrades) {
            p.upgrade(upgrade);
        }
        return p;
    }

    public void initializeAttacks() {
        attacks.add("Basic");
        attacks.add("Heavy");
        attacks.add("Defense");
        attacks.add("Special");
        for (int i = 0; i < 4; i++) {
            attackCounters.add(new int[2]);
        }
        attackCounters.get(3)[0] = 50;
        attackCounters.get(2)[0] = 800;
        attackCounters.get(1)[0] = 600;
        attackCounters.get(0)[0] = 1000;
    }

    public void move(boolean w, boolean a, boolean s, boolean d) {
        boolean movement = true;
        if (a&&w) {
            direction = 3.14 * 5 / 4;
        }
        else if (a&&s) {
            direction = 3.14 * 3 / 4;
        }
        else if (d&&w) {
            direction = 3.14 * 7 / 4;
        }
        else if (d&&s) {
            direction = 3.14 / 4;
        }
        else if (a) {
            direction = 3.14;
        }
        else if (d) {
            direction = 0;
        }
        else if (w) {
            direction = 3.14 * 3 / 2;
        }
        else if (s) {
            direction = 3.14 / 2;
        }
        if (!((a||w)||(s||d))) {
            movement = false;
        }
        if (movement) {
            if (confusionTimer > 0) {
                moveInDirection(-speed, direction);
            }
            else {
                moveInDirection(speed, direction);
            }
        }
    }

    public void upgrade(String upgrade) {
        upgrades.add(upgrade);
        switch(upgrade) {
            //hook upgrades
            case "Slip":
                basicStagger = 0;
                basicDamage += 2;
                break;
            case "Figure 8":
                attackCounters.get(3)[0] -= 10;
                break;
            case "Bowline":
                basicDamage += 10;
                break;
            case "Hitch":
                basicSize += 5;
                basicStun += 30;
                break;

            //anchor upgrades
            case "Tsunami":
                heavyDamage += 10;
                heavyStun += 60;
                heavySizeMult += 1;
                break;
            case "Wind":
                attackCounters.get(2)[0] -= 300;
                break;
            case "Tidal":
                attackCounters.get(2)[0] -= 100;
                break;
            case "Swell":
                heavyLifetime += 1000;
                attackCounters.get(2)[0] -= 100;
                break;

            //drift upgrades
            case "Dolphin":
                defenseSpeedMult += 2;
                defenseDuration += 100;
                break;
            case "Salmon":
                attackCounters.get(1)[0] -= 50;
                break;
            case "Sardine":
                attackCounters.get(1)[0] -= 200;
                break;

            //slam upgrades
            case "Trawler":
                specialScaling += 2;
                specialLifetime += 1000;
                break;
            case "Speedboat":
                specialSpeed += 4;
                specialLifetime += 60;
                attackCounters.get(0)[0] -= 200;
                break;
            case "Yacht":
                specialScaling += 2;
                break;
            case "Sailboat":
                specialScaling += 6;
                break;

            //passives
            case "Pacific":
                width += 10;
                height += 10;
                basicSize += 10;
                speed += 0.5;
                break;

            //moves
            case "Grapple":
                attacks.add("Grapple");
                int[] grappleStats = {300, 0};
                attackCounters.add(grappleStats);
                break;
            case "Cannon":
                attacks.add("Cannon");
                int[] cannonStats = {1000000, 0};
                attackCounters.add(cannonStats);
        }
        for (String s : upgrades) {
            System.out.println(s);
        }
    }

    public void runPlayer() {
        

        for (int[] attackStats : attackCounters) {
            attackStats[1]--;
        }

        confusionTimer = Math.max(confusionTimer - 1, 0);

        if (health <= 0) {
            deathTimer--;
        }
        if (deathTimer <= 0) {
            System.exit(0);
        }
        if (deathTimer < 240) {
            if (Main.tempo == "Fade") {
                frame = "FadeDeath";
            }
            else {
                frame = "Death" + Integer.toString(5-(int)((deathTimer - 1.0)/48.0));
            }
            return;
        }
        
        if (stun > 0) {
            stun--;
            return;
        }
        if (defenseTimer > 0) {
            defenseTimer--;
        }
        else if (defenseTimer == 0) {
            defenseTimer--;
            speed = baseSpeed;
            immune = false;
        }

        if (immune) {
            frame = "Drift";
        }
        else {
            frame = "Idle";
        }

        move(Main.panel.keyHandler.wPressed, Main.panel.keyHandler.aPressed, Main.panel.keyHandler.sPressed, Main.panel.keyHandler.dPressed);

        //basic
        if (Main.panel.mouseHandler.leftClick && attackCounters.get(3)[1] <= 0) {
            useMove("Basic");
            attackCounters.get(3)[1] = attackCounters.get(3)[0]; 
        }

        //heavy
        if (Main.panel.mouseHandler.rightClick && attackCounters.get(2)[1] <= 0) {
            useMove("Heavy");
            attackCounters.get(2)[1] = attackCounters.get(2)[0]; 
        }

        //defense
        if (Main.panel.keyHandler.shiftPressed && attackCounters.get(1)[1] <= 0) {
            useMove("Defense");
            attackCounters.get(1)[1] = attackCounters.get(1)[0]; 
        }

        //special
        if (Main.panel.keyHandler.qPressed && attackCounters.get(0)[1] <= 0) {
            useMove("Special");
            attackCounters.get(0)[1] = attackCounters.get(0)[0];
        }      
        
        if (attacks.size() >= 5) {
            if (Main.panel.keyHandler.ePressed && attackCounters.get(4)[1] <= 0) {
                useMove(attacks.get(4));
                attackCounters.get(4)[1] = attackCounters.get(4)[0];
            }
        }
        if (attacks.size() >= 6) {
            if (Main.panel.keyHandler.rPressed && attackCounters.get(5)[1] <= 0) {
                useMove(attacks.get(5));
                attackCounters.get(5)[1] = attackCounters.get(5)[0];
            }
        }
        if (attacks.size() >= 7) {
            if (Main.panel.keyHandler.tPressed && attackCounters.get(6)[1] <= 0) {
                useMove(attacks.get(6));
                attackCounters.get(6)[1] = attackCounters.get(6)[0];
            }
        }
           
    }

    public void useMove(String type) {
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mousePosition, Main.panel);
        mousePosition.x = (int)(mousePosition.x/Main.scale);
        mousePosition.y = (int)(mousePosition.y/Main.scale);
        if (type == "Basic") {
            figure8Counter += 1;
            Projectile p = null;
            if (figure8Counter >= 8 && upgrades.contains("Figure 8")) {
                p = new Projectile(
                    x, y, basicSize * 2, basicSize * 2, 
                    pointTowards(
                        mousePosition.x, 
                        mousePosition.y
                    ), 
                    0, 30, basicDamage * 4, true, 10, basicStun, "slash"
                );
                figure8Counter = 0;
            }
            else {
                p = new Projectile(
                    x, y, basicSize, basicSize, 
                    pointTowards(
                        mousePosition.x, 
                        mousePosition.y
                    ), 
                    0, 30, basicDamage, true, 10, basicStun, "slash"
                );
            }
            if (upgrades.contains("Bowline")) {
                health--;
            }
            Main.projectiles.add(p);
            p.moveInDirection(p.width, p.direction);
            stun += basicStagger;
        }
        else if (type == "Heavy") {    
            Projectile p = new Projectile(
                x, y, (int)(width * heavySizeMult), (int)(height * heavySizeMult), 
                pointTowards(
                    mousePosition.x, 
                    mousePosition.y
                ), 
                1, heavyLifetime, heavyDamage, true, 0, heavyStun, "anchor"
            );
            if (upgrades.contains("Tidal")) {
                Projectile p2 = new Projectile(
                    x, y, (int)(width * heavySizeMult/2), (int)(height * heavySizeMult/2), 
                    pointTowards(
                        mousePosition.x, 
                        mousePosition.y
                    ), 
                    0.5, heavyLifetime/2, heavyDamage/2, true, 20, heavyStun/2, "anchor"
                );
                Main.projectiles.add(p2);
            }
            Main.projectiles.add(p);
            stun = 20;
        }
        else if (type == "Defense") {
            immune = true;
            speed = speed * defenseSpeedMult;
            defenseTimer = defenseDuration;
            if (upgrades.contains("Salmon")) {
                attackCounters.get(1)[1] = 0; 
            }
            if (upgrades.contains("Sardine")) {
                health = Math.min(maxHealth, health + 3);
            }
            if (upgrades.contains("Piranha")) {
                Projectile p = new Projectile(
                    x, y, (int)(width * heavySizeMult/2), (int)(height * heavySizeMult/2), 
                    pointTowards(
                        mousePosition.x, 
                        mousePosition.y
                    ), 
                    0.5, heavyLifetime/2, heavyDamage/2, true, 0, heavyStun/2, "anchor"
                );
                Main.projectiles.add(p);
            }
        }
        else if (type == "Special") {
            Projectile p = new Projectile(
                x, y, width * 2, height * 2, 
                pointTowards(
                    mousePosition.x, 
                    mousePosition.y
                ), 
                specialSpeed, specialLifetime, specialScaling, true, 15, 0, "sailorSlam"
            );
            Main.projectiles.add(p);
            p.moveInDirection(width, p.direction);
            if (!upgrades.contains("Speedboat")) {
                p.direction = 0;
            }
            stun += 20;
            if (upgrades.contains("Yacht")) {
                attackCounters.get(1)[1] = 0;
            }
        }
        else if (type == "Grapple") {
            Projectile p = new Projectile(
                x, y, basicSize, basicSize, 
                pointTowards(
                    mousePosition.x, 
                    mousePosition.y
                ), 
                4, heavyLifetime, basicDamage, true, 0, heavyStun, "grapple"
            );
            Main.projectiles.add(p);
        }
        else if (type == "Cannon") {
            Projectile p = new Projectile(
                x, y, (int)(width * heavySizeMult), (int)(height * heavySizeMult), 
                pointTowards(
                    mousePosition.x, 
                    mousePosition.y
                ), 
                1, heavyLifetime, heavyDamage * 4, true, 0, heavyStun, "cannonball"
            );
            Main.projectiles.add(p);
        }
    }

}
