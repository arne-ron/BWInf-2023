package aufgabe2;


import java.util.HashMap;
import java.util.ArrayList;
import java.lang.Thread;


public class Aufgabe2 {

    // Size of the simulated area
    static int width = 300;
    static int height = 300;
    static int scale = 2;
    static int time = 0;
    static GUI GUI;
    
    // Maps to store the Points during the simulation
    static HashMap<String, Point> open = new HashMap<String, Point>();
    static HashMap<String, Point> closed = new HashMap<String, Point>();
    static HashMap<String, Point> empty = new HashMap<String, Point>();
    static Thread thread;

    
    public static void main(String[] args) {

        GUI = new GUI();
        GUI.simDisplay.scale = scale;

    }
    

    // Setup
    static void startingSetup(int numberOfPoints, int minGrowth, int maxGrowth) {

        // Fill every position on the grid in the simulated area with a Point
        for (int x = 0; x < width; x++) {

            for (int y = 0; y < height; y++) {

                empty.put(xyString(x, y), new Point(x, y));
            }

        }

        open = new HashMap<String, Point>();
        closed = new HashMap<String, Point>();

        // Initialize the starting Points
        ArrayList<Point> startingPoints = new ArrayList<Point>();

        // Create random Points according to the input
        for (int i = 0; i < numberOfPoints; i++) {

            startingPoints.add(new Point(maxRand(width), maxRand(height), minGrowth + maxRand(maxGrowth-minGrowth), minGrowth + maxRand(maxGrowth-minGrowth),
                minGrowth + maxRand(maxGrowth-minGrowth), minGrowth + maxRand(maxGrowth-minGrowth)));

        }

        // Move the starting Points from empty to open
        for (Point i : startingPoints) {

            Point newPoint = empty.remove(xyString(i.x, i.y));
            if (newPoint == null) return;
            newPoint.copyValues(i);
            open.put(xyString(i.x, i.y), newPoint);

        }

    }

    
    // Gives a random number between 0 and maxValue
    static int maxRand(int maxValue) {

        return (int) (Math.random() * maxValue);

    }


    // Returns a string in the format "x,y" used as keys in the maps
	static String xyString(int x, int y) {

		return Integer.toString(x) + "," + Integer.toString(y);

	}


    // Runs one cycle of the simulation
	static void nextCycle() {
		
        time++;

        // Temporary maps to store Points, without disrupting the foreach loops
        HashMap<String, Point> moveToOpen = new HashMap<String, Point>();
        HashMap<String, Point> moveToClosed = new HashMap<String, Point>();

        // Simulate the growing behaviour for each Point which is open (has a free space to grow towards)
        for (Point i : open.values()) {

            // Growing in all 4 directions, when enough time has passed
            // North (-y)
            if ((time - i.startTime) % i.speedN == 0) { // Grow, if the age is equal to the grow speed/delay

                String newPos = xyString(i.x, i.y - 1);

                // Check if the new Point is in empty and therefore available
                if (empty.containsKey(newPos)) {

                    // move the Point from empty to open and  values of the parent Point
                    Point newPoint = empty.remove(newPos);
                    newPoint.copyValues(i);
                    newPoint.startTime = time;
                    moveToOpen.put(newPos, newPoint);

                }
                
                // Log that one direction has already grown or was occupied
                i.transformed.put("N", true);

            }

            // East (+x)
            if ((time - i.startTime) % i.speedE == 0) {

                String newPos = xyString(i.x + 1, i.y);
                if (empty.get(newPos) != null) {

                    Point newPoint = empty.remove(newPos);
                    newPoint.copyValues(i);
                    newPoint.startTime = time;
                    moveToOpen.put(newPos, newPoint);

                }
                i.transformed.put("E", true);

            }
            
            // South (+y)
            if ((time - i.startTime) % i.speedS == 0) {

                String newPos = xyString(i.x, i.y + 1);
                if (empty.get(newPos) != null) {
                	
                    Point newPoint = empty.remove(newPos);
                    newPoint.copyValues(i);
                    newPoint.startTime = time;
                    moveToOpen.put(newPos, newPoint);
                }
                i.transformed.put("S", true);

            }

            // West (-x)
            if ((time - i.startTime) % i.speedW == 0) {

                String newPos = xyString(i.x - 1, i.y);
                if (empty.get(newPos) != null) {

                    Point newPoint = empty.remove(newPos);
                    newPoint.copyValues(i);
                    newPoint.startTime = time;
                    moveToOpen.put(newPos, newPoint);

                }
                i.transformed.put("W", true);

            }
            
            // If one Point can't grow in any direction, it moves to the
            // closed list and dosn't get computed any further
            if (i.transformed.size() == 4) {

                moveToClosed.put(xyString(i.x, i.y), open.get(xyString(i.x, i.y)));

            }

        }
        
        // End the simulation, if all Points are simulated
        if (empty.isEmpty()) {

            if (thread.isAlive()) {
            	thread.interrupt();
            }
            GUI.simFinishedDialog.setVisible(true);
            return;

        }
        
        // Finally move the Points in the different maps
        open.putAll(moveToOpen);
        for (String s :  moveToClosed.keySet()) {
            
            open.remove(s);

        }
        closed.putAll(moveToClosed);
        
        // Update the screen
        GUI.simDisplay.repaint();
        
    }

}