import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by MoloHunt on 27/05/15.
 */
public class Main extends Canvas {

    private final int SIZE = 800;
    private final int scaleSize = 2;
    private JFrame frame;
    private BufferStrategy bs;

    private Keyboard key;
    private Mouse mouse;

    private CellularSimulator cells;

    public static void main(String[] args) {
        Main m = new Main();
        m.Start();
    }

    public Main(){
        Dimension size = new Dimension(SIZE, SIZE);
        setSize(size);
        setPreferredSize(size);

        key = new Keyboard();
        addKeyListener(key);
        mouse = new Mouse();
        addMouseListener(mouse);

        frame = new JFrame("Cellular Automata");
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        requestFocus();

        cells = new CellularSimulator(SIZE / scaleSize, SIZE / scaleSize, 3, scaleSize);

        createBufferStrategy(2);
    }

    private void Start() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        long timer2 = System.currentTimeMillis();
        final double ns = 1000000000.0 / 15.0;
        double delta = 0;
        int frames = 0;
        int ticks = 0;
        requestFocus();

        while(true){
            long now = System.nanoTime();
            delta += (now-lastTime) / ns;
            lastTime = now;

            while(delta >= 1){
                Update();
                ticks++;
                delta--;
            }
            Render();
            frames++;

            if((System.currentTimeMillis() - timer) > 1000){
                timer += 1000;
                System.out.printf("tps: %d || fps: %d\n", ticks, frames);
                ticks = 0;
                frames = 0;
            }

            if((System.currentTimeMillis() - timer2) > 2000){
                timer2 += 2000;
                cells.Iterate();
            }
        }
    }

    private void Update() {
        if(key.keys[KeyEvent.VK_ESCAPE]){
            System.exit(0);
        }

        if(key.keys[KeyEvent.VK_SPACE]){
            cells.TogglePlay();
        }

        if(key.keys[KeyEvent.VK_C]){
            CustomMenu();
            cells.Restart();
        }

        if(mouse.isMouseClicked()){
            cells.Restart();
        }

        frame.setTitle(cells.play + " :: " + cells.seed);

        if(cells.done){
            int reply = JOptionPane.showConfirmDialog(null, "Simulation Finished, would you like to save?", "SIMULATION FINISHED", JOptionPane.YES_NO_OPTION);
            if(reply == JOptionPane.YES_OPTION){
                JFileChooser fc = new JFileChooser();
                if(fc.showSaveDialog(fc) == JFileChooser.APPROVE_OPTION){
                    File file = fc.getSelectedFile();
                    Container c = frame.getContentPane();
                    BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = im.createGraphics();
                    cells.Render(g2d);
                    try {
                        ImageIO.write(im, "PNG", file);
                        System.exit(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                System.exit(0);
            }
        }
    }

    private void CustomMenu() {
        boolean f = false;
        int fill = 0;
        while(!f){
            try{
                f = true;
                fill = Integer.parseInt(JOptionPane.showInputDialog("Current Fill Percentage is at " + cells.fillPercent));
            }catch(Exception e){
                f = false;
            }
        }

        boolean s = false;
        int smooth = 0;
        while(!s){
            try{
                s = true;
                smooth = Integer.parseInt(JOptionPane.showInputDialog("Current Smoothing is at " + cells.maxSmooths));
            }catch(Exception e){
                s = false;
            }
        }

        cells = new CellularSimulator(SIZE / scaleSize, SIZE / scaleSize, smooth, scaleSize, fill);
    }

    private void Render() {
        bs = getBufferStrategy();
        Graphics2D g2 = (Graphics2D)bs.getDrawGraphics();

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        cells.Render(g2);

        g2.dispose();
        bs.show();;
    }
}
