public class Projectile extends Collidable {

    boolean playerTeam = false;
    int lifetime;
    int maxLifetime;
    int delay;
    int stun;
    boolean area = false;
    String id;

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
            if (id == "bulletbouncy" || (id == "anchor" && Main.player.upgrades.contains("Swell"))) {
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

        if (playerTeam) {
            for (Enemy e : Main.enemies) {
                if (collision(e) && !e.immune && !Main.noDamage) {
                    if (e.type == "diavolo" && e.counter3 <= 0 && Main.difficulty != "Hours") {
                        Main.noDamage = true;
                        e.counter3 = 480;
                        if (!(Main.tempo == "Omit" || Main.tempo == "Rule" || Main.tempo == "Seer")) {
                            Main.fps = 9999;
                        }
                    }
                    else {
                        e.stun += stun;
                        if (Main.player.upgrades.contains("Arctic")) {
                            e.stun += Math.min(35, 5 * e.sinking);
                        }
                        e.health -= health + Math.ceil(Math.sqrt(e.sinking));
                        e.sinking += health / 10;
                        if (id == "sailorSlam") {
                            e.health -= e.sinking * health - health;
                            Main.player.health = Math.min(Main.player.maxHealth, Main.player.health + e.sinking * 2);
                            e.sinking = 0;
                        }
                        if (id == "anchor" && Main.player.upgrades.contains("Wind")) {
                            e.sinking += health/10;
                            e.health += health;
                        }
                        if (id == "anchor" && Main.player.upgrades.contains("Swell")) {
                            e.health -= (maxLifetime - lifetime)/40;
                             e.sinking += (maxLifetime - lifetime)/400;
                        }
                        if (e.health <= 0) {
                            Main.player.health = Math.min(Main.player.maxHealth, Main.player.health + e.sinking);
                        }
                        health = 0;
                        System.out.println("HIT" + e.health);
                    }
                }
            }
        }
        else {
            if (collision(Main.player) && !Main.player.immune && !Main.noDamage) {
                if (Main.tempo == "Seer" && Main.tempoCounter <= 0) {
                    Main.panel.keyHandler.spacePressed = true;
                }
                else {
                    if (Main.difficulty == "Seconds") {
                        Main.player.health -= health * 2;
                    }
                    else if (Main.difficulty == "Minutes") {
                        Main.player.health -= health;
                    }
                    else {
                        Main.player.health -= health/2;
                    }
                    Main.player.stun += stun;
                    if (Main.rewinded == 0) {
                        int[] damage = {Main.timeCounter, health};
                        Main.damaged.add(damage);
                    }
                    if (id == "smog") {
                        Main.player.confusionTimer += 500;
                        System.out.println(Main.player.confusionTimer);
                    }
                    health = 0;
                    System.out.println("HIT" + Main.player.health);
                }
            }
        }
    }
}
