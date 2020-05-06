import java.util.Arrays;

public class Schedule {
	
	String file;
	int[][] conflictMatrix, timeslotSchedule;
	int[] timeslot;
	int totalExams;
	int timeslotIndex;
	int timeslotMax;
	
	public Schedule(String file, int[][] conflictMatrix, int totalExams) {
		this.file = file;
		this.conflictMatrix = conflictMatrix;
		this.totalExams = totalExams;
	}
	
	public int[][] getSchedule() {
		// fill hasiltimeslot array
		timeslotSchedule = new int[totalExams][2];
    	for (int course = 0; course < totalExams; course++) {
    		timeslotSchedule[course][0] = (course+1);
    		timeslotSchedule[course][1] = timeslot[course];
    	}
		return this.timeslotSchedule; 
	}
	
	public void scheduling(int[] timeslot) {
		timeslot = new int[totalExams];
		timeslotIndex = 1;
    	for(int i= 0; i < conflictMatrix.length; i++)
    		timeslot[i] = 0;
    	
    	
		for(int i = 0; i < conflictMatrix.length; i++) {
			for (int j = 1; j <= timeslotIndex; j++) {
				if(isTimeslotAvailable(i, j, conflictMatrix, timeslot)) {
					timeslot[i] = j;
					break;
				}
					else
						timeslotIndex = timeslotIndex+1;
			}
		}
		
		//printSchedule(file, timeslot);
	}
	public int[] schedulingByDegree(int [][] sortedCourse) {
    	this.timeslot = new int[totalExams];
    	timeslotIndex = 1; // starting timeslot from 1
    	for(int i= 0; i < sortedCourse.length; i++)
    		this.timeslot[i] = 0;
    	
		for(int course = 0; course < sortedCourse.length; course++) {
			for (int time_slotindex = 1; time_slotindex <= timeslotIndex; time_slotindex++) {
				if(isTimeslotAvailableWithSorted(course, time_slotindex, conflictMatrix, sortedCourse, this.timeslot)) {
					this.timeslot[sortedCourse[course][0]-1] = time_slotindex;
					break;
				}
					else
						timeslotIndex = timeslotIndex+1; // move to ts+1 if ts is crash
			}
		}
		return this.timeslot;
		
		//printSchedule(file, timeslot);
    }
	
	public int getHowManyTimeSlot(int[] timeslot) { return Arrays.stream(timeslot).max().getAsInt(); }
        
        public int getTotalTimeslots(int[][] timeslot) { 
		int totalTimeslot = 0;
		
		for(int i = 0; i < timeslot.length; i++) {
			if(timeslot[i][1] > totalTimeslot)
				totalTimeslot = timeslot[i][1];
		}
		return totalTimeslot; 
	}
	
	public static boolean isTimeslotAvailable(int course, int timeslot, int[][] conflictMatrix, int[] timeslotArray) {
		for(int i = 0; i < conflictMatrix.length; i++)
			if(conflictMatrix[course][i] != 0 && timeslotArray[i] == timeslot)
				return false;
		
		return true;
	}
    public static boolean isTimeslotAvailableWithSorted(int course, int timeslot, int[][] conflictMatrix, int[][] sortedMatrix, int[] timeslotArray) {
		for(int i = 0; i < sortedMatrix.length; i++) 
			if(conflictMatrix[sortedMatrix[course][0]-1][i] != 0 && timeslotArray[i] == timeslot) {
				return false;
			}
		
		return true;
	}
    
    public static boolean checkRandomTimeslot(int randomCourse, int randomTimeslot, int[][] conflictMatrix, int[][] schedule){
        for(int i=0; i<conflictMatrix.length; i++)
            if(conflictMatrix[randomCourse][i] !=0 && schedule[i][1]==randomTimeslot)
                return false;
        return true;              
    }
    
	public void printSchedule() {
		System.out.println("\n================================================\n");
    	for (int i = 0; i < totalExams; i++)
    		System.out.println("Timeslot untuk course "+ (i+1) +" adalah timeslot: " + timeslot[i]);       
	    
    	System.out.println("\n================================================"); 
	    System.out.println("Jumlah timeslot yang dibutuhkan untuk menjadwalkan " + totalExams + " course di file " + file + " adalah "+ Arrays.stream(timeslot).max().getAsInt() + " timeslot.");
	}
}
