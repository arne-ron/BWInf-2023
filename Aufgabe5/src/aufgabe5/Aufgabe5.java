package aufgabe5;


import java.awt.TextArea;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;


public class Aufgabe5 {
	
	static GUI GUI;
	static TextArea output;
	
	
	public static void main(String[] args) {
		
		GUI = new GUI();
		output = GUI.outputText;
	
	}
	
	
	@SuppressWarnings("unlikely-arg-type")
	public static void run() {
		
		// setup	
		output.setText("");
		
		// Initialize variables
		String input;
		int[][] parcours;
		boolean possible = false;
		int firstCycleTime = -1;
		int firstCycleStart = -1;
		int secondCycleTime = -1;
		int secondCycleStart = -1;
		ArrayList< ArrayList< ArrayList<Integer> > > firstArr = new ArrayList<>();
		ArrayList< ArrayList< ArrayList<Integer> > > secondArr = new ArrayList<>();
		
		// Input
		try {
			input = new String(Files.readAllBytes(Paths.get(GUI.sourceFileText.getText())));
			input = input.replace("\r", "\n");
		} catch (IOException e) {
			GUI.sourceFileText.setText("Die Datei wurde nicht gefunden");
			return;
		}
		String[] inputSplit = input.split("\n");
		parcours = new int[inputSplit.length][2];
		int count = 0;
		for (String i : inputSplit) {
			
			if (i.equals("")) continue;
			parcours[count][0] = Integer.parseInt(i.split(" ")[0]);
			parcours[count][1] = Integer.parseInt(i.split(" ")[1]);
			count++;
			
		}
		
		
		// Add starting points
		firstArr.add(toArrayList(toArrayList(1)));
		secondArr.add(toArrayList(toArrayList(2)));
		
		// END SETUP
		
		
		for (int t = 0; true; t++) {
			
			// ADVANCE FIRST PERSON
			ArrayList< ArrayList<Integer> > temporaryFirst = new ArrayList<>();
			
			for (ArrayList<Integer> currentField : getLast(firstArr)) {
				
				// Check each
				for (int[] i : parcours) {
					
					if (i[0] == getLast(currentField) && !temporaryFirst.contains(i[1])) {
						
						ArrayList<Integer> temp = new ArrayList<>(currentField);
						temp.add(i[1]);
						
						// Add to temporaryFirst only if it's a new final destination
						boolean duplicateFinalDestination = false;
						for (ArrayList<Integer> j : temporaryFirst) {
							
							if (getLast(j) == i[1]) duplicateFinalDestination = true;
							
						}
						if (!duplicateFinalDestination) temporaryFirst.add(temp);
						
					}
					
				}	
				
			}

			// Check if first has nodes to go to
			if (temporaryFirst.isEmpty()) {
				
				break;
				
			}
			
			firstArr.add(temporaryFirst);

			// Check for the existence and length of loops
			if (firstCycleTime == -1) {
				
				ArrayList<Integer> lastDestinations = new ArrayList<>();
				
				for (ArrayList<Integer> destination : getLast(firstArr)) {
					
					lastDestinations.add(getLast(destination));
					
				}
				
				Collections.sort(lastDestinations);
				
				for (int i = 0; i < firstArr.size() - 1; i++) {
					
					ArrayList<Integer> currentDestinations = new ArrayList<>();
					ArrayList<ArrayList<Integer>> currentStep = firstArr.get(i);
					
					for (ArrayList<Integer> destination : currentStep) {
						
						currentDestinations.add(getLast(destination));
						
					}
					
					Collections.sort(currentDestinations);
					
					if (currentDestinations.equals(lastDestinations)) {
						
						firstCycleTime = t - firstArr.indexOf(currentStep) + 1;
						firstCycleStart = t;
						break;
						
					}
					
				}
	
			}
			
			
			// ADVANCE SECOND PERSON
			ArrayList< ArrayList<Integer> > temporarySecond = new ArrayList<>();
			
			for (ArrayList<Integer> currentField : getLast(secondArr)) {
				
				// Check each
				for (int[] i : parcours) {
					
					if (i[0] == getLast(currentField) && !temporarySecond.contains(i[1])) {
						
						ArrayList<Integer> temp = new ArrayList<>(currentField);
						temp.add(i[1]);
						
						// Add to temporaryFirst only if it's a new final destination
						boolean duplicateFinalDestination = false;
						for (ArrayList<Integer> j : temporarySecond) {
							
							if (getLast(j) == i[1]) duplicateFinalDestination = true;
							
						}
						if (!duplicateFinalDestination) temporarySecond.add(temp);
							
							
					}
						
				}
					
			}
			
			// Check if second has nodes to go to
			if (temporarySecond.isEmpty()) {
				
				break;
				
			}
			
			secondArr.add(temporarySecond);

			// Check for the existence and length of loops
			if (secondCycleTime == -1) {
				
				ArrayList<Integer> lastDestinations = new ArrayList<>();
				
				for (ArrayList<Integer> destination : getLast(secondArr)) {
					
					lastDestinations.add(getLast(destination));
					
				}
				
				Collections.sort(lastDestinations);
				
				for (int i = 0; i < secondArr.size() - 1; i++) {
					
					ArrayList<Integer> currentDestinations = new ArrayList<>();
					ArrayList<ArrayList<Integer>> currentStep = secondArr.get(i);
					
					for (ArrayList<Integer> destination : currentStep) {
						
						currentDestinations.add(getLast(destination));
						
					}
					
					Collections.sort(currentDestinations);
					
					if (currentDestinations.equals(lastDestinations)) {
						
						secondCycleTime = t - secondArr.indexOf(currentStep) + 1;
						secondCycleStart = t;
						break;
						
					}
					
				}
	
			}
						
			
			
			// CHECK MATCHING
			for (ArrayList<Integer> i : getLast(firstArr)) {
				
				for (ArrayList<Integer> j : getLast(secondArr)) {
					
					if (getLast(j) == getLast(i)) {
						
						output.append("Sie treffen sich auf Feld " + getLast(i) + " nach " + (i.size()-1) + " Schritt(en) \n");
						output.append("Sasha muss auf folgende Felder springen: \n" + i + "\n");
						output.append("Mika muss auf folgende Felder springen: \n" + j + "\n");
						possible = true;
						break;
						
					}
					
				}
				
				if (possible) break;
				
			}
			
			if (possible) break;
			
			// Break if all cyclic combinations were checked
			if (firstCycleTime != -1 && secondCycleTime != -1) {
				
				if (t >= Math.max(firstCycleStart, secondCycleStart) + 10 + kgV(firstCycleTime, secondCycleTime)) {
					
					break;
					
				}
				
			}
			
		} // END WHILE LOOP
		
		if (!possible) output.append("Der Parcours kann nicht absolviert werden");
		
	}
		
	
	public static <E> E getLast(ArrayList<E> input) {
		return input.get(input.size() - 1);
	}

	
	// Gibt den größten gemeinsamen Teiler aus
	public static int ggT(int a, int b) {
		if (b == 0) {
			return a;
		} else {
			return ggT(b, a%b);
		}
	}
	
	
	// Gibt das kleinste gemeinsame Vielfache aus
	public static int kgV(int a, int b) {
		return Math.abs(a / ggT(a, b) * b);
	}

	
	// Create a ArrayList containing the content 
	public static <E> ArrayList<E> toArrayList(E content) {
		ArrayList<E> list = new ArrayList<>();
		list.add(content);
		return list;
	}
	
}
