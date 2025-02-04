package aufgabe1;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Aufgabe1 {

	static GUI GUI;
	
	
	// Main method that initializes the GUI
    public static void main(String[] args) {
        
    	GUI = new GUI();
    	
    }
    
    
    // Returns all matching patterns
    public static ArrayList<String> findAll(String input, String pattern) {
        
    	// Formats the input to delete and change unwanted characters
        input = formated(input);
        String inputLowercase = input.toLowerCase();
        
        ArrayList<String> returnValues = new ArrayList<>();

        // Seperates the pattern in an array of single words
        String[] patternWords = pattern.split(" ");
        
        // Find the first index of the first word
        ArrayList<Integer> iof = new ArrayList<>();

        // First possible occurance of first word
        int nextWordIndex = inputLowercase.indexOf(patternWords[0]);

        // Every word is accepted
        if (patternWords[0].equals("_")) nextWordIndex = 0;
            
        // Find and validate all possible locations of first words
        while (nextWordIndex >= 0) {
            
            if (patternWords[0].equals("_")) {
                
                iof.add(nextWordIndex);
                
                // Set nextWordIndex to the beginning of the next word
                nextWordIndex = inputLowercase.indexOf(" ", nextWordIndex) + 1;
                
                // No next Word (indexOf has returned -1)
                if (nextWordIndex == 0) break;
                
            } else {
                
            	// Ensure it is the whole word and not a composite of it (run and not run(-ner))
                if (nextWordIndex + patternWords[0].length() == input.length() || inputLowercase.charAt(nextWordIndex + patternWords[0].length()) == ' ') {
                    
                    iof.add(nextWordIndex);
    
                } 

                // Set nextWordIndex to the beginning of the next word
                nextWordIndex = inputLowercase.indexOf(patternWords[0], nextWordIndex + 1);

            }

        }
        
        // ---------
         
        // For each first matching word
        for (int beginningWordIndex : iof) {

            boolean missmatch = false;

            int currentIndex = beginningWordIndex;

            // For each pattern word, check if the next word matches the pattern
            for (int i = 1; i < patternWords.length; i++) {

                // Set current index to the beginning of the next word
                currentIndex = input.indexOf(" ", currentIndex) + 1;

                // break the loop if one word dosn't match
                if (!matches(input, patternWords[i], currentIndex, false)) {
                    
                    missmatch = true;
                    break;
                    
                }
                
            }
            
            if (missmatch) continue;
            
            // If the previous loop didn't broke, the current
            // beginning word must be one of the matching phrases
            
            // Get the whole sentence at the index
            currentIndex = beginningWordIndex;
            for (int i = 1; i <= patternWords.length; i++) {
            	
                // Set current index to the beginning of the next word
                currentIndex = input.indexOf(" ", currentIndex + 1);
                if (currentIndex == -1) currentIndex = input.length();
                
            }
            
            // Add the sentence to the solutions
            returnValues.add(input.substring(beginningWordIndex, currentIndex));

        }

        // Retun the solutions
        return removeDuplicats(returnValues);

    }
    
    
    // Check if the given string matches the input directly at the fromIndex
    public static boolean matches(String input, String pattern, int fromIndex, boolean caseSensitive) {
        
        if (fromIndex == 0) return false;

        //makes all characters lowercase
        if (!caseSensitive) {

            pattern = pattern.toLowerCase();
            input = input.toLowerCase();

        }

        // Any word matches but there has to be at least one word remaining in the string
        if (pattern.matches("_") && input.length() > fromIndex) return true;
        
        // Check if the pattern matches the word at from index and
        // ensure it is the whole word and not a composite of it (run and not run(-ner))
        if (input.indexOf(pattern, fromIndex) == fromIndex && (fromIndex + pattern.length() == input.length() || input.charAt(fromIndex + pattern.length()) == ' ')) {

            return true;

        }

        return false;

    }


    // Deletes unwanted characters and replaces "\n" with " "
    public static String formated(String input) {

        input = input.replaceAll("\n", " ");
        input = input.replaceAll("[^\\w .!?àÀâÂäÄáÁéÉèÈêÊëËìÌîÎïÏòÒôÔöÖùÙûÛüÜç'ñß]", "");
        
        return input;

    }
    
    
    // Removes duplicates from an ArrayList
    public static <E> ArrayList<E> removeDuplicats(ArrayList<E> list) { 
        ArrayList<E> newList = new ArrayList<>();
        
        for (E i : list) {
        	
        	if (!newList.contains(i)) newList.add(i);
        	
        }
        
        return newList;
    }
    
    
    // Method which finds the matching patterns and is run by the run button
    public static void run() {
    	
    	String input = null;
		try {
			input = new String(Files.readAllBytes(Paths.get(GUI.sourceFileText.getText())));
		} catch (IOException e) {
			GUI.outputText.setText("The input file wasn't found");
			return;
		}
    	ArrayList<String> returnValues = findAll(input, GUI.patternText.getText());
        
        // Output
        switch (returnValues.size()) {
            
            // No matching sequence
            case 0:
	            GUI.outputText.setText("Kein Textabschnitt passt zu dem gesuchten Muster");
	            return;
            
            // One matching sequence
            case 1:
	            GUI.outputText.setText("Die gesuchte Textpassage lautet: \n" + returnValues.get(0));
	            return;
            
            // Multiple matching sequences
            default:
	            GUI.outputText.setText("Alle möglichen Textpassagen sind: ");
	            for (String string : returnValues) {
	            	 GUI.outputText.setText(GUI.outputText.getText() + "\n" + string);
	            }
            
        }
        
    }
    
}