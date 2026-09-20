import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class Player extends Collidable {
    
    String host = "Brawler";

    int stun;
    int maxHealth = 100;
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
    double defenseScaling = 2;

    int specialDamage = 6;
    int specialSpeed = 0;
    int specialLifetime = 30;
    int specialStun = 120;

    int moveCounter = 0;

    int defenseTimer = -1;
    boolean immune = false;

    int confusionTimer = 0;
    int ammoCounter = 0;
    int chargeCounter = 0;

    int regenTime = -1;
    int regenCounter = 0;

    public Player(int health, double speed, String host) {
        this.health = health;
        this.speed = speed;
        this.baseSpeed = speed;
        this.host = host;
        initializeAttacks();
    }

    public Player copy() {
        Player p = new Player(health, baseSpeed, host);
        p.x = x;
        p.y = y;
        for (String upgrade : upgrades) {
            p.upgrade(upgrade);
        }
        return p;
    }

    public void initializeAttacks() {
        if (host == "Sailor") {
            attacks.add("Slam");
            attacks.add("Drift");
            attacks.add("Anchor");
            attacks.add("Hook");
            
            for (int i = 0; i < 4; i++) {
                attackCounters.add(new int[2]);
            }
            attackCounters.get(3)[0] = 50;
            attackCounters.get(2)[0] = 800;
            attackCounters.get(1)[0] = 600;
            attackCounters.get(0)[0] = 1000;

            basicDamage = 10;
            basicStagger = 20;
            basicSize = width;
            basicStun = 0;

            heavyDamage = 20;
            heavySizeMult = 1.5;
            heavyStun = 60;
            heavyLifetime = 200;

            defenseDuration = 100;
            defenseScaling = 2;

            specialDamage = 6;
            specialSpeed = 0;
            specialLifetime = 30;
        }
        else if (host == "Brawler") {
            attacks.add("Smash");
            attacks.add("Block");
            attacks.add("Charge");
            attacks.add("Punch");
            
            for (int i = 0; i < 4; i++) {
                attackCounters.add(new int[2]);
            }
            attackCounters.get(3)[0] = 25;
            attackCounters.get(2)[0] = 1000000;
            attackCounters.get(1)[0] = 300;
            attackCounters.get(0)[0] = 1000000;

            speed += 0.2;
            maxHealth += 20;
            health += 20;

            basicDamage = 7;
            basicStagger = 20;
            basicSize = width;
            basicStun = 0;

            heavyDamage = 20;
            heavySizeMult = 1;
            heavyStun = 40;
            heavyLifetime = 40;

            defenseDuration = 60;
            defenseScaling = 1;

            specialDamage = 120;
            specialLifetime = 30;
            specialStun = 120;
        }
        
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
        if (host == "Sailor") {
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
                    defenseScaling += 2;
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
                    specialDamage += 2;
                    specialLifetime += 1000;
                    break;
                case "Speedboat":
                    specialSpeed += 4;
                    specialLifetime += 60;
                    attackCounters.get(0)[0] -= 200;
                    break;
                case "Yacht":
                    specialDamage += 2;
                    break;
                case "Sailboat":
                    specialDamage += 6;
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
                    break;
                case "Cutlass":
                    attacks.add("Cutlass");
                    int[] cutlassStats = {1000000, 0};
                    attackCounters.add(cutlassStats);
                    break;
                case "Flintlock":
                    attacks.set(2, "Flintlock");
                    int[] flintlockStats = {Main.baseFps/2, 0};
                    attackCounters.set(2, flintlockStats);
                    heavyLifetime += 400;
                    break;
            }
        }
        if (host == "Brawler") {
            switch (upgrade) {
                //punch upgrades
                case "Light Punch":
                    basicStagger -= 5;
                    break;
                case "Zoom Punch":
                    basicDamage += 2;
                    break;
                case "Heavy Punch":
                    basicDamage += 30;
                    break;
                case "Sucker Punch":
                    basicDamage += 1;
                    break;

                //charge upgrades
                case "Walkspeed Override":
                    heavyLifetime += 40;
                    heavySizeMult += 0.75;
                    heavyDamage += 10;
                    break;
                case "Raging Pace":
                    attackCounters.get(2)[0] = Main.baseFps*10;
                    break;
                case "Cataclysm":
                    heavyStun = Main.baseFps;
                    break;

                //block upgrades
                case "Protection":
                    defenseDuration += 30;
                    defenseScaling += 1;
                    attackCounters.get(1)[0] -= 50;
                    break;

                //smash upgrades
                case "Rage":
                    attackCounters.get(0)[0] = Main.baseFps*25;
                    specialDamage += 30;
                    break;
                case "Parry":
                    specialLifetime += 20;
                    break;
                case "Shock":
                    specialStun += 120;
                    break;

                //passives
                case "Workout":
                    speed += 0.3;
                    regenTime = 120;
                    break;
                case "Muscles":
                    basicDamage += 2;
                    heavyDamage += 5;
                    specialDamage += 30;
                    break;
                case "Fat":
                    maxHealth += 300;
                    health += 300;
                    break;

                //new moves
                case "Bandage":
                    attacks.add("Bandage");
                    int[] bandageStats = {7200, 0};
                    attackCounters.add(bandageStats);
                    maxHealth += 10;
                    health += 10;
                    break;
                case "Surge Fist":
                    attacks.add("Surge Fist");
                    int[] surgeFistStats = {1000000, 0};
                    attackCounters.add(surgeFistStats);
                    break;
                case "Defensive Stance":
                    attacks.add("Defensive Stance");
                    int[] blockStats = {300, 0};
                    attackCounters.add(blockStats);
                    break;
                case "Blood Ritual":
                    attacks.add("Ritual");
                    int[] ritualStats = {600, 0};
                    attackCounters.add(ritualStats);
                    break;

            }
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
        regenCounter++;
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
        if (immune) {
            frame = "Drift";
        }
        else {
            frame = "Idle";
        }
        

        if (defenseTimer > 0) {
            defenseTimer--;
        }
        else if (defenseTimer == 0) {
            defenseTimer--;
            speed = baseSpeed;
            immune = false;
        }
        if (regenCounter >= regenTime && regenTime >= 1) {
            health = Math.min(maxHealth, health + 1);
            regenCounter = 0;
        }
        
        if (chargeCounter > 0) {
            moveInDirection(200/45, direction);
            chargeCounter--;
            return;
        }
        else if (upgrades.contains("Void Rush") && moveCounter == 0 && chargeCounter == 0) {
            useMove("Charge");
            moveCounter = 1;
        }
        else {
            if (upgrades.contains("Void Rush")) {
                moveCounter = 0;
                chargeCounter--;
            }
            if (stun > 0) {
                stun--;
                return;
            }
            move(Main.panel.keyHandler.wPressed, Main.panel.keyHandler.aPressed, Main.panel.keyHandler.sPressed, Main.panel.keyHandler.dPressed);
        }

        //basic
        if (Main.panel.mouseHandler.leftClick) {
            checkMove(3);
        }

        //heavy
        if (Main.panel.mouseHandler.rightClick) {
            checkMove(2); 
        }

        //defense
        if (Main.panel.keyHandler.shiftPressed) {
            checkMove(1);
        }

        //special
        if (Main.panel.keyHandler.qPressed) {
            checkMove(0);
        }      
        
        //extra moves
        if (attacks.size() >= 5) {
            if (Main.panel.keyHandler.ePressed) {
                checkMove(4);
            }
        }
        if (attacks.size() >= 6) {
            if (Main.panel.keyHandler.rPressed) {
                checkMove(5);
            }
        }
        if (attacks.size() >= 7) {
            if (Main.panel.keyHandler.tPressed) {
                checkMove(6);
            }
        }
        
           
    }

    public void checkMove(int position) {
        if (attackCounters.get(position)[1] <= 0) {
            useMove(attacks.get(position));
            attackCounters.get(position)[1] = attackCounters.get(position)[0];
        }
    }

    public void useMove(String type) {
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mousePosition, Main.panel);
        mousePosition.x = (int)(mousePosition.x/Main.scale);
        mousePosition.y = (int)(mousePosition.y/Main.scale);
        //sailor
        if (type == "Hook") {
            moveCounter += 1;
            Projectile p = null;
            if (moveCounter >= 8 && upgrades.contains("Figure 8")) {
                p = new Projectile(
                    x, y, basicSize * 2, basicSize * 2, 
                    pointTowards(
                        mousePosition.x, 
                        mousePosition.y
                    ), 
                    0, 30, basicDamage * 4, true, 10, basicStun, "slash"
                );
                moveCounter = 0;
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
        else if (type == "Anchor") {    
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
        else if (type == "Drift") {
            immune = true;
            speed = speed * defenseScaling;
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
        else if (type == "Slam") {
            Projectile p = new Projectile(
                x, y, width * 2, height * 2, 
                pointTowards(
                    mousePosition.x, 
                    mousePosition.y
                ), 
                specialSpeed, specialLifetime, specialDamage, true, 15, 0, "sailorSlam"
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
        else if (type == "Cutlass") {
            Projectile p = new Projectile(
                x, y, basicSize*2, basicSize*2, 
                pointTowards(
                    mousePosition.x, 
                    mousePosition.y
                ), 
                0, 30, basicDamage*4, true, 10, basicStun, "cutlassSlash"
            );
            Main.projectiles.add(p);
            p.moveInDirection(p.width, p.direction);
            stun += basicStagger;
        }
        else if (type == "Flintlock" && ammoCounter >= 1) {    
            Projectile p = new Projectile(
                x, y, (int)(width * heavySizeMult/2), (int)(height * heavySizeMult/2), 
                pointTowards(
                    mousePosition.x, 
                    mousePosition.y
                ), 
                2, heavyLifetime/2, heavyDamage/2, true, 20, heavyStun/2, "bullet"
            );
            if (upgrades.contains("Tidal")) {
                Projectile p2 = new Projectile(
                    x, y, (int)(width * heavySizeMult/4), (int)(height * heavySizeMult/4), 
                    pointTowards(
                        mousePosition.x, 
                        mousePosition.y
                    ), 
                    1, heavyLifetime/4, heavyDamage/4, true, 20, heavyStun/4, "bullet"
                );
                Main.projectiles.add(p2);
            }
            Main.projectiles.add(p);
            stun = 20;
            ammoCounter--;
            Main.panel.attackKeys[2] = Integer.toString(Main.player.ammoCounter);
        }
        
        //brawler
        if (type == "Punch" && (!upgrades.contains("Heavy Punch") || ammoCounter >= 1)) {
            Projectile p = new Projectile(
                x, y, basicSize, basicSize, 
                pointTowards(mousePosition.x, mousePosition.y), 
                0, 30, basicDamage, true, 10, basicStun, "punch"
            );

            Main.projectiles.add(p);
            p.moveInDirection(p.width, p.direction);
            if (upgrades.contains("Zoom Punch")) {
                p.moveInDirection(p.width, p.direction);
            }
            if (upgrades.contains("Heavy Punch")) {
                ammoCounter--;
                Main.panel.attackKeys[3] = Integer.toString(ammoCounter);
            }
            stun += basicStagger;
        }
        else if (type == "Charge") {
            chargeCounter = (int)(heavyLifetime * heavySizeMult);
            for (int i = 0; i < heavyLifetime/4; i++) {
                Projectile p = new Projectile(
                    x, y, (int)(width * heavySizeMult), (int)(height * heavySizeMult), 
                    pointTowards(mousePosition.x, mousePosition.y), 
                    0, 30, heavyDamage, true, 50 + 2*i, heavyStun, "slash"
                );
                p.moveInDirection(width*heavySizeMult*i, pointTowards(mousePosition.x, mousePosition.y));
                Main.projectiles.add(p);
            }
            direction = pointTowards(mousePosition.x, mousePosition.y);
            stun = 50;
            if (upgrades.contains("Raging Pace")) {
                immune = true;
                defenseTimer = 50;
            }
            if (upgrades.contains("Cataclysm")) {
                health = Math.min(health + 5, maxHealth);
            }
        }
        else if (type == "Block" || type == "Defensive Stance") {
            Projectile p = new Projectile(
                x, y, (int)(width * defenseScaling), (int)(height * defenseScaling), 
                pointTowards(mousePosition.x, mousePosition.y), 
                0, defenseDuration, 1, true, 10, 0, "block"
            );

            Main.projectiles.add(p);
            p.moveInDirection(p.width, p.direction);
            p.direction = 0;
            stun += defenseDuration;

            if (upgrades.contains("Mending")) {
                health = Math.min(health + 2, maxHealth);
            }
            if (upgrades.contains("Unbreaking")) {
                immune = true;
                defenseTimer = defenseDuration;
            }
        }
        else if (type == "Smash") {
            Projectile p = new Projectile(
                x, y, width * 2, height * 2, 
                pointTowards(
                    mousePosition.x, 
                    mousePosition.y
                ), 
                0, specialLifetime, specialDamage, true, 15, specialStun, "smash"
            );
            Main.projectiles.add(p);
            p.moveInDirection(width, p.direction);
            p.direction = 0;
            stun += basicStagger;
        }
        else if (type == "Bandage") {
            health = Math.min(maxHealth, health + 50);
        }
        else if (type == "Surge Fist") {    
            Projectile p = new Projectile(
                x, y, (int)(width * heavySizeMult), (int)(height * heavySizeMult), 
                pointTowards(
                    mousePosition.x, 
                    mousePosition.y
                ), 
                1, 2000, heavyDamage, true, 20, 0, "blueFireball"
            );
            Main.projectiles.add(p);
            stun = 20;
        }
        else if (type == "Ritual") {
            health -= 40;
            for (int[] stats : attackCounters) {
                stats[1] = 0;
            }
        }
    }

}
