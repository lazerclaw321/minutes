import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.awt.geom.AffineTransform;

public class GamePanel extends JPanel {

    KeyHandler keyHandler = new KeyHandler();
    MouseHandler mouseHandler = new MouseHandler();

    HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();

    final File[] enemyImageList = new File("Images/Enemies").listFiles();
    final File[] playerImageList = new File("Images/Player").listFiles();
    final File[] projectileImageList = new File("Images/Projectiles").listFiles();

    final Font upgradeFont = new Font("Arial", 1, 20);
    final Font descriptionFont = new Font("Arial", 2, 11);

    public GamePanel() {
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.setFocusable(true);
        loadImages(enemyImageList);
        loadImages(playerImageList);
        loadImages(projectileImageList);
    }

    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }
    
    public void drawTextBox(Graphics g, String text, int x, int y, Font font, int length) {
        FontMetrics metrics = g.getFontMetrics(font);
        int height = 0;
        String currentLine = "";
        String[] words = text.split(" ");
        int totalWidth = 0;
        for (String word : words) {
            int width = 0;
            for (int i = 0; i < word.length(); i++) {
                width += metrics.charWidth(word.charAt(i));
            }
            totalWidth += width;
            currentLine += word + " ";
            if (totalWidth >= length) {
                Rectangle rect = new Rectangle(x, height + y, 1, 1);
                drawCenteredString(g, currentLine, rect, font);
                totalWidth = 0;
                currentLine = "";
                height += metrics.getHeight();
            }
        }
        Rectangle rect = new Rectangle(x, height + y, 1, 1);
        drawCenteredString(g, currentLine, rect, font);
    }

    public void loadImages(File[] imageList) {
        for (File image : imageList) {
            BufferedImage newImage = null;
            try {
                newImage = ImageIO.read(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(image.getName().substring(0, image.getName().length() - 4));
            images.put(image.getName().substring(0, image.getName().length() - 4), newImage);
        }
    }

    public BufferedImage reflect(BufferedImage image) {
        AffineTransform tx = new AffineTransform();
        tx.translate(image.getWidth(), 0);
        tx.scale(-1, 1);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    public static BufferedImage rotate(BufferedImage image, double direction) {
        AffineTransform tx = new AffineTransform();
        double sin = Math.abs(Math.sin(direction));
        double cos = Math.abs(Math.cos(direction));
        int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
        int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
        tx.translate(w/2, h/2);
        tx.rotate(direction, 0, 0);
        tx.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    public void drawBar(Graphics2D g2, double scale, int x, int y, int width, int height, double percentage, Color back, Color front) {
        g2.setColor(back);
        g2.fillRect((int)(x*scale), (int)(y*scale), (int)(width * scale), height);
        g2.setColor(front);
        g2.fillRect((int)(x*scale), (int)(y*scale), (int)(width * percentage * scale), height);
    }

    public void drawScaledImage(Graphics2D g2, double scale, BufferedImage image, int x, int y, int width, int height) {
        g2.drawImage(image, (int)(x*scale), (int)(y*scale), (int)(width*scale), (int)(height*scale), null);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        if (Main.upgrading) {
            g2.setColor(Color.BLACK);
            drawCenteredString(g2, Main.upgrades.choices[0], new Rectangle(Main.panelWidth/4, 100, 1, 1), upgradeFont);
            drawCenteredString(g2, Main.upgrades.choices[1], new Rectangle(Main.panelWidth/2, 100, 1, 1), upgradeFont);
            drawCenteredString(g2, Main.upgrades.choices[2], new Rectangle(Main.panelWidth*3/4, 100, 1, 1), upgradeFont);
            drawTextBox(g2, Main.upgrades.choiceDescription[0], Main.panelWidth/4, 200, descriptionFont, 50*(int)Main.scale);
            drawTextBox(g2, Main.upgrades.choiceDescription[1], Main.panelWidth/2, 200, descriptionFont, 50*(int)Main.scale);
            drawTextBox(g2, Main.upgrades.choiceDescription[2], Main.panelWidth*3/4, 200, descriptionFont, 50*(int)Main.scale);
        }
        else if (Main.fps < 9000) {
            if (Main.noDamage) {
                setBackground(Color.BLACK);
            }
            else {
                setBackground(Color.WHITE);
            }
            if (Main.player != null) {
                drawScaledImage(g2, Main.scale, images.get(Main.player.host + Main.player.frame), (int)Main.player.x, (int)Main.player.y, Main.player.width, Main.player.height);
                if (Main.backup != null) {
                    drawScaledImage(g2, Main.scale, images.get(Main.backup.host + Main.backup.frame), (int)Main.backup.x, (int)Main.backup.y, Main.backup.width, Main.backup.height);
                }
            }

            for (Projectile p : Main.projectiles) {
                g2.setColor(Color.RED);
                if (p.delay > 0) {
                    drawScaledImage(g2, Main.scale, images.get("delay"), (int)p.x, (int)p.y, p.width, p.height);
                }
                else {
                    if (images.containsKey(p.id)) {
                        drawScaledImage(g2, Main.scale, rotate(images.get(p.id), p.direction), (int)p.x, (int)p.y, p.width, p.height);
                    }
                    else {
                        drawScaledImage(g2, Main.scale, images.get("Placeholder"), (int)p.x, (int)p.y, p.width, p.height);
                    }
                }
            }
            for (Enemy e : Main.enemies) {
                if (images.containsKey(e.type + e.frame)) {
                    if (Math.abs(e.direction) > 3.14/2) {
                        drawScaledImage(g2, Main.scale, images.get(e.type + e.frame), (int)e.x, (int)e.y, e.width, e.height);
                    }
                    else {
                        drawScaledImage(g2, Main.scale, reflect(images.get(e.type + e.frame)), (int)e.x, (int)e.y, e.width, e.height);
                    }
                }
                else {
                    drawScaledImage(g2, Main.scale, images.get("Placeholder"), (int)e.x, (int)e.y, e.width, e.height);
                }
                g2.setColor(Color.GREEN);
                //g2.fillRect((int)e.x, (int)e.y, e.width, e.height);
                drawBar(g2, Main.scale, (int)e.x, (int)e.y - 10, e.width, 5, (double)e.health / e.maxHealth, Color.RED, Color.GREEN);
                for (int i = 0; i < e.sinking; i++) {
                    g2.setColor(Color.BLUE);
                    g2.fillRect((int)((e.x + 2 * i)*Main.scale), (int)((e.y - 15)*Main.scale), (int)(Main.scale), (int)(3*Main.scale));
                }
            }

            //healthbar
            drawBar(g2, 1, Main.panelWidth / 10, Main.panelHeight * 8 / 9, Main.panelWidth / 3, Main.panelHeight / 30, (double)Main.player.health / (double)Main.player.maxHealth, Color.RED, Color.GREEN);
            //tempobar
            if (Main.tempoCooldown - Main.tempoDuration < Main.tempoCounter) {
                drawBar(g2, 1, Main.panelWidth / 10, Main.panelHeight * 8 / 9 + Main.panelHeight / 30, Main.panelWidth / 3, Main.panelHeight / 30, 1-(((double)Main.tempoCooldown - (double)Main.tempoCounter)/(double)Main.tempoDuration), Color.BLACK, Color.BLUE);
            }
            else {
                drawBar(g2, 1, Main.panelWidth / 10, Main.panelHeight * 8 / 9 + Main.panelHeight / 30, Main.panelWidth / 3, Main.panelHeight / 30, 1-(double)Main.tempoCounter/((double)Main.tempoCooldown - (double)Main.tempoDuration), Color.BLACK, Color.BLUE);
            }
            drawBar(g2, 1, Main.panelWidth * 6 / 10, Main.panelHeight * 8 / 9 + Main.panelHeight / 30, Main.panelWidth / 30, Main.panelHeight / 30, (double)Main.player.basicCounter/(double)Main.player.basicCooldown, Color.BLACK, Color.BLUE);
            drawBar(g2, 1, Main.panelWidth * 7 / 10, Main.panelHeight * 8 / 9 + Main.panelHeight / 30, Main.panelWidth / 30, Main.panelHeight / 30, (double)Main.player.heavyCounter/(double)Main.player.heavyCooldown, Color.BLACK, Color.BLUE);
            drawBar(g2, 1, Main.panelWidth * 8 / 10, Main.panelHeight * 8 / 9 + Main.panelHeight / 30, Main.panelWidth / 30, Main.panelHeight / 30, (double)Main.player.defenseCounter/(double)Main.player.defenseCooldown, Color.BLACK, Color.BLUE);
            drawBar(g2, 1, Main.panelWidth * 9 / 10, Main.panelHeight * 8 / 9 + Main.panelHeight / 30, Main.panelWidth / 30, Main.panelHeight / 30, (double)Main.player.specialCounter/(double)Main.player.specialCooldown, Color.BLACK, Color.BLUE);
            drawCenteredString(g2, "M1", new Rectangle(Main.panelWidth * 13 / 20, Main.panelHeight * 8 / 9 + Main.panelHeight / 30, 1, 1), upgradeFont);
            drawCenteredString(g2, "M2", new Rectangle(Main.panelWidth * 15 / 20, Main.panelHeight * 8 / 9 + Main.panelHeight / 30, 1, 1), upgradeFont);
            drawCenteredString(g2, "↑", new Rectangle(Main.panelWidth * 17 / 20, Main.panelHeight * 8 / 9 + Main.panelHeight / 30, 1, 1), upgradeFont);
            drawCenteredString(g2, "Q", new Rectangle(Main.panelWidth * 19 / 20, Main.panelHeight * 8 / 9 + Main.panelHeight / 30, 1, 1), upgradeFont);

        }
        g2.dispose();
    }
}