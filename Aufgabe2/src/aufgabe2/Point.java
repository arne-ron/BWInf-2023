package aufgabe2;


import java.awt.Color;
import java.util.HashMap;


class Point {

    // Position
    int x;
    int y;
    // Color of the crystal
    Color color;
    // Spreading speed in ticks per spread
    int speedN = 1;
    int speedE = 1;
    int speedS = 1;
    int speedW = 1;
    // Time of birth
    int startTime = 0;
    // Keep track of tansformable fields to save computing power
    HashMap<String, Boolean> transformed = new HashMap<String, Boolean>();
    
    // Costructors
    public Point(int x, int y) {

        this.x = x;
        this.y = y;

        // Assign a random gray tone
        int rndmValue = (int) (20 + Math.random() * 215);
        color = new Color(rndmValue, rndmValue, rndmValue);

    }

    public Point(int x, int y, int speedN, int speedE, int speedS, int speedW) {

        this.x = x;
        this.y = y;
        this.speedN = speedN;
        this.speedE = speedE;
        this.speedS = speedS;
        this.speedW = speedW;

        // Assign a random gray tone
        int rndmValue = (int) (20 + Math.random() * 215);
        color = new Color(rndmValue, rndmValue, rndmValue);     

    }


    public void copyValues(Point parent) {

        color = parent.color;

        speedN = parent.speedN;
        speedE = parent.speedE;
        speedS = parent.speedS;
        speedW = parent.speedW;
    }
}