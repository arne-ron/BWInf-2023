package aufgabe4;


public class Assignment {

	int recieved;
	int finished;
	
	int duration;
	int progress = 0;
	int remaining;
	
	
	// Constructor
	public Assignment(int recieved, int duration) {
		this.recieved = recieved;
		this.duration = duration;
		remaining = duration;
	}

	
	// Increases the progress by the given value
	public void addProgress(int value) {
		
		progress += value;
		remaining -= value;
		
	}
	
}
