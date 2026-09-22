import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.SwingUtilities;

public class UpgradeManager {
    int upgrades = 0;
    ArrayList<String> upgradeTypes = new ArrayList<String>();
    ArrayList<String> upgradeDescription = new ArrayList<String>();

    ArrayList<String> tempoTypes = new ArrayList<String>();
    ArrayList<String> tempoDescription = new ArrayList<String>();

    ArrayList<String> difficultyTypes = new ArrayList<String>();
    ArrayList<String> difficultyDescription = new ArrayList<String>();

    String[] choices = new String[3];
    String[] choiceDescription = new String[3];

    public void initialize() {
        //difficulties
        difficultyTypes.add("Hours");
        difficultyDescription.add("All enemies and enemy projectiles move at half speed, have halved health, and deal half damage.");
        difficultyTypes.add("Minutes");
        difficultyDescription.add("Normal gameplay.");
        difficultyTypes.add("Seconds");
        difficultyDescription.add("Named for how long you will last. All enemies deal double damage have double health and take halved stun.");

        //tempos
        //stop time
        tempoTypes.add("Halt");
        tempoDescription.add("Stop time for 5 seconds, after which there is a cooldown of 20 seconds.");
        tempoTypes.add("Star");
        tempoDescription.add("Stop time for 2 seconds, after which there is a cooldown of 10 seconds.");
        tempoTypes.add("Gold");
        tempoDescription.add("Time is stopped while you are not moving. Activate to move in stopped time for 3 seconds, after which there is a cooldown of 10 seconds.");

        //skip time
        tempoTypes.add("Omit");
        tempoDescription.add("Skip time for 10 seconds, after which there is a 10 second cooldown.");
        tempoTypes.add("Rule");
        tempoDescription.add("Skip time for half a second, after which there is a 2 second cooldown.");
        tempoTypes.add("Seer");
        tempoDescription.add("When you would be hit or on activation, skip 1 second of time. This recharges after 4 seconds.");

        //rewind time
        tempoTypes.add("Echo");
        tempoDescription.add("Use to store a past copy of yourself. Use again to rewind into the past copy. Each use has a 15 second cooldown.");
        tempoTypes.add("Null");
        tempoDescription.add("Undo the time of all projectiles to before they were shot (clear all projectiles). Cooldown of 3 seconds.");
        tempoTypes.add("Blip");
        tempoDescription.add("Every second, save a past copy of yourself. Use to rewind into the past copy. Each use has a 5 second cooldown.");

        //speed up time
        tempoTypes.add("Rush");
        tempoDescription.add("Quadruple the speed of time for yourself for 1 second. Hold to use. Recharges after 10 seconds");
        tempoTypes.add("Zion");
        tempoDescription.add("Doubles the speed of time for yourself for 20 seconds. Recharges after 40 seconds.");
        tempoTypes.add("Rust");
        tempoDescription.add("While you are not moving, time moves 12 times faster. While moving, time moves three times faster and your speed of time is doubled. Activate to make the game run normal speed.");
        
        //alternate dimension
        tempoTypes.add("Cast");
        tempoDescription.add("Switch positions with a clone of yourself that is frozen in time. There is a 4 second cooldown between swaps.");
        tempoTypes.add("Deed");
        tempoDescription.add("Switch with a new copy of yourself from an alternative timeline for 3 seconds. There is a 9 second cooldown between swaps.");
        tempoTypes.add("Rail");
        tempoDescription.add("Once at the start of each wave, store a past copy of yourself. Use to reset to said past copy and restart the wave. If used during a wave with an upgrade, take a new upgrade, keeping the old one.");

        //slow down time
        tempoTypes.add("Fade");
        tempoDescription.add("Enemies and opposing projectiles have their time slowed down the closer they get to you. When close enough, they get repelled from you. Hold to use for up to 2 seconds with a 5 second cooldown.");
        tempoTypes.add("Wing");
        tempoDescription.add("Hold this abillity to percieve time four times slower and see in skipped time.");
        tempoTypes.add("Edge");
        tempoDescription.add("Use to slow the speed of everything else by 80% for 7 seconds. Recharges after 20 seconds.");

        if (Main.player.host == "Sailor") {
            //hook
            upgradeTypes.add("Slip");
            upgradeDescription.add("You can move and use other attacks when using hook. Hook deals 2 more damage.");
            upgradeTypes.add("Figure 8");
            upgradeDescription.add("Every eighth hook is larger and deals four times the damage. Hook's cooldown is reduced slightly.");
            upgradeTypes.add("Bowline");
            upgradeDescription.add("Hook deals 10 more damage. You lose 1 HP when you use hook");
            upgradeTypes.add("Hitch");
            upgradeDescription.add("Hook stuns for 0.25 seconds and is 5 pixels larger");

            //anchor
            upgradeTypes.add("Tsunami");
            upgradeDescription.add("Anchor now deals 10 more damage, stuns for 2x the duration, and is larger.");
            upgradeTypes.add("Wind");
            upgradeDescription.add("Anchor now deals no damage, but applies 2x the sinking and has lowered cooldown");
            upgradeTypes.add("Tidal");
            upgradeDescription.add("Anchor shoots a small anchor with halved stats 20 frames after. Anchor now stuns you for a short time. Anchor's cooldown is reduced slightly.");
            upgradeTypes.add("Swell");
            upgradeDescription.add("Anchor now bounces off walls and deals more damage the longer it has been on the screen. Anchor lasts for twice as long and has reduced cooldown.");

            //drift
            upgradeTypes.add("Dolphin");
            upgradeDescription.add("Drift increases speed twice as much and lasts twice as long");
            upgradeTypes.add("Salmon");
            upgradeDescription.add("Drift puts anchor off cooldown. Drift has a slightly lowered cooldown.");
            upgradeTypes.add("Sardine");
            upgradeDescription.add("Drift's cooldown is massively decreased. Drift heals 3 HP on use.");
            upgradeTypes.add("Piranha");
            upgradeDescription.add("When drift is used, shoot a projectile with half of your anchor's stats to your mouse.");

            //slam
            upgradeTypes.add("Trawler");
            upgradeDescription.add("Slam deals 4 more damage per stack of sinking. Slam lasts a lot longer on the field.");
            upgradeTypes.add("Speedboat");
            upgradeDescription.add("Slam is now a projectile. Slam lasts 3x longer. The cooldown of slam is decreased.");
            upgradeTypes.add("Yacht");
            upgradeDescription.add("Slam deals 2 more damage per stack of sinking. Slam puts Drift off cooldown.");
            upgradeTypes.add("Sailboat");
            upgradeDescription.add("Slam deals 6 more damage per stack of sinking.");

            //misc
            upgradeTypes.add("Arctic");
            upgradeDescription.add("When sinking is applied, stun the enemy for 5 frames per sinking stack, up to 35 frames. Enemies spawn with one stack of sinking.");
            upgradeTypes.add("Atlantic");
            upgradeDescription.add("Enemies move slightly slower per stack of sinking, up to 0.05 pixels per frame.");
            upgradeTypes.add("Pacific");
            upgradeDescription.add("You and your melee attacks are bigger. You walk faster");
            upgradeTypes.add("Indian");
            upgradeDescription.add("Sinking is applied to each enemy randomly, on average once every seven seconds.");
        
            //moves
            upgradeTypes.add("Grapple");
            upgradeDescription.add("Use to shoot a projectile that deals 10 damage (scales off hook damage and size and anchor lifetime and stun). When hitting an enemy or the edge of the map, it teleports you there.");
            upgradeTypes.add("Cannon");
            upgradeDescription.add("Every stack of sinking consumed by slam decreases this cooldown by 1/20. Use to shoot a projectile that deals 80 damage (scales off anchor stats)");
            upgradeTypes.add("Cutlass");
            upgradeDescription.add("Every hook landed decreases this cooldown by 1/8. Use to make a large slash that deals 40 damage and nullifies enemy projectiles that hit it (scales off hook stats)");
            upgradeTypes.add("Flintlock");
            upgradeDescription.add("Replace your anchor with a flintlock. When you land an M1, gain 1 shot. Has half of your anchor stats. Anchor lasts 3x longer and is 4x as fast.");

        }
        if (Main.player.host == "Brawler") {
            //punch
            upgradeTypes.add("Light Punch");
            upgradeDescription.add("Punch decreases the cooldown of charge by 1/5 and smash by 1/15. Punch staggers you for a shorter amount of time.");
            upgradeTypes.add("Heavy Punch");
            upgradeDescription.add("Punch now stores block counters, and needs 1 block counter to use. There is a limit of 10 block counters. Punch deals 30 more damage.");
            upgradeTypes.add("Zoom Punch");
            upgradeDescription.add("Punch spawns further in front of you. Punch deals 2 more damage.");
            upgradeTypes.add("Sucker Punch");
            upgradeDescription.add("Punch deals 4x damage on enemies with full HP. Punch stuns enemies with full HP. When hitting an enemy with full HP with punch, heal hp equal to 25% of damage dealt. Punch deals 1 more damage.");

            //charge
            upgradeTypes.add("Walkspeed Override");
            upgradeDescription.add("Charge is 50% larger and deals 50% more damage.");
            upgradeTypes.add("Raging Pace");
            upgradeDescription.add("Charge now has a 10 second cooldown. Become immune to damage while charging.");
            upgradeTypes.add("Cataclysm");
            upgradeDescription.add("Charge stuns enemies for 2 seconds. Charge heals 5 hp when used.");
            upgradeTypes.add("Void Rush");
            upgradeDescription.add("After charge is done, initiate a second charge.");

            //block
            upgradeTypes.add("Mending");
            upgradeDescription.add("Blocking a projectile lowers block's cooldown by 1/2. Using block heals 2 HP.");
            upgradeTypes.add("Protection");
            upgradeDescription.add("Block is larger, lasts longer, and has a slightly lower cooldown.");
            upgradeTypes.add("Thorns");
            upgradeDescription.add("Block deals damage equal to your punch to all enemies when it blocks a projectile.");
            upgradeTypes.add("Unbreaking");
            upgradeDescription.add("Blocking a projectile heals 10 hp. Become immune to damage while blocking.");

            //smash
            upgradeTypes.add("Rage");
            upgradeDescription.add("Smash now has a 25 second cooldown. Smash deals 30 more damage.");
            upgradeTypes.add("Parry");
            upgradeDescription.add("Smash now works as a block, except it reflects projectiles. Smash lasts twice as long.");
            upgradeTypes.add("Shock");
            upgradeDescription.add("Smash stuns enemies twice as long. Smash deals 30 damage to all other enemies.");
            upgradeTypes.add("Tremor");
            upgradeDescription.add("Smash doubles stun duration of enemies hit. Stun stuns all enemies for an extra half a second.");

            //misc
            upgradeTypes.add("Workout");
            upgradeDescription.add("You walk 25% faster. You passively heal 1 HP every second.");
            upgradeTypes.add("Muscles");
            upgradeDescription.add("All your moves deal 25% more damage.");
            upgradeTypes.add("Fat");
            upgradeDescription.add("Increase your max health by 300.");
            upgradeTypes.add("Aura");
            upgradeDescription.add("Enemies spawn with 5 stacks of sinking (they take 3 more damage per hit)");

            //new moves
            upgradeTypes.add("Bandage");
            upgradeDescription.add("Use to heal 50 HP. Cooldown of 60 seconds, each block lowers cooldown by 6 seconds. You have 10 more max health.");
            upgradeTypes.add("Surge Fist");
            upgradeDescription.add("Charge and shoot a blue fireball dealing 40 damage, scaling with charge stats. Goes off cooldown on block.");
            upgradeTypes.add("Defensive Stance");
            upgradeDescription.add("Get a second block.");
            upgradeTypes.add("Blood Ritual");
            upgradeDescription.add("Lose 40 HP, put all your moves but this one on cooldown.");
        }
        
    
    }

    public void upgrade(boolean tempo, boolean difficulties) {
        Random random = new Random();
        Main.player.health = Math.min(Main.player.maxHealth, Main.player.health + Main.player.maxHealth/2);
        if (tempo) {
            for (int i = 0; i < 3; i++) {
                int removing = random.nextInt(tempoTypes.size());
                choices[i] = tempoTypes.get(removing);
                choiceDescription[i] = tempoDescription.get(removing);
                tempoTypes.remove(removing);
                tempoDescription.remove(removing);
            }
        }
        else if (difficulties) {
            for (int i = 0; i < 3; i++) {
                choices[i] = difficultyTypes.get(i);
                choiceDescription[i] = difficultyDescription.get(i);
            }
        }
        else {
            for (int i = 0; i < 3; i++) {
                int removing = random.nextInt(upgradeTypes.size());
                choices[i] = upgradeTypes.get(removing);
                choiceDescription[i] = upgradeDescription.get(removing);
                upgradeTypes.remove(removing);
                upgradeDescription.remove(removing);
            }
        }

        System.out.println(choices[0] + " " + choices[1] + " " + choices[2]);
        Main.panel.repaint();
        try {
            Thread.sleep(500); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        while (!Main.panel.mouseHandler.leftClick) {
            try {
                Thread.sleep(5); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mousePosition, Main.panel);
        if (tempo) {
            if (mousePosition.x <= Main.panelWidth/3) {
                Main.tempo = choices[0];
            }
            else if (mousePosition.x <= Main.panelWidth*2/3) {
                Main.tempo = choices[1];
            }
            else {
                Main.tempo = choices[2];
            }
        }
        else if (difficulties) {
            if (mousePosition.x <= Main.panelWidth/3) {
                Main.difficulty = choices[0];
            }
            else if (mousePosition.x <= Main.panelWidth*2/3) {
                Main.difficulty = choices[1];
            }
            else {
                Main.difficulty = choices[2];
            }
        }
        else {
            if (mousePosition.x <= Main.panelWidth/3) {
                Main.player.upgrade(choices[0]);
                if (Main.backup != null) {
                    Main.backup.upgrade(choices[0]);
                }
            }
            else if (mousePosition.x <= Main.panelWidth*2/3) {
                Main.player.upgrade(choices[1]);
                if (Main.backup != null) {
                    Main.backup.upgrade(choices[1]);
                }
            }
            else {
                Main.player.upgrade(choices[2]);
                if (Main.backup != null) {
                    Main.backup.upgrade(choices[2]);
                }
            }
        }
        Main.upgrading = false;
    }
    
}
