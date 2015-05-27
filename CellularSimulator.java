import java.awt.*;
import java.util.Random;

/**
 * Created by MoloHunt on 27/05/15.
 */
public class CellularSimulator {

    private int[][] cells;
    private int width, height;
    private int scalesize;
    public String seed;
    private Random numberGen;
    public int fillPercent = 50;

    private Color onColor = Color.black;
    private Color offColor = Color.white;

    public int maxSmooths;
    private int currentSmooths;

    public boolean play = false;
    public boolean done = false;

    public CellularSimulator(int w, int h, int mS, int scale){
        width = w;
        height = h;
        cells = new int[width][height];
        numberGen = new Random(System.currentTimeMillis());
        seed = Long.toString(System.currentTimeMillis());

        scalesize = scale;

        maxSmooths = mS;
        currentSmooths = 0;

        GenerateInitialStructure();
    }

    public CellularSimulator(int w, int h, int mS, int scale, int fill){
        width = w;
        height = h;
        cells = new int[width][height];
        numberGen = new Random(System.currentTimeMillis());
        seed = Long.toString(System.currentTimeMillis());

        scalesize = scale;

        maxSmooths = mS;
        currentSmooths = 0;

        fillPercent = fill;

        GenerateInitialStructure();
    }

    private void GenerateInitialStructure() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(x == 0 || x == width -1 || y == 0 || y == height - 1){
                    cells[x][y] = 1;
                }else{
                    if(numberGen.nextInt(100) < fillPercent){
                        cells[x][y] = 1;
                    }else{
                        cells[x][y] = 0;
                    }
                }
            }
        }
    }

    public void Iterate() {
        if(currentSmooths <= maxSmooths && play){
            currentSmooths ++;
            Smooth();
        }

        if(currentSmooths > maxSmooths){
            done = true;
        }
    }

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
    }

    private int GetSurroundingCells(int x1, int y1) {
        int wallCount = 0;
        for(int neighbourX = x1 - 1; neighbourX <= x1 + 1; neighbourX++){
            for(int neighbourY = y1 - 1; neighbourY <= y1 + 1; neighbourY++){
                if(InRange(neighbourX, neighbourY)){
                    if(neighbourX != x1 || neighbourY != y1){
                        wallCount += cells[neighbourX][neighbourY];
                    }
                }else{
                    wallCount ++;
                }
            }
        }
        return wallCount;
    }

    private boolean InRange(int x, int y) {
        return x >= 0 && x < width - 1 && y >= 0 && y < height -1;
    }

    public void TogglePlay() {
        play = !play;
    }

    public void Restart() {
        play = false;
        currentSmooths = 0;
        GenerateInitialStructure();
    }

    public void Render(Graphics2D g2) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(cells[x][y] == 1){
                    g2.setColor(onColor);
                }else{
                    g2.setColor(offColor);
                }
                g2.fillRect(x * scalesize, y * scalesize, scalesize, scalesize);
            }
        }
    }
}
