package aufgabe2;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


@SuppressWarnings("serial")
class GUI extends Frame {

    int PointSize = 1;
    Dialog simFinishedDialog;
    MyCanvas simDisplay;
    TextField pointCount;
    TextField minGrowth;
    TextField maxGrowth;
    TextField simSpeed;

    // Constructor
    public GUI() {

        // Setup frame
        setTitle("Aufgabe 2");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
                System.exit(0);
            }

        });
        
        // Set up the main panel which houses all UI-elements
        Panel mainPanel = new Panel(new GridLayout(1, 2));
        mainPanel.setSize(Aufgabe2.width * Aufgabe2.scale * 2, Aufgabe2.height * Aufgabe2.scale);
        mainPanel.setBackground(new Color(0, 120, 70));

        // Set up the canvas to draw the points on
        simDisplay = new MyCanvas();
        simDisplay.setPreferredSize(new Dimension(Aufgabe2.width * Aufgabe2.scale, Aufgabe2.height * Aufgabe2.scale));
        simDisplay.setMinimumSize(new Dimension(Aufgabe2.width * Aufgabe2.scale, Aufgabe2.height * Aufgabe2.scale));
        simDisplay.setMaximumSize(new Dimension(Aufgabe2.width * Aufgabe2.scale, Aufgabe2.height * Aufgabe2.scale));
        
        // Set up panel for controlling the simulation
        Panel controllerPanel = new Panel(new GridLayout(2, 1));
        
        // Panel for inputting the simulation variables
        Panel inputPanel = new Panel(new GridLayout(4, 1));

            pointCount = new TextField("Number of Points: 250");
            inputPanel.add(pointCount);
            minGrowth = new TextField("Min growth time: 17");
            inputPanel.add(minGrowth);
            maxGrowth = new TextField("Max growth time: 50");
            inputPanel.add(maxGrowth);
            simSpeed = new TextField("Simulation speed: 10ms");
            inputPanel.add(simSpeed);

        controllerPanel.add(inputPanel);

        // Panel for the buttons
        Panel buttonPanel = new Panel(new GridLayout(1, 2));
        // Create the button to run the simulation
        Button runSimButton = new Button("Run Simulation");
        runSimButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	
                // Get the values (default or user input) from the input panel, start and run the simulation
                Aufgabe2.startingSetup(getInts(pointCount.getText()), getInts(minGrowth.getText()), getInts(maxGrowth.getText()));
                
                simDisplay.bufferedImage = new BufferedImage(Aufgabe2.width * Aufgabe2.scale, Aufgabe2.height * Aufgabe2.scale, BufferedImage.TYPE_INT_ARGB);
                
                // Start the reoccuring execution of the cycles
                Aufgabe2.thread = new Thread() {
                	
                	@Override
                	public void run() {
                		while(true) {
                			Aufgabe2.nextCycle();
                			if (Aufgabe2.thread.isInterrupted()) break;
                			try {
								Thread.sleep(getInts(simSpeed.getText()));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
                		}
                	}
                	
                };
                Aufgabe2.thread.start();
            
            } // End action performed

        }); // End action listener
        buttonPanel.add(runSimButton);
        
        // Create the button to save the simulation
        Button saveImgButton = new Button("Save as Image");
        saveImgButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                
                // Ask for a place and name to export to
                FileDialog fileDialog = new FileDialog(Aufgabe2.GUI, "Save as", FileDialog.SAVE);
                fileDialog.setFile("Simulation001");
                fileDialog.setVisible(true);
                
                // Cancel
                if (fileDialog.getFile() == null) return;

                // Save as PNG
                File file = new File(fileDialog.getDirectory() + fileDialog.getFile() + ".png");
                try {
                    ImageIO.write(simDisplay.bufferedImage, "png", file);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

        });
        buttonPanel.add(saveImgButton);

        controllerPanel.add(buttonPanel);
        
        // Dialog for when the simulation is finished
        simFinishedDialog = new Dialog(this, "Simulation finished", true);
        simFinishedDialog.setLayout(new BorderLayout());
        simFinishedDialog.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {

                e.getWindow().dispose();

            }

        });
        simFinishedDialog.add(new Label("The simulation is done!"));
        simFinishedDialog.setSize(200, 100);

        mainPanel.add(simDisplay);
        mainPanel.add(controllerPanel);

        add(mainPanel);
        pack();
        setVisible(true);

    }


    // Formats the int to be 3 digit (ie. 4 -> 004)
    public String formated(int i) {

        if (i < 0) throw new Error("Invalid argument, i must be > 0");
        if (i < 10) return ("00" + i);
        if (i < 100) return ("0" + i);
        return Integer.toString(i);

    }


    // Return all integers of a string
    public int getInts(String text) {

        return Integer.parseInt(text.replaceAll("[^0-9]", ""));

    }
}
