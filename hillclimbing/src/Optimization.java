import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Optimization {
	int[][] timeslotHillClimbing, conflictMatrix, courseSorted;
	int[] timeslot;
	String file;
	int jumlahexam, randomCourse, randomTimeslot;
	
	Optimization(String file) { this.file = file; }
	
	// hill climbing method
	public void getTimeslotByHillClimbing(int[][] conflictMatrix, int[][] courseSorted, int totalExams, int totalStudents, int iterasi) throws IOException {
	
		Schedule schedule = new Schedule(file, conflictMatrix, totalExams);
		timeslot = schedule.schedulingByDegree(courseSorted);
		
		timeslotHillClimbing = schedule.getSchedule(); // initial feasible solution
		int[][] timeslotHillClimbingSementara = new int[timeslotHillClimbing.length][2]; // handle temporary solution. if better than feasible, replace initial
		
		// copy timeslotHillClimbing to timeslotHillClimbingSementara
		for (int i = 0; i < timeslotHillClimbingSementara.length; i ++) {
			timeslotHillClimbingSementara[i][0] = timeslotHillClimbing[i][0]; // fill with course
			timeslotHillClimbingSementara[i][1] = timeslotHillClimbing[i][1]; // fill with timeslot
		}
		
		double penaltiInitialFeasible = Evaluator.getPenalty(conflictMatrix, timeslotHillClimbing, totalStudents);
		
		for(int i = 0; i < iterasi; i++) {
			try {
				randomCourse = randomNumber(0, totalExams); // random course
				randomTimeslot = randomNumber(0, schedule.getHowManyTimeSlot(timeslot)); // random timeslot
				timeslotHillClimbingSementara[randomCourse][1] = randomTimeslot;
			
				if (Schedule.checkRandomTimeslot(randomCourse, randomTimeslot, conflictMatrix, timeslotHillClimbing)) {	
					timeslotHillClimbingSementara[randomCourse][1] = randomTimeslot;
					double penaltiAfterHillClimbing = Evaluator.getPenalty(conflictMatrix, timeslotHillClimbingSementara, totalStudents);
					
					// compare between penalti. replace initial with after if initial penalti is greater
					if(penaltiInitialFeasible > penaltiAfterHillClimbing) {
						penaltiInitialFeasible = penaltiAfterHillClimbing;
						timeslotHillClimbing[randomCourse][1] = timeslotHillClimbingSementara[randomCourse][1];
					} 
						else 
							timeslotHillClimbingSementara[randomCourse][1] = timeslotHillClimbing[randomCourse][1];
				}
				//System.out.println("jadwaltemp ke " + randomCourseIndex);
				//System.out.println("Random timeslot ke " + randomTimeslot);
				System.out.println("Iterasi ke " + (i+1) + " memiliki penalti : "+penaltiInitialFeasible);
			}
				catch (ArrayIndexOutOfBoundsException e) {
					//System.out.println("randomCourseIndex index ke- " + randomCourseIndex);
					//System.out.println("randomTimeslot index ke- " + randomTimeslot);
				}
			
		}
		
		// print updated timeslot
		System.out.println("\n================================================\n");
    	for (int courseIndex = 0; courseIndex < totalExams; courseIndex++)
    		System.out.println("Timeslot untuk course "+ timeslotHillClimbing[courseIndex][0] +" adalah timeslot: " + timeslotHillClimbing[courseIndex][1]);       
	    
            System.out.println("\n================================================\n"); 
            System.out.println("Perbandingan Hasil Initial Solution dengan Hasil Algoritma Hill Climbing Untuk Penjadwalan Dataset " + Main.filePilihanOutput +"\n");
            System.out.println("Hasil Hill Climbing Algorithm");
	    System.out.println("Jumlah timeslot yang dibutuhkan untuk menjadwalkan " + totalExams + " course dalam dataset " + Main.filePilihanOutput + " adalah "+ Arrays.stream(timeslot).max().getAsInt() + " timeslot.");
            System.out.println("Penalti akhir : " + penaltiInitialFeasible); // print latest penalti
	}
	
	public static int randomNumber(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min) + min;
	}
}
