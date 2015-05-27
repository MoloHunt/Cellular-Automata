import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by MoloHunt on 27/05/15.
 */

//Custom Mouse listener to handle specifically when the mouse is clicked

public class Mouse implements MouseListener{

    private boolean mouseClick;

    //isMouseClicked
    //returns mouseClick and then sets it to false
    //this is to prevent double firing the mouse being clicked
    public boolean isMouseClicked(){
        if(mouseClick){
            mouseClick = false; //mouseClick set to false to avoid double firing
            return true;
        }else{
            return false; //this used to return mouseClick but I think it is faster to just return false so we aren't checking mouseClick twice
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseClick = true; //sets mouseClick to true when the mouse is clicked
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
