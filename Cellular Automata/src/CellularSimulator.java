import java.awt.*;
import java.util.Random;

/**
 * Created by MoloHunt on 27/05/15.
 */

//The simulator class, handles all of the simulating, as the name suggests
public class CellularSimulator {

    private int[][] cells; //2D array of integers to represent the cells either 0 or 1
    private int width, height; //width and height of the grid
    private int scalesize; //this is used by the renderer to determine how big each cell is
    public String seed; //this is the seed used for the random number generator and is displayed in the title bar
    private Random numberGen; //this s the object used for generating the random numbers
    public int fillPercent = 50; //the default fill percent amount

    private Color onColor = Color.black; //this allow the colours to be changed quickly
    private Color offColor = Color.white;

    public int maxSmooths; //the maximum number of smooths the program will do
    private int currentSmooths; //how many smooths the program has performed so far

    public boolean play = false; //whether the program should be simulating or not
    public boolean done = false; //whether the simulation has finished

    public boolean needRender; //Controls whether Render is run in the main class

    //One version of the constructor that doesn't have a custom fill Percent
    public CellularSimulator(int w, int h, int mS, int scale){
        width = w;
        height = h;
        cells = new int[width][height];
        numberGen = new Random(System.currentTimeMillis()); //uses the currentTimeMillis as a seed
        seed = Long.toString(System.currentTimeMillis());

        scalesize = scale;

        maxSmooths = mS;
        currentSmooths = 0;

        GenerateInitialStructure();
    }

    //One version of the constructor that has a custom fill Percent
    public CellularSimulator(int w, int h, int mS, int scale, int fill){
        width = w;
        height = h;
        cells = new int[width][height];
        numberGen = new Random(System.currentTimeMillis());
        seed = Long.toString(System.currentTimeMillis());

        scalesize = scale;

        maxSmooths = mS;
        currentSmooths = 0;

        fillPercent = fill; //here the custom fill percent is set

        GenerateInitialStructure();
    }

    //fills each point int the 2D array with a number
    private void GenerateInitialStructure() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(x == 0 || x == width -1 || y == 0 || y == height - 1){
                    cells[x][y] = 1;
                }else{
                    //Sets the cell contents based on the random number, this is that the average number of filled cells as a percent is equal to fill Percent
                    cells[x][y] = (numberGen.nextInt(100) < fillPercent)? 1 : 0;
                }
            }
        }
        needRender = true; //So that the initial grid is rendered
    }
    
    //This just calls Smooth if we are still below the maxSmooths and we are simulating
    //If we are done then it sets done to true
    public void Iterate() {
        if(currentSmooths <= maxSmooths && play){
            currentSmooths ++;
            Smooth();
        }

        if(currentSmooths > maxSmooths){
            done = true;
        }
    }

    //This is the smoothing algorithm, it's fairly simple but works well
    public void Smooth(){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int surroundingCells = GetSurroundingCells(x, y);

                if(surroundingCells > 4){
                    cells[x][y] = 1;
                }else if(surroundingCells < 4){
                    cells[x][y] = 0;
                }
            }
        }
        needRender = true; //the grid has been changed so it needs to be re rendered
    }

    //This counts how many squares around a given square are filled
    //[][F][F]
    //[][X][]
    //[F][][F]
    //Where [X] is our square and [F] is a filled square
    //the function would return 4
    private int GetSurroundingCells(int x1, int y1) {
        int wallCount = 0;
        for(int x = x1 - 1; x <= x1 + 1; x++){
            for(int y = y1 - 1; y <= y1 + 1; y++){
                if(InRange(x, y)){ //checks to see if the cell we want to check is in our map
                    if(x != x1 || y != y1){
                        wallCount += cells[x][y];
                    }
                }else{
                    wallCount ++; //if it's not then we increment wallCount so that we get wall growth near the edges
                }
            }
        }
        return wallCount;
    }

    //Checks that any given square is in our map range
    private boolean InRange(int x, int y) {
        return x >= 0 && x < width - 1 && y >= 0 && y < height -1;
    }

    //Toggle the play boolean
    public void TogglePlay() {
        play = !play;
    }

    //this just refreshes the grid
    public void Restart() {
        play = false;
        currentSmooths = 0;
        GenerateInitialStructure();
    }

    //The render method just runs through every cell and draws it to the canvas in a colour based on the cells state
    public void Render(Graphics2D g2) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                g2.setColor( (cells[x][y] == 1) ? onColor : offColor); //sets the colours
                g2.fillRect(x * scalesize, y * scalesize, scalesize, scalesize); //draws the cell in the right place and the right size
            }
        }
        needRender = false;
    }
}
