import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class KeyHandler implements KeyListener {

    boolean wPressed, aPressed, sPressed, dPressed, shiftPressed, spacePressed, qPressed, ePressed, rPressed, tPressed, fPressed;

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            wPressed = true;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            aPressed = true;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            sPressed = true;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            dPressed = true;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }
        if (code == KeyEvent.VK_SHIFT) {
            shiftPressed = true;
        }
        if (code == KeyEvent.VK_Q) {
            qPressed = true;
        }
        if (code == KeyEvent.VK_E) {
            ePressed = true;
        }
        if (code == KeyEvent.VK_R) {
            rPressed = true;
        }
        if (code == KeyEvent.VK_T) {
            tPressed = true;
        }
        if (code == KeyEvent.VK_F) {
            fPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            wPressed = false;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            aPressed = false;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            sPressed = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            dPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
        if (code == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        }
        if (code == KeyEvent.VK_Q) {
            qPressed = false;
        }
        if (code == KeyEvent.VK_E) {
            ePressed = false;
        }
        if (code == KeyEvent.VK_R) {
            rPressed = false;
        }
        if (code == KeyEvent.VK_T) {
            tPressed = false;
        }
        if (code == KeyEvent.VK_F) {
            fPressed = false;
        }
    }
}
