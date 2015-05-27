import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by MoloHunt on 27/05/15.
 */
public class Keyboard implements KeyListener{

    public boolean keys[] = new boolean[600];

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}
