package aufgabe4;


import java.awt.*;
import java.awt.event.*;

import aufgabe4.Aufgabe4.Sorting;


public class GUI extends Frame {

	TextField sourceFileText;
	
	TextField[] outputMax;
	TextField[] outputAvg;

	int width = 1280;
	int height = 720;
	
	
	public GUI() {
		
		int differentVersions = Sorting.values().length;
		
		outputMax = new TextField[differentVersions];
		outputAvg = new TextField[differentVersions];
		
		// Setup frame
        setTitle("Aufgabe 4");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
                System.exit(0);
            }

        });
		
        // Set up the main panel which houses all UI-elements
        Panel mainPanel = new Panel(new GridLayout(1, 4));
	        mainPanel.setPreferredSize(new Dimension(width, height));
	        mainPanel.setMinimumSize(new Dimension(width, height));
	        mainPanel.setMaximumSize(new Dimension(width, height));
	        mainPanel.setBackground(new Color(255, 255, 255));
        
        
        // Panel for the diferent sorting types
        Panel firstPanel = new Panel(new GridLayout(1 + differentVersions, 1));
        	// Leave one empty cell
        	firstPanel.add(new Panel());
        	// Add labels for each sorting type
        	int count = 0;
        	for (Sorting i : Sorting.values()) {
        		
        		Label l = new Label(i.toString());
        		l.setBackground((count % 2 == 0) ? new Color(255,255,255) : new Color(200,200,200));
        		l.setAlignment(Label.CENTER);
        		firstPanel.add(l);
        		count++;
        		
        	}
        mainPanel.add(firstPanel);
        
        // Panel for the output texts of the maximum duration
        Panel secondPanel = new Panel(new GridLayout(1 + differentVersions, 1));
	        secondPanel.add(new Label("Maximale Wartezeit (in min)"));
	        
	        // Add output textFields for each sorting type
        	for (int i = 0; i < differentVersions; i++) {
        		
        		TextField t = new TextField();
        		outputMax[i] = t;
        		t.setBackground((i % 2 == 0) ? new Color(255,255,255) : new Color(200,200,200));
        		t.setEditable(false);
        		secondPanel.add(t);
        		
        	}
	        
        mainPanel.add(secondPanel);
        
        // Panel for the output texts of the average duration
        Panel thirdPanel = new Panel(new GridLayout(1 + differentVersions, 1));
        	thirdPanel.add(new Label("Durchschnittliche Wartezeit (in min)"));
        	// Add output textFields for each sorting type
        	for (int i = 0; i < differentVersions; i++) {
        		
        		TextField t = new TextField();
        		outputAvg[i] = t;
        		t.setBackground((i % 2 == 0) ? new Color(255,255,255) : new Color(200,200,200));
        		t.setEditable(false);
        		thirdPanel.add(t);
        		
        	}
        mainPanel.add(thirdPanel);
        
        // Panel for user input
        Panel inputPanel = new Panel(new GridLayout(3, 1));
	        Button chooseInputTextButton = new Button("Wähle eine Eingabedatei");
	        chooseInputTextButton.addActionListener(new ActionListener() {
	
	            public void actionPerformed(ActionEvent e) {
	                
	                // Ask for a place and name to export to
					FileDialog fileDialog = new FileDialog(Aufgabe4.GUI, "Wähle eine Datei", FileDialog.LOAD);
	                fileDialog.setVisible(true);
	                
	                // Cancel
	                if (fileDialog.getFile() == null) return;
	
	                if (!fileDialog.getFile().endsWith(".txt")) {
	                	sourceFileText.setText("Wähle eine \".txt\"-Datei");
	                	return;
	                }
	                
	                sourceFileText.setText(fileDialog.getDirectory() + fileDialog.getFile());
	                
	            }
	
	        });
	        inputPanel.add(chooseInputTextButton);
	        
	        sourceFileText = new TextField();
	        inputPanel.add(sourceFileText);
	        
	        // Create the button to calculate maximum and average times
	        Button runButton = new Button("Bearbeite die Aufträge!");
	        runButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	
	            	for (int i = 0; i < differentVersions; i++) {
	            		
	            		Aufgabe4.sortingValue = Sorting.values()[i];
	            		Aufgabe4.run(outputMax[i], outputAvg[i]);
	            		
	            	}
	            
	            }
	        });
	        inputPanel.add(runButton);
        // Add the input to the main panel
        mainPanel.add(inputPanel);
        
        // Finally
        add(mainPanel);
        pack();
        setVisible(true);
        
	}

}
