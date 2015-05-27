import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by MoloHunt on 27/05/15.
 */

//Custom Mouse listener to handle specifically when the mouse is clicked

public class Keyboard implements KeyListener{

    public boolean keys[] = new boolean[600]; //this is just to store any possible keyboard key that could be pressed, just incase I need to implement a new key being pressed

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true; //when the key is down it sets the boolean in the array at the index of the key code to true
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false; //when the key is up it sets the boolean in the array at the index of the key code to false
    }
}
