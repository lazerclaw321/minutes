public class Projectile extends Collidable {

    boolean playerTeam = false;
    int lifetime;
    int maxLifetime;
    int delay;
    int stun;
    boolean area = false;
    String id;
    Player player;

    public Projectile(double x, double y, int width, int height, double direction, double speed, int lifetime, int health, boolean team, int delay, int stun, String id) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.baseWidth = width;
        this.height = height;
        this.baseHeight = height;
        this.direction = direction;
        this.speed = speed;
        this.playerTeam = team;
        this.maxLifetime = lifetime;
        this.lifetime = lifetime;
        this.health = health;
        this.delay = delay;
        this.stun = stun;
        this.id = id;
        this.player = Main.player;
        if (!this.playerTeam && Main.difficulty == "Hours") {
            this.speed = this.speed/2;
        }
    }

    public void runProjectile() {
        delay--;
        if (delay > 0) {
            return;
        }
        if (speed > 0) {
            if (id == "bulletbouncy" || ((id == "anchor" || (id == "bullet" && playerTeam)) && player.upgrades.contains("Swell"))) {
                if (x == 0 || x == 0 || x == Main.panelWidth/Main.scale - width) {
                    System.out.println(direction);
                    direction = 3.14-direction;
                }
                else if (y == 0 || y == Main.panelWidth/Main.scale - height) {
                    direction = -direction;
                }
            }
            else if (y == 0 || y == Main.panelWidth/Main.scale - height || x == 0 || x == Main.panelWidth/Main.scale - width){
                health = 0;
            }
        }
        moveInDirection(speed, direction);
        lifetime--;
        if (lifetime <= 0) {
            health = 0;
        }
        if (id == "cutlassSlash" || id == "block" || (id == "smash" && player.upgrades.contains("Parry"))) {
            for (Projectile p : Main.projectiles) {
                if (p.playerTeam != playerTeam && collision(p) && p.delay <= 10) {
                    if (id == "smash" && player.upgrades.contains("Parry")) {
                        p.playerTeam = playerTeam;
                        p.speed = -p.speed;
                    }
                    else {
                        p.health = 0;
                    }
                    if (id == "block" || (id == "smash" && player.upgrades.contains("Parry"))) {
                        player.health = Math.min(player.health + 5, player.maxHealth);
                        player.attackCounters.get(player.attacks.indexOf("Charge"))[1] -= player.attackCounters.get(player.attacks.indexOf("Charge"))[0];
                        player.attackCounters.get(player.attacks.indexOf("Smash"))[1] -= player.attackCounters.get(player.attacks.indexOf("Smash"))[0]/3;
                        if (player.upgrades.contains("Heavy Punch")) {
                            player.ammoCounter = Math.min(10, player.ammoCounter + 1);
                            Main.panel.attackKeys[3] = Integer.toString(player.ammoCounter);
                        }
                        if (player.upgrades.contains("Mending")) {
                            player.attackCounters.get(player.attacks.indexOf("Block"))[1] -= player.attackCounters.get(player.attacks.indexOf("Block"))[0]/2;
                        }
                        if (player.upgrades.contains("Bandage")) {
                            player.attackCounters.get(player.attacks.indexOf("Bandage"))[1] -= player.attackCounters.get(player.attacks.indexOf("Bandage"))[0]/10;
                        }
                        if (player.upgrades.contains("Surge Fist")) {
                            player.attackCounters.get(player.attacks.indexOf("Surge Fist"))[1] -= player.attackCounters.get(player.attacks.indexOf("Surge Fist"))[0];
                        }
                        if (player.upgrades.contains("Thorns")) {
                            for (Enemy e : Main.enemies) {
                                e.health -= player.basicDamage;
                            }
                        }
                        if (player.upgrades.contains("Unbreaking")) {
                            player.health = Math.min(player.health + 5, player.maxHealth);
                        }
                    }
                }
            }
        }
        if (id == "block") {
            return;
        }
        if (playerTeam) {
            for (Enemy e : Main.enemies) {
                if (collision(e) && !e.immune && !Main.noDamage) {
                    System.out.println(id + " " + e.type);
                    if (e.type == "diavolo" && e.counter3 <= 0) {
                        Main.noDamage = true;
                        e.counter3 = 480;
                        if (!(Main.tempo == "Omit" || Main.tempo == "Rule" || Main.tempo == "Seer")) {
                            Main.fps = 9999;
                        }
                    }
                    else {
                        e.stun += stun;
                        e.health -= health + Math.ceil(Math.sqrt(e.sinking));

                        if (player.host == "Sailor") {
                            if (player.upgrades.contains("Arctic")) {
                                e.stun += Math.min(35, 5 * e.sinking);
                            }
                            e.sinking += health / 10;
                            if (id == "sailorSlam") {
                                e.health -= e.sinking * health - health;
                                player.health = Math.min(player.maxHealth, player.health + e.sinking * 2);
                                if (player.attacks.contains("Cannon")) {
                                    player.attackCounters.get(player.attacks.indexOf("Cannon"))[1] -= player.attackCounters.get(player.attacks.indexOf("Cannon"))[0]/20 * e.sinking;
                                }
                                e.sinking = 0;
                            }
                            if (id == "slash" && player.attacks.contains("Cutlass")) {
                                player.attackCounters.get(player.attacks.indexOf("Cutlass"))[1] -= player.attackCounters.get(player.attacks.indexOf("Cutlass"))[0]/8;
                            }
                            if (id == "slash" && player.attacks.contains("Flintlock")) {
                                player.ammoCounter++;
                                Main.panel.attackKeys[2] = Integer.toString(player.ammoCounter);
                            }
                            if ((id == "anchor" || id == "bullet") && player.upgrades.contains("Wind")) {
                                e.sinking += health/10;
                                e.health += health;
                            }
                            if ((id == "anchor" || id == "bullet") && player.upgrades.contains("Swell")) {
                                e.health -= (maxLifetime - lifetime)/40;
                                e.sinking += (maxLifetime - lifetime)/400;
                            }
                        }
                        
                        if (player.host == "Brawler") {
                            if (id == "punch" && player.upgrades.contains("Light Punch")) {
                                player.attackCounters.get(player.attacks.indexOf("Charge"))[1] -= player.attackCounters.get(player.attacks.indexOf("Charge"))[0]/5;
                                player.attackCounters.get(player.attacks.indexOf("Smash"))[1] -= player.attackCounters.get(player.attacks.indexOf("Smash"))[0]/15;
                            }
                            if (id == "punch" && player.upgrades.contains("Sucker Punch") && e.health >= e.maxHealth - player.basicDamage) {
                                e.health -= health * 3;
                                e.stun += 60;
                                player.health = Math.min(player.maxHealth, player.health + health);
                            }
                            if (id == "smash" && player.upgrades.contains("Shock")) {
                                e.health += 30;
                                for (Enemy e2 : Main.enemies) {
                                    e2.health -= 30;
                                }
                            }
                            if (id == "smash" && player.upgrades.contains("Tremor")) {
                                for (Enemy e2 : Main.enemies) {
                                    e2.stun += 60;
                                }
                                e.stun *= 2;
                            }
                        }

                        if (e.health <= 0) {
                            player.health = Math.min(player.maxHealth, player.health + e.sinking);
                        }
                        health = 0;
                        System.out.println("HIT" + e.health);
                    }
                }
            }
        }
        else {
            if (collision(player) && !player.immune && !Main.noDamage) {
                if (Main.tempo == "Seer" && Main.tempoCounter <= 0) {
                    Main.panel.keyHandler.spacePressed = true;
                }
                else {
                    if (Main.difficulty == "Seconds") {
                        player.health -= health * 2;
                    }
                    else if (Main.difficulty == "Minutes") {
                        player.health -= health;
                    }
                    else {
                        player.health -= health/2;
                    }
                    player.stun += stun;
                    if (Main.rewinded == 0) {
                        int[] damage = {Main.timeCounter, health};
                        Main.damaged.add(damage);
                    }
                    if (id == "smog") {
                        player.confusionTimer += 500;
                        System.out.println(player.confusionTimer);
                    }
                    health = 0;
                    System.out.println("HIT" + player.health);
                }
            }
        }
        if (health == 0 && id == "grapple") {
            for (int i = 0; i < 200; i++) {
                player.moveInDirection(1, player.pointTowards(x, y));
            }
        }
    }
}
