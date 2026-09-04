public class WaveSpawner {
    int wave = 0;
    public void nextWave() {
        wave++;
        Main.timeCounter = 0;
        Main.projectiles.clear();
        switch(wave) {
            case 1:
                //1 basic
                Main.enemies.add(new Enemy(100, 100, 20, 20, 0.8, 40, "basic", 50));
                break;
            case 2:
                //1 basic
                Main.enemies.add(new Enemy(100, 100, 20, 20, 0.8, 40, "basic", 50));
                //1 ranged
                Main.enemies.add(new Enemy(100, 100, 15, 15, 0.2, 20, "ranged", 50));
                break;
            case 3:
                //1 shotgunner
                Main.enemies.add(new Enemy(100, 100, 25, 25, 1.2, 60, "shotgunner", 60));
                break;
            case 4:
                //2 ranged
                Main.enemies.add(new Enemy(300, 300, 20, 20, 0.2, 20, "ranged", 50));
                Main.enemies.add(new Enemy(200, 200, 20, 20, 0.2, 20, "ranged", 50));
                //MEGA BASIC
                Main.enemies.add(new Enemy(100, 100, 35, 35, 0.6, 200, "basic", 250));
                Main.upgrading = true;
                break;
            case 5:
                Main.enemies.add(new Enemy(100, 100, 30, 30, 0.5, 400, "miniboss", 200));
                break;
            case 6:
                Main.enemies.add(new Enemy(100, 100, 20, 20, 0.5, 400, "dio", 200));
                break;
            case 7:
                Main.enemies.add(new Enemy(100, 100, 15, 15, 0.5, 60, "archer", 200));
                Main.enemies.add(new Enemy(200, 200, 15, 15, 0.2, 20, "ranged", 50));
                Main.enemies.add(new Enemy(300, 300, 15, 15, 0.2, 20, "ranged", 50));
                Main.enemies.add(new Enemy(100, 100, 25, 25, 1.2, 60, "shotgunner", 60));
                Main.upgrading = true;
                break;
            case 8: 
                if (Main.rewinded == -1) {
                    Main.rewinded = 0;
                }
                Main.backup2 = Main.player.copy();
                Main.enemies.add(new Enemy(100, 100, 20, 20, 0.5, 450, "kira", 300));
                break;
            case 9:
                Main.rewinded = -1;
                Main.enemies.add(new Enemy(100, 100, 30, 30, 0.5, 400, "miniboss", 200));
                Main.enemies.add(new Enemy(100, 100, 15, 15, 0.5, 60, "archer", 200));
                Main.enemies.add(new Enemy(100, 100, 35, 35, 0.6, 200, "basic", 250));
                Main.upgrading = true;
                break;
            case 10:
                Main.enemies.add(new Enemy(100, 100, 20, 20, 0.5, 300, "doppio", 300));
                break;
            case 11:
                Main.enemies.add(new Enemy(100, 100, 20, 20, 0.5, 400, "whitesnake", 300));
                Main.upgrading = true;
                break;
            }

    }
}
