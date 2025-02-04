package aufgabe5;



import java.awt.*;
import java.awt.event.*;


public class GUI extends Frame {

	TextArea outputText;
	TextField sourceFileText;
	
	public GUI() {
		
		// Setup frame
        setTitle("Aufgabe 5");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
                System.exit(0);
            }

        });
		
        // Set up the main panel which houses all UI-elements
        Panel mainPanel = new Panel(new GridLayout(1, 2));
        mainPanel.setPreferredSize(new Dimension(700, 300));
        mainPanel.setMinimumSize(new Dimension(700, 300));
        mainPanel.setMaximumSize(new Dimension(700, 300));
        mainPanel.setBackground(new Color(255, 255, 255));
        
        // Set up the output panel for displaying the results
        outputText = new TextArea();
        outputText.setEditable(false);
        mainPanel.add(outputText);
        
        // Panel for user input
        Panel inputPanel = new Panel(new GridLayout(3, 1));
        
        Button chooseInputTextButton = new Button("Wähle die Eingabe-Datei");
        chooseInputTextButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                
                // Ask for a place and name to export to
                FileDialog fileDialog = new FileDialog(Aufgabe5.GUI, "Wähle eine Datei", FileDialog.LOAD);
                fileDialog.setVisible(true);
                
                // Cancel
                if (fileDialog.getFile() == null) return;

                if (!fileDialog.getFile().endsWith(".txt")) {
                	sourceFileText.setText("Bitte wähle eine \".txt\"-Datei");
                	return;
                }
                
                sourceFileText.setText(fileDialog.getDirectory() + fileDialog.getFile());
                
            }

        });
        inputPanel.add(chooseInputTextButton);
        
        sourceFileText = new TextField();
        inputPanel.add(sourceFileText);
        
        // Create the button to test the parcours
        Button runSimButton = new Button("Teste den Parcours!");
        runSimButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Aufgabe5.run();
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
