package aufgabe4;


import java.awt.TextField;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Aufgabe4 {
	
	enum Sorting {
		EINGANGSZEIT_MIN,
		DAUER_MIN,
		ABWECHSELND,
		GEWICHTET_20_80,
		GEWICHTET_40_60,
		GEWICHTET_60_40,
		GEWICHTET_80_20,
	}

	static int time;					// Global time
	static int timeWorked;				// How long was already worked that day
	static Assignment current;			// The assignment that is worked on at the moment
	static ArrayList<Assignment> todoTotal;		// The list with all assignments total
	static ArrayList<Assignment> todo;	// The list with all assignments that are pending at the current time
	static ArrayList<Assignment> done;	// The list with all done assignments
	static Sorting sortingValue; 		// Determines what property to sort by (0: recieved, 1: duration)
	static int auxilary = 0;			// Secondary value for sorting
	
	final static int MAXWORK = 480;		// Minutes of work per day
	final static int NIGHT = 960;		// Minutes of break between each day
	
	static GUI GUI;
	
	public static void main(String[] args) {
		
		GUI = new GUI();
	
	}
	
	
	// The code run by the run buton in the GUI
	public static void run(TextField outMax, TextField outAvg) {
		
		time = 0;
		timeWorked = 0;
		
		String input;
		try {
			
			input = new String(Files.readAllBytes(Paths.get(GUI.sourceFileText.getText())));
			input = input.replace("\r", "\n");
			
		} catch (IOException e) {
			
			GUI.sourceFileText.setText("The input file wasn't found");
			return;
			
		}
		String[] inputSplit = input.split("\n");
		todoTotal = new ArrayList<>();
		todo = new ArrayList<>();
		done = new ArrayList<>();
		for (String i : inputSplit) {
			
			String[] temp = i.split(" ");
			todoTotal.add(new Assignment(Integer.parseInt(temp[0]), Integer.parseInt(temp[1])));
			
		}
		

		
		todo.add(todoTotal.remove(0));
		
		// Simulation of the work day
		current = todo.get(0);
		// Wait for the next assignment to be recieved
		time = current.recieved;
		int toOneDay = time % (MAXWORK + NIGHT);
		if (toOneDay < MAXWORK) {
			timeWorked = toOneDay;
		} else {
			timeWorked = 0;
			time += (MAXWORK + NIGHT) - toOneDay;
		}
		
		while (!todo.isEmpty() || !todoTotal.isEmpty()) {
			
			// Work on the bikes
			// Finish the current assignment if there's enough time
			if (current.remaining <= MAXWORK - timeWorked) {
				
				timeWorked += current.remaining;
				time += current.remaining;
				current.addProgress(current.remaining);
				current.finished = time;
				
				done.add(current);
				todo.remove(current);
				
				// Only have assignments that have already been given 
				ArrayList<Assignment> temp = new ArrayList<>();
				if (!todoTotal.isEmpty()) {
					for (Assignment i : todoTotal) {
						
						if (i.recieved <= time) {
							
							temp.add(i);
							
						}
						
					}
					todoTotal.removeAll(temp);
					todo.addAll(temp);
				}
				
				sort(todo, 0, todo.size() - 1);
				
				if (todo.isEmpty() && todoTotal.isEmpty()) break;
				
				// Wait for the next assignment
				if (todo.isEmpty()) {
					
					todo.add(todoTotal.remove(0));
					
					// Skip ahead towards the time of recieving a new assignment
					time = todo.get(0).recieved;
					toOneDay = time % (MAXWORK + NIGHT);
					if (toOneDay < MAXWORK) {
						timeWorked = toOneDay;
					} else {
						timeWorked = 0;
						time += (MAXWORK + NIGHT) - toOneDay;
					}
					
				}
				
				current = todo.get(0);
				auxilary = (auxilary == 0) ? 1 : 0; 
				
			// Or work the whole day and wait for the next
			} else {
				
				current.addProgress(MAXWORK - timeWorked);
				time += MAXWORK - timeWorked + NIGHT;
				timeWorked = 0;
				
			}
		
		}
		
		
		outMax.setText(Integer.toString(getMaxDuration(done)));
		outAvg.setText(Integer.toString((int) getAvgDuration(done)));
		
	}


	// Returns the average duration of the arrays assignments
	public static float getAvgDuration(ArrayList<Assignment> arr) {
		
		float average = 0;
		
		for (Assignment a : arr) {
			
			average += a.finished - a.recieved;
			
		}
		
		average /= arr.size();
		
		return average;
		
	}
	
	
	// Returns the maximum duration of the arrays assignments
	public static int getMaxDuration(ArrayList<Assignment> arr) {
		
		int max = arr.get(0).finished - arr.get(0).recieved;
		
		for (Assignment a : arr) {
			
			if (a.finished - a.recieved > max) max = a.finished - a.recieved;
		}
				
		return max;
		
	}
	
	
	// Sorts an array of assignments using the quicksort algorithm by the global defined parameter
	// in a section which is defined by start and finish
	public static void sort(ArrayList<Assignment> arr, int start, int end) {
		
		if (start < 0 || end >= arr.size()) throw new IndexOutOfBoundsException("The values of start=" + start + " and end=" + end + " must be within the list");
		
		if (arr.size() <= 1 || start >= end) return;
		
		int temp = partition(arr, start, end);
		
		sort(arr, start, temp - 1);
		sort(arr, temp + 1, end);
		
	}
	
	
	// Rearanges the values from start to end where elements smaller or larger than the pivot get put before and after it
	// Returns the index of the pivot
	// Partition for the quicksort algorithm
	public static int partition(ArrayList<Assignment> arr, int start, int end) {
		
		int pivotIndex = start;
		int pivotValue = getCompValue(arr.get(end));
		
		for (int i = start; i < end; i++) {
			
			int value = getCompValue(arr.get(i));
			
			if (value < pivotValue) {
				
				swap(arr, pivotIndex, i);
				pivotIndex++;
				
			}
			
		}
		
		swap(arr, pivotIndex, end);
		
		return pivotIndex;
		
	}
	
	
	// Returns the value for sorting defined by sortingValue
	public static int getCompValue(Assignment a) {
		
		switch (sortingValue) {
			case EINGANGSZEIT_MIN:
				return a.recieved;
			case DAUER_MIN:
				return a.duration;
			case ABWECHSELND:
				return (auxilary == 0) ? a.recieved : a.duration;
			default:
				double percentage = Integer.parseInt(sortingValue.name().split("_")[1]) / 100.0;
				return (int) ((a.recieved - time) / 5 * percentage + a.duration * (1-percentage));
		}
		
	}
	
	
	// Swaps the given indecies of the given array
	// Swaps the contents of the given indecies
	public static <E> void swap(ArrayList<E> arr, int indexFirst, int indexSecond) {
		
		E temp = arr.get(indexFirst);
		arr.set(indexFirst, arr.get(indexSecond));
		arr.set(indexSecond, temp);
		
	}
	
}


































































