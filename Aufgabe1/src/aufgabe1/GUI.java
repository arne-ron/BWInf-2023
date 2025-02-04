package aufgabe1;


import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class GUI extends Frame {

	TextArea outputText;
	TextField sourceFileText;
	TextField patternText;
	
	public GUI() {
		
		// Setup frame
        setTitle("Aufgabe 1");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
                System.exit(0);
            }

        });
		
        // Set up the main panel which houses all UI-elements
        Panel mainPanel = new Panel(new GridLayout(1, 2));
        mainPanel.setPreferredSize(new Dimension(800, 300));
        mainPanel.setMinimumSize(new Dimension(800, 300));
        mainPanel.setMaximumSize(new Dimension(800, 300));
        mainPanel.setBackground(new Color(255, 255, 255));
        
        // Set up the output panel for displaying the results
        outputText = new TextArea();
        outputText.setEditable(false);
        mainPanel.add(outputText);
        
        // Panel for user input
        Panel inputPanel = new Panel(new GridLayout(5, 1));
        
        Button chooseInputTextButton = new Button("Wähle die zu durchsuchende Datei");
        chooseInputTextButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                
                // Ask for a place and name to export to
                FileDialog fileDialog = new FileDialog(Aufgabe1.GUI, "Wähle eine Datei", FileDialog.LOAD);
                fileDialog.setVisible(true);
                
                // Cancel
                if (fileDialog.getFile() == null) return;

                if (!fileDialog.getFile().endsWith(".txt")) {
                	sourceFileText.setText("Bitte  wähle eine \".txt\"-Datei");
                	return;
                }
                
                sourceFileText.setText(fileDialog.getDirectory() + fileDialog.getFile());
                
            }

        });
        inputPanel.add(chooseInputTextButton);
        
        sourceFileText = new TextField();
        inputPanel.add(sourceFileText);
        
        Button choosePatternButton = new Button("Wähle die Lückentext-Datei");
        choosePatternButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                
                // Ask for a place and name to export to
                FileDialog fileDialog = new FileDialog(Aufgabe1.GUI, "Wähle eine Datei", FileDialog.LOAD);
                fileDialog.setVisible(true);
                
                // Cancel
                if (fileDialog.getFile() == null) return;
                
                if (!fileDialog.getFile().endsWith(".txt")) {
                	patternText.setText("Bitte wähle eine \".txt\"-Datei oder gib den Lückentext ein");
                	return;
                }
                
                try {
					patternText.setText(new String(Files.readAllBytes(Paths.get(fileDialog.getDirectory()+fileDialog.getFile()))));
				} catch (IOException err) {
					err.printStackTrace();
		        }

            }

        });
        inputPanel.add(choosePatternButton);
        
        patternText = new TextField("oder gib den Lückentext ein");
        inputPanel.add(patternText);
        
        // Create the button to search for pattern
        Button runSimButton = new Button("Starte die Suche!");
        runSimButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Aufgabe1.run();
            }
        });
        inputPanel.add(runSimButton);
        
        // Add the input to the main panel
        mainPanel.add(inputPanel);
        
        // Finally
        add(mainPanel);
        pack();
        setVisible(true);
        
	}

}
