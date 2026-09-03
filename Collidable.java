public class Collidable {
    double x = 0;
    double y = 0;

    int width = 20;
    int height = 20;
    double baseWidth, baseHeight;
    
    double baseSpeed = 1;
    double speed = 1;
    double direction = 0;

    int health = 10;

    public double pointTowards(double x2, double y2) {
        return Math.atan2(y2 - y, x2 - x);
    }

    public double distanceFrom(Collidable other) {
        return Math.sqrt((other.x - x)*(other.x - x) + (other.y - y)*(other.y - y));
    }

    public void moveInDirection(double size, double direction) {
        x = Math.min(Main.panelWidth/Main.scale - width, Math.max(0, x + size * Math.cos(direction)));
        y = Math.min(Main.panelHeight/Main.scale - height, Math.max(0, y + size * Math.sin(direction)));
    }

    public boolean collision(Collidable other) {
        if (this.x + this.width > other.x && 
            this.x <= other.x + other.width &&
            this.y + this.height > other.y &&
            this.y <= other.y + other.height) {
            return true;
        }
        return false;
    }

}
