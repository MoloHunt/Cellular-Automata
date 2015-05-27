import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by MoloHunt on 27/05/15.
 */
public class Mouse implements MouseListener{

    public boolean mouseDown;
    private boolean mouseClick;

    public boolean isMouseClicked(){
        if(mouseClick){
            mouseClick = false;
            return true;
        }else{
            return mouseClick;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseClick = true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
