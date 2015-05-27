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

    private final int SIZE = 800; //Screen dimensions width and height
    private final int scaleSize = 2; //scaling, so that each part of the grid isn't confined to being on epixel
    private JFrame frame; //JFrame to contain the Canvas
    private BufferStrategy bs; //Buffer strategy used when drawing graphics to the screen

    private Keyboard key; //Custom Key Listener
    private Mouse mouse; //Custom Mouse listener

    private CellularSimulator cells; //Holds the CellularSimulator, so that it can be reinitialised at any time or reset

    //main
    //Creates a new main and calls the start function
    public static void main(String[] args) {
        Main m = new Main();
        m.Start();
    }

    //Constructor
    //Initialises many key values that will be used by the program
    public Main(){
        Dimension size = new Dimension(SIZE, SIZE); //Size of the Canvas
        setSize(size);
        setPreferredSize(size);

        key = new Keyboard(); //Initalises and setting the key and mouse listeners
        addKeyListener(key);
        mouse = new Mouse();
        addMouseListener(mouse);

        frame = new JFrame(""); //Sets up the Frame, the Title changes dynamically so doesn't need to be set
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        requestFocus();

        cells = new CellularSimulator(SIZE / scaleSize, SIZE / scaleSize, 3, scaleSize); //initialises the cells object with the relevant values

        createBufferStrategy(2); //IMPORTAT, if you forget this (it's easy to) your program will stop working
    }

    //Start
    //This is just a program loop
    //It uses two timers, one for calculating the frames/ticks per second and the other for giving a delay between smoothing iterations without having to make a thread go to sleep
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
                System.out.printf("tps: %d || fps: %d\n", ticks, frames); //Prints out the ticks per second (tps) and the frames per second (fps)
                ticks = 0;
                frames = 0;
            }

            //This timer "fires" off every 2 seconds just to give some delay between smoothing iterations
            if((System.currentTimeMillis() - timer2) > 2000){
                timer2 += 2000;
                cells.Iterate();
            }
        }
    }

    //Update
    //This is called every tick and runs through some input pieces and also dynamically changes the title
    //Also checks to se if the program "End Game" should be run
    private void Update() {
        if(key.keys[KeyEvent.VK_ESCAPE]){ //Press ESCAPE to close the application
            System.exit(0);
        }

        if(key.keys[KeyEvent.VK_SPACE]){ //Press SPACE to toggle between simulating and not simulating
            cells.TogglePlay();
        }

        if(key.keys[KeyEvent.VK_C]){ //If you press C it starts a customisation process
            CustomMenu();
        }

        if(mouse.isMouseClicked()){ //If you press the mouse button it refreshes the board
            cells.Restart();
        }

        frame.setTitle(cells.play + " :: " + cells.seed); //Dynamically change the window title


        //Complex set of code that allows you to save out the results of the program
        if(cells.done){
            int reply = JOptionPane.showConfirmDialog(null, "Simulation Finished, would you like to save?", "SIMULATION FINISHED", JOptionPane.YES_NO_OPTION); //Option panel that takes asks if you want to save
            if(reply == JOptionPane.YES_OPTION){//if you do want to save
                JFileChooser fc = new JFileChooser();
                if(fc.showSaveDialog(fc) == JFileChooser.APPROVE_OPTION){ //opens file browser for you to select a destination for the file
                    File file = fc.getSelectedFile();
                    Container c = frame.getContentPane();
                    BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB); //creates an image the size of the canvas
                    Graphics2D g2d = im.createGraphics();
                    cells.Render(g2d); //renders the CellularSimulator to the imaghe instead of the canvas
                    try {
                        ImageIO.write(im, "PNG", file);
                        System.exit(0); //writes the image to a file and then quits
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(0); //if that fails the program will quit
                    }
                }
            }else{
                System.exit(0);//if you don't want to save the program quits
            }
        }
    }

    //CustomMenu
    //This allows you to specify some custom values for the fill percent and the number of times the program will smooth
    private void CustomMenu() {
        boolean f = false; //this means a fill percent hasn't been set
        int fill = 0;
        while(!f){
            try{
                f = true;
                fill = Integer.parseInt(JOptionPane.showInputDialog("Current Fill Percentage is at " + cells.fillPercent)); //this tries to get a fill percentage from the JOptionPane
            }catch(Exception e){
                f = false; //if not it sets f to false so the process repeats
            }

            if(fill < 0 || fill > 100){ //this catches inputs that aren't a percentage 0 - 100
                f = false;
            }
        }

        //The same thing happens here
        boolean s = false;
        int smooth = 0;
        while(!s){
            try{
                s = true;
                smooth = Integer.parseInt(JOptionPane.showInputDialog("Current Smoothing is at " + cells.maxSmooths));
            }catch(Exception e){
                s = false;
            }

            if(smooth < 1){ //catches smoothing numbers that are less than 1
                s = false;
            }
        }


        cells = new CellularSimulator(SIZE / scaleSize, SIZE / scaleSize, smooth, scaleSize, fill); //reinitialises the cell simulator with the new values
    }

    //Render function
    //Calls the render function from the CellularSimulator
    //And then paints the graphics to the Canvas
    private void Render() {
        bs = getBufferStrategy();
        Graphics2D g2 = (Graphics2D)bs.getDrawGraphics();

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight()); //This sets a white background so that if the CellularSimulator doesn't render the screen isn't blank, it's white

        cells.Render(g2);

        g2.dispose();
        bs.show();
    }
}
