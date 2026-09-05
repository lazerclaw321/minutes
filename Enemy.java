import java.util.Random;

public class Enemy extends Collidable {
    
    String type;
    int cooldown;
    String frame = "Idle";

    //these counters are used for different things depending on the enemy
    int counter = 0;
    int counter2 = 0;
    int counter3 = 0;
    int counter4 = 0;
    double[] targetPosition = {0, 0};
    
    int stun = 0;
    int sinking = 0;
    int maxHealth;
    
    Random random = new Random();
    boolean moveToPlayer = true;
    boolean immune = false;

    double targetX, targetY;

    public void death() {
        if (type == "doppio") {
            Main.enemies.add(new Enemy((int)x, (int)y, width + 3, height + 3, baseSpeed, maxHealth + 100, "diavolo", cooldown));
        }
        if (type == "dio") {
            Main.enemies.add(new Enemy((int)x, (int)y, width, height, baseSpeed, maxHealth, "dio2", cooldown));
        }
        if (type == "kira" && Main.rewinded == 0) {
            Main.rewinded = 1;
            Main.player = Main.backup2;
            health = 0;
            Main.waves.wave--;
            Main.waves.nextWave();
            Main.projectiles.clear();
        }
        if (type == "whitesnake") {
            Main.enemies.add(new Enemy((int)x, (int)y, width, height, baseSpeed, maxHealth, "cmoon", cooldown));
        }
        if (type == "cmoon") {
            Main.enemies.add(new Enemy((int)x, (int)y, width, height, baseSpeed, maxHealth*2, "heaven", cooldown));
        }
    }

    public void runEnemy() {
        if (Main.player.upgrades.contains("Atlantic")) {
            speed = Math.max(0.05, baseSpeed - 0.05 * sinking);
        }
        counter--;
        if (Main.tempo != "Omit" || Main.tempoCounter <= Main.tempoCooldown - Main.tempoDuration) {
            targetX = Main.player.x;
            targetY = Main.player.y;
        }
        stun = Math.max(stun - 1, 0);
        if (Main.difficulty == "Seconds") {
            stun = Math.max(stun - 1, 0);
        }

        if (stun > 0) {
            return;
        }

        direction = pointTowards(targetX, targetY);
        if (moveToPlayer) {
            moveInDirection(speed, direction);
        }
        if (type == "basic" && counter <= 0 && distanceFrom(Main.player) <= width * 2) {
            Projectile p = new Projectile(
                x, y, width, height, 
                pointTowards(
                    targetX, 
                    targetY
                ), 
                0, 30, 10, false, 20, 0, "slash"
            );
            Main.projectiles.add(p);
            p.moveInDirection(width, p.direction);
            counter = cooldown;
            stun += 30;
        }
        if (type == "ranged" && counter <= 0) {
            counter2 += 1;
            Projectile p = new Projectile(
                x, y, 10, 10, 
                pointTowards(
                    targetX, 
                    targetY
                ), 
                2, 300, 10, false, 10, 0, "bullet"
            );
            Main.projectiles.add(p);
            counter = cooldown;
            if (counter2 == 6) {
                stun = 150;
                counter2 = 0;
            }
        }
        if (type == "shotgunner" && counter <= 0 && distanceFrom(Main.player) <= 75) {
            counter2 += 1;
            Main.projectiles.add(new Projectile(
                x, y, 10, 10, 
                pointTowards(
                    targetX, 
                    targetY
                ), 
                2.5, 250, 5, false, 50, 0, "bullet"
            ));
            Main.projectiles.add(new Projectile(
                x, y, 10, 10, 
                pointTowards(
                    targetX, 
                    targetY
                ) + 0.5, 
                2.5, 250, 5, false, 50, 0, "bullet"
            ));
            Main.projectiles.add(new Projectile(
                x, y, 10, 10, 
                pointTowards(
                    targetX, 
                    targetY
                ) - 0.5, 
                2.5, 250, 5, false, 50, 0, "bullet"
            ));
            counter = cooldown;
            stun += 60;
            if (counter2 == 6) {
                stun += 150;
                counter2 = 0;
            }
        }
        if (type == "miniboss" && counter <= 0) {
            if (distanceFrom(Main.player) <= width * 4) {
                Projectile p = new Projectile(
                    x - width * 1.5, y - width * 1.5, width * 4, height * 4, 0, 
                    0, 30, 25, false, 60, cooldown - 120, "smash"
                );
                stun = 60;
                counter = cooldown;
                counter2 += 40;
                Main.projectiles.add(p);
            }
            else {
                Projectile p = new Projectile(
                    x, y, width / 2, height / 2, 
                    pointTowards(
                        targetX, 
                        targetY
                    ), 
                    3, 300, 5, false, 0, 0, "bullet"
                );
                counter = cooldown / 20;
                counter2 += 1;
                Main.projectiles.add(p);
            }
            if (counter2 >= 200) {
                stun = 400;
                counter2 = 0;
                //health = Math.min(maxHealth, health + maxHealth / 4);
                Main.enemies.add(new Enemy((int)x, (int)y, width / 2, height / 2, 1.5, 20, "basic", 50));
            }
        }
        if (type == "dio") {
            counter2--;
            if (counter2 >= 0) {
                moveToPlayer = false;
                moveInDirection(speed, targetPosition[0]);
                speed = 200/45;
            } 
            else {
                moveToPlayer = true;
                speed = 0.5;
                frame = "Idle";
            }
            if (counter <= 0) {
                if (distanceFrom(Main.player) <= width * 1.5) {
                    Projectile p = new Projectile(
                        x, y, (int)(width * 1.5), (int)(height * 1.5), 
                        pointTowards(
                            targetX, 
                            targetY
                        ) + random.nextDouble() * 2 - 1, 
                        0, 10, 15, false, 40, cooldown - 60, "slash"
                    );
                    Main.projectiles.add(p);
                    p.moveInDirection(width, p.direction);
                    counter = cooldown;
                    stun = 40;
                }
                else {
                    if (Math.random() > 0.5) {
                        targetPosition[0] = pointTowards(targetX, targetY);
                        counter2 = 40;
                        for (int i = 0; i < 10; i++) {
                            Projectile p = new Projectile(
                                x, y, 20, 20, 
                                pointTowards(
                                    targetX, 
                                    targetY
                                ), 
                                0, 12, 10, false, 50 + 2*i, 0, "slash"
                            );
                            p.moveInDirection(20*i, pointTowards(targetX, targetY));
                            counter = cooldown;
                            Main.projectiles.add(p);
                        }
                        stun = 50;
                    }
                    else {
                        double direction = pointTowards(targetX, targetY) + Math.random() - 0.5;
                        for (int i = 0; i < 20; i++) {
                            Projectile p = new Projectile(
                                x, y, 10, 10, 
                                direction, 
                                8, 500, 2, false, 75 + i, 0, "bullet"
                            );
                            if (i % 2 == 0) {
                                p.moveInDirection(width / 2, direction + 3.14/2);
                            }
                            Main.projectiles.add(p);
                            counter = cooldown;
                            frame = "Laser";
                            stun = 75;
                        }
                    }
                }
                counter3++;
                if (counter3 >= 6 && counter2 <= 0) {
                    stun = 480;
                    counter3 = 0;
                }
            }
        }
        if (type == "dio2" && counter <= 0) {                       
            if (distanceFrom(Main.player) <= width * 1.5) {
                counter2++; 
                Projectile p = new Projectile(
                    x, y, width, height, 
                    pointTowards(
                        targetX, 
                        targetY
                    ) + random.nextDouble() * 2 - 1, 
                    0, 10, 2, false, 10, 0, "slash"
                );
                Main.projectiles.add(p);
                p.moveInDirection(width, p.direction);
                counter = cooldown/20;
                stun = 0;
            }
            else {
                counter2 += 10;
                if (Math.random() > 0.5 || Main.enemyStopTime) {
                    for (int i = -2; i < 3; i++) {
                        Main.projectiles.add(new Projectile(
                            x, y, 16, 16, 
                            pointTowards(
                                targetX, 
                                targetY
                            ) + 0.2 * i, 
                            2.5, 500, 5, false, 50, 0, "knife"
                        ));
                        counter = cooldown;
                    }
                    stun = 50;
                }
                else {
                    for (int i = 0; i < 10; i++) {
                        Projectile p = new Projectile(
                            Math.random() * Main.panelWidth, Math.random() * Main.panelHeight, 16, 16, 
                            Math.random() * 6.28, 
                            1.5, 500, 5, false, 75 + i, 0, "knife"
                        );
                        p.direction = p.pointTowards(targetX, targetY);
                        Main.projectiles.add(p);
                        counter = cooldown;
                        stun = 100;
                    }
                }
            }
            if (counter2 >= 20 && counter2 < 60) {
                Main.enemyStopTime = false;
                if (Main.tempo != "Halt" && Main.tempo != "Star" && Main.tempo != "Gold") {
                    Main.fps = 120;
                }
            }
            if (counter2 >= 9999) {
                frame = "Idle";
                Main.enemyStopTime = true;
                counter2 = 0;
            }
            if (counter2 >= 60) {
                frame = "TimeStop";
                stun = 40;   
                counter = 0;
                counter2 = 9999;
            }
            
        }
        if (type == "archer" && counter <= 0) {
            Projectile p = new Projectile(
                Main.player.x - Main.player.width/2 - width * 2, Main.player.y - Main.player.width/2 - height * 2, width * 4, height * 4, 
                0, 
                0, 30, 10, false, 130, 0, "beam"
            );
            Main.projectiles.add(p);
            p.moveInDirection(width, p.direction);
            counter = cooldown + random.nextInt(100) - 50;
            stun += 130;
        }
        if (type == "doppio") {
            moveToPlayer = false;
            counter2--;
            if (counter2 >= 0) {
                moveInDirection(speed, targetPosition[0]);
                speed = 200/40;
            } 
            else {
                speed = baseSpeed;
                moveInDirection(speed, direction);
                //strafing
                for (Projectile p : Main.projectiles) {
                    if (p.playerTeam) {
                        if (collision(p)) {
                            moveInDirection(-speed, direction);
                        }
                        p.moveInDirection(p.speed * 10, p.direction);
                        if (collision(p)) {
                            moveInDirection(6, p.direction + 3.14/2);
                        }
                        p.moveInDirection(p.speed * -10, p.direction);
                    }
                }
            }
            if (counter <= 0) {
                if (Math.random() < 0.3) {
                    for (int i = 0; i < 16; i++) {
                        Main.projectiles.add(new Projectile(
                            x, y, 10, 10, 
                            pointTowards(
                                targetX, 
                                targetY
                            ) + 3.14/8 * i, 
                            2.5, 500, 5, false, 50, 0, "bullet"
                        ));
                        counter = cooldown;
                        stun = 50;
                    }
                }
                else if (Math.random() > 0.5) {
                    targetPosition[0] = pointTowards(targetX, targetY);
                    counter2 = 80;
                    for (int i = 0; i < 20; i++) {
                        Projectile p = new Projectile(
                            x, y, 20, 20, 
                            pointTowards(
                                targetX, 
                                targetY
                            ), 
                            0, 12, 10, false, 50 + 2*i, 0, "slash"
                        );
                        p.moveInDirection(20*i, pointTowards(targetX, targetY));
                        counter = cooldown;
                        Main.projectiles.add(p);
                    }
                    stun = 50;
                }
                else {
                    double direction = pointTowards(targetX, targetY) + Math.random() - 0.5;
                    Projectile p = new Projectile(
                        x, y, 40, 40, 
                        direction, 
                        2, 500, 50, false, 150, 0, "bullet"
                    );
                    p.moveInDirection(width, direction);
                    Main.projectiles.add(p);
                    counter = cooldown;
                    stun = 150;
                }
            }
        }
        if (type == "diavolo") {
            counter3--;
            if (counter3 == 360) {
                Main.noDamage = false;
                Main.fps = Main.baseFps;
            }
            if (counter <= 0) {
                if (distanceFrom(Main.player) <= width * 1.5) {
                    counter2++; 
                    Projectile p = new Projectile(
                        x, y, width, height, 
                        pointTowards(
                            targetX, 
                            targetY
                        ) + random.nextDouble() * 2 - 1, 
                        0, 10, 5, false, 10, 0, "slash"
                    );
                    Main.projectiles.add(p);
                    p.moveInDirection(width, p.direction);
                    counter = cooldown/20;
                }
                else {
                    counter2 += 10;
                    if (Math.random() > 0.5) {
                        if (Math.random() > 0.5) {
                            for (int i = 0; i < Main.panelWidth/100; i++) {
                                Main.projectiles.add(new Projectile(
                                    i * 100, 0, 15, Main.panelHeight, 
                                    0, 
                                    0, 50, 15, false, 50, 0, "beam"
                                ));
                                counter = cooldown;
                            }
                        }
                        else {
                            for (int i = 0; i < Main.panelHeight/100; i++) {
                                Main.projectiles.add(new Projectile(
                                    0, i * 100, Main.panelWidth, 15, 
                                    0, 
                                    0, 50, 15, false, 50, 0, "beam"
                                ));
                                counter = cooldown;
                            }
                        }
                    }
                    else {
                        for (int j = -1; j < 2; j++) {
                            direction = pointTowards(targetX, targetY) + j * 0.5;
                            for (int i = 0; i < 20; i++) {
                                Projectile p = new Projectile(
                                    x, y, 20, 20, 
                                    direction, 
                                    0, 12, 10, false, 150 + 2*i, 0, "bullet"
                                );
                                p.moveInDirection(20*i, direction);
                                counter = cooldown;
                                Main.projectiles.add(p);
                            }
                        }
                        stun = 150;
                    }
                }
            }
        }
        if (type == "kira") {
            moveToPlayer = false;
            counter2--;
            if (counter2 >= 0) {
                moveInDirection(speed, targetPosition[0]);
                speed = 200/40;
            } 
            else {
                speed = baseSpeed;
                moveInDirection(speed, direction);
                frame = "Idle";
            }
            if (counter <= 0) {
                if (counter3 >= -2) {
                    for (int i = 0; i < 40; i++) {
                        Projectile p = new Projectile(
                            Math.random() * Main.panelWidth, Math.random() * Main.panelHeight, 30, 30, 
                            0, 
                            0, 10000, 10, false, 75 + i, 0, "beam"
                        );
                        Main.projectiles.add(p);
                        counter = cooldown / 3;
                    }
                    counter3--;
                }
                else if (Math.random() > 0.5) {
                    for (int i = 0; i < 16; i++) {
                        Main.projectiles.add(new Projectile(
                            x, y, 15, 15, 
                            pointTowards(
                                targetX, 
                                targetY
                            ) + 3.14/8 * i, 
                            0.5, 1500, 5, false, 50, 0, "bullet"
                        ));
                        counter = cooldown;
                        stun = 50;
                    }
                    frame = "Attack";
                }
                else {
                    for (int i = 0; i < 4; i++) {
                        double direction = pointTowards(targetX, targetY) + Math.random() - 0.5;
                        Projectile p = new Projectile(
                            x, y, 40, 40, 
                            direction, 
                            0.5, 500, 50, false, 100, 0, "bullet"
                        );
                        p.moveInDirection(width, direction);
                        Main.projectiles.add(p);
                        counter = cooldown;
                        stun = 100;
                    }
                    frame = "Attack";
                }
            }
        }
        if (type == "whitesnake") {
            moveToPlayer = false;
            counter2--;
            if (counter2 >= 0) {
                moveInDirection(speed, targetPosition[0]);
                speed = 200/40;
            } 
            else {
                speed = baseSpeed;
                moveInDirection(speed, direction);
                //strafing
                for (Projectile p : Main.projectiles) {
                    if (p.playerTeam) {
                        if (collision(p)) {
                            moveInDirection(-speed, direction);
                        }
                        p.moveInDirection(p.speed * 3, p.direction);
                        if (collision(p)) {
                            moveInDirection(1, p.direction + 3.14/2);
                        }
                        p.moveInDirection(p.speed * -3, p.direction);
                    }
                }
            }
            if (counter <= 0) {
                if (Math.random() < 0.3) {
                    for (int i = 0; i < 3; i++) {
                        Projectile p = new Projectile(
                            x, y, 20, 20, 
                            pointTowards(
                                targetX, 
                                targetY
                            ) + Math.random() - 0.5, 
                            1.5, 2000, 10, false, 90 + 10 * i, 0, "bulletbouncy"
                        );
                        p.moveInDirection(15, p.direction);
                        Main.projectiles.add(p);
                        counter = cooldown;
                    }
                    stun = 120;
                }
                else if (Math.random() > 0.5) {
                    Enemy ranged = new Enemy((int)(x - 50), (int)(y - 50), 15, 15, 0.2, 20, "ranged", 150);
                    ranged.stun = 300;
                    Main.enemies.add(ranged);
                    Projectile p = new Projectile(
                        x, y, 10, 10, 
                        pointTowards(
                            targetX, 
                            targetY
                        ) + Math.random() - 0.5, 
                        5.5, 500, 10, false, 90, 0, "bulletbouncy"
                    );
                    p.moveInDirection(15, p.direction);
                    Main.projectiles.add(p);
                    counter = cooldown;
                    
                }
                else {
                    Projectile p = new Projectile(
                        0, 0, 500, 500, 
                        direction, 
                        2, 3, 1, false, 100, 0, "smog"
                    );
                    Main.projectiles.add(p);
                    counter = cooldown;
                    stun = 150;
                }
            }
        }
        if (type == "cmoon") {
            Main.player.moveInDirection(-50/distanceFrom(Main.player), Main.player.pointTowards(x, y));
            if (distanceFrom(Main.player) <= 2) {
                Main.player.health -= 100;
            }
            for (Projectile p : Main.projectiles) {
                p.moveInDirection(-50/distanceFrom(p), p.pointTowards(x, y));
            }
            if (counter <= 0) {
                counter2++;
                if (Math.random() <= 0.3) {
                    for (int i = 0; i < Main.panelWidth/100; i++) {
                        Main.projectiles.add(new Projectile(
                            i * 100, 0, 5, Main.panelHeight, 
                            0, 
                            0, 50, 10, false, 50, 0, "beam"
                        ));
                    }
                    for (int i = 0; i < Main.panelHeight/100; i++) {
                        Main.projectiles.add(new Projectile(
                            0, i * 100, Main.panelWidth, 5, 
                            0, 
                            0, 50, 10, false, 50, 0, "beam"
                        ));
                        counter = cooldown;
                    }
                }
                else if (Math.random() <= 0.5) {
                    for (int i = 0; i < 16; i++) {
                        Main.projectiles.add(new Projectile(
                            x, y, 10, 10, 
                            pointTowards(
                                targetX, 
                                targetY
                            ) + 3.14/8 * i, 
                            2.5, 500, 5, false, 50, 0, "bullet"
                        ));
                        counter = cooldown;
                    }
                }
                else {
                    for (int i = 0; i < 3; i++) {
                        Projectile p = new Projectile(
                            x, y, 20, 20, 
                            pointTowards(
                                targetX, 
                                targetY
                            ) + Math.random() - 0.5, 
                            1.5, 2000, 10, false, 90 + 10 * i, 0, "bulletbouncy"
                        );
                        p.moveInDirection(15, p.direction);
                        Main.projectiles.add(p);
                        counter = cooldown;
                    }
                }
            }
            if (counter2 == 6) {
                counter2 = 0;
                stun = Main.baseFps * 4;
            }
        }
        if (type == "heaven") {
            
            moveToPlayer = false;
            counter2--;
            if (counter2 >= 0) {
                moveInDirection(speed, targetPosition[0]);
                speed = 200/100;
            } 
            else {
                if (distanceFrom(Main.player) >= 50) {
                    moveInDirection(speed, direction);
                }
                else {
                    moveInDirection(speed, direction - 3.14/2);
                }
            }

            if (counter <= 0) {
                if (Math.random() < 0.3) {
                    for (int i = 0; i < 3; i++) {
                        Projectile p = new Projectile(
                            x, y, 20, 20, 
                            pointTowards(
                                targetX, 
                                targetY
                            ) + Math.random() - 0.5, 
                            0.5, 2000, 10, false, 90 + 10 * i, 0, "bullet"
                        );
                        p.moveInDirection(15, p.direction);
                        Main.projectiles.add(p);
                        counter = cooldown;
                    }
                    stun = 120;
                }
                else if (Math.random() > 0.5) {
                    for (int i = 0; i < 50; i++) {
                        Projectile p = new Projectile(
                            x, y, 10, 10, 
                            ((double)i/5), 
                            0.5, 250, 5, false, 90 + 5*i, 0, "bullet"
                        );
                        p.moveInDirection(15, p.direction);
                        Main.projectiles.add(p);
                        counter = cooldown;
                    }
                    stun = 90;
                }
                else {
                    targetPosition[0] = pointTowards(targetX, targetY);
                    counter2 = 250;
                    for (int i = 0; i < 20; i++) {
                        Projectile p = new Projectile(
                            x, y, 20, 20, 
                            pointTowards(
                                targetX, 
                                targetY
                            ), 
                            0, 12, 5, false, 50 + 10*i, 0, "slash"
                        );
                        p.moveInDirection(20*i, pointTowards(targetX, targetY));
                        counter = cooldown;
                        Main.projectiles.add(p);
                    }
                    stun = 50;
                }
                counter4++;
                if (counter4 == 6) {
                    stun = 480;
                    counter4 = 0;
                }
            }

            counter3++;
            if (counter3 >= Main.fps/40) {
                Main.baseFps++;
                Main.fps++;
                counter3 = 0;
                System.out.println(Main.fps + " " + Main.enemyTimeSpeed);
            }
            if (Main.fps >= 3000) {
                Main.fps = 9999;
            }
        }
    }

    public Enemy(int x, int y, int width, int height, double speed, int health, String type, int cooldown) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.baseSpeed = speed;
        this.speed = speed;
        this.maxHealth = health;
        this.health = health;
        this.type = type;
        this.cooldown = cooldown;
        this.stun = 100;
        if (Main.difficulty == "Hours") {
            this.speed = this.speed/2;
            this.baseSpeed = this.baseSpeed/2;
        }
        if (Main.difficulty == "Seconds") {
            this.maxHealth *= 2;
            this.health *= 2;
        }
        if (Main.player.upgrades.contains("Arctic")) {
            this.sinking = 1;
        }
    }
}
