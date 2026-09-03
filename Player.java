import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Window;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

public class Player extends Collidable {
    
    String host = "Sailor";

    int basicCooldown = 50;
    int heavyCooldown = 800;
    int defenseCooldown = 600;
    int specialCooldown = 1000;

    int basicDamage = 10;
    int basicStagger = 20;
    int basicSize = width;
    int basicStun = 0;

    int heavyDamage = 20;
    double heavySizeMult = 1.5;
    int heavyStun = 60;

    int defenseDuration = 100;
    double defenseSpeedMult = 2;

    int specialScaling = 6;
    int specialSpeed = 0;
    int specialLifetime = 30;
    
    int basicCounter = 0;
    int heavyCounter = 0;
    int defenseCounter = 0;
    int specialCounter = 0;

    int figure8Counter = 0;

    int stun;
    final int maxHealth = 100;

    int defenseTimer = -1;
    boolean immune = false;

    String frame = "Idle";

    ArrayList<String> upgrades = new ArrayList<String>();

    int deathTimer = 240;

    int confusionTimer = 0;

    public Player(int health, double speed) {
        this.health = health;
        this.speed = speed;
        this.baseSpeed = speed;
    }

    public Player copy() {
        Player p = new Player(health, baseSpeed);
        p.x = x;
        p.y = y;
        p.basicCounter = basicCounter;
        p.defenseCounter = defenseCounter;
        p.heavyCounter = heavyCounter;
        p.specialCounter = specialCounter;
        return p;
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
                basicCooldown -= 10;
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
                heavyCooldown -= 300;
                break;

            //drift upgrades
            case "Dolphin":
                defenseSpeedMult += 2;
                defenseDuration += 100;
                break;
            case "Salmon":
                defenseCooldown -= 50;
                break;
            case "Sardine":
                defenseCooldown -= 200;
                break;

            //slam upgrades
            case "Trawler":
                specialScaling += 2;
                specialLifetime += 1000;
                break;
            case "Speedboat":
                specialSpeed += 4;
                specialLifetime += 60;
                specialCooldown -= 200;
                break;
            case "Yacht":
                specialScaling += 2;
                break;
            case "Sailboat":
                specialScaling += 6;
                break;

            //passives
            case "Arctic":
                //does nothing to buff abillities
                break;
            case "Atlantic":
                break;
        }
    }

    public void runPlayer() {
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mousePosition, Main.panel);
        mousePosition.x = (int)(mousePosition.x/Main.scale);
        mousePosition.y = (int)(mousePosition.y/Main.scale);

        basicCounter--;
        heavyCounter--;
        defenseCounter--;
        specialCounter--;        

        confusionTimer = Math.max(confusionTimer - 1, 0);
        System.out.println(confusionTimer);

        if (health <= 0) {
            deathTimer--;
        }
        if (deathTimer <= 0) {
            System.exit(0);
        }
        if (deathTimer < 240) {
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
        if (Main.panel.mouseHandler.leftClick && basicCounter <= 0) {
            basicCounter = basicCooldown; 
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

        //heavy
        if (Main.panel.mouseHandler.rightClick && heavyCounter <= 0) {
            heavyCounter = heavyCooldown; 
            Projectile p = new Projectile(
                x, y, (int)(width * heavySizeMult), (int)(height * heavySizeMult), 
                pointTowards(
                    mousePosition.x, 
                    mousePosition.y
                ), 
                1, 200, heavyDamage, true, 0, heavyStun, "anchor"
            );
            Main.projectiles.add(p);
        }

        //defense
        if (Main.panel.keyHandler.shiftPressed && defenseCounter <= 0) {
            defenseCounter = defenseCooldown; 
            immune = true;
            speed = speed * defenseSpeedMult;
            defenseTimer = defenseDuration;
            if (upgrades.contains("Salmon")) {
                heavyCounter = 0;
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
                    0.5, 100, heavyDamage/2, true, 0, heavyStun/2, "anchor"
                );
                Main.projectiles.add(p);
            }
        }

        //special
        if (Main.panel.keyHandler.qPressed && specialCounter <= 0) {
            specialCounter = specialCooldown; 
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
            p.direction = 0;
            stun += 20;
            if (upgrades.contains("Yacht")) {
                defenseCounter = 0;
            }
        }        
    }
}
