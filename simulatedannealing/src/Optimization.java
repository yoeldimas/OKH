import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Optimization {
	int[][] timeslotHillClimbing, timeslotSimulatedAnnealing, initialTimeslot, conflictMatrix, courseSorted;
	int[] timeslot;
	String file;
	int totalExams, totalStudents, randomCourse, randomTimeslot, iterasi;
	double initialPenalty, bestPenalty, deltaPenalty;
	
	Schedule schedule;
	
	Optimization(String file, int[][] conflictMatrix, int[][] courseSorted, int totalExams, int totalStudents, int iterasi) { 
		this.file = file; 
		this.conflictMatrix = conflictMatrix;
		this.courseSorted = courseSorted;
		this.totalExams = totalExams;
		this.totalStudents = totalStudents;
		this.iterasi = iterasi;
	}
	
	// hill climbing method
	public void getTimeslotByHillClimbing() throws IOException {
		schedule = new Schedule(file, conflictMatrix, totalExams);
		timeslot = schedule.schedulingByDegree(courseSorted);
		
		int[][] initialTimeslot = schedule.getSchedule(); // get initial solution
		timeslotHillClimbing = Evaluator.getTimeslot(initialTimeslot);
		initialPenalty = Evaluator.getPenalty(conflictMatrix, initialTimeslot, totalStudents);
		
		int[][] timeslotHillClimbingSementara = Evaluator.getTimeslot(timeslotHillClimbing); // handle temporary solution. if better than feasible, replace initial
		
		bestPenalty = Evaluator.getPenalty(conflictMatrix, timeslotHillClimbing, totalStudents);
		
		for(int i = 0; i < iterasi; i++) {
			try {
				randomCourse = random(totalExams); // random course
				randomTimeslot = random(schedule.getJumlahTimeSlot(initialTimeslot)); // random timeslot
				
				if (Schedule.checkRandomTimeslot(randomCourse, randomTimeslot, conflictMatrix, timeslotHillClimbingSementara)) {	
					timeslotHillClimbingSementara[randomCourse][1] = randomTimeslot;
					double penaltiAfterHillClimbing = Evaluator.getPenalty(conflictMatrix, timeslotHillClimbingSementara, totalStudents);
					
					// compare between penalti. replace bestPenalty with penaltiAfterHillClimbing if initial penalti is greater
					if(bestPenalty > penaltiAfterHillClimbing) {
						bestPenalty = Evaluator.getPenalty(conflictMatrix, timeslotHillClimbingSementara, totalStudents);
						timeslotHillClimbing[randomCourse][1] = timeslotHillClimbingSementara[randomCourse][1];
					} 
						else 
							timeslotHillClimbingSementara[randomCourse][1] = timeslotHillClimbing[randomCourse][1];
				}
			//	System.out.println("Iterasi ke " + (i+1) + " memiliki penalti : "+ bestPenalty);
			}
				catch (ArrayIndexOutOfBoundsException e) {
					//System.out.println("randomCourseIndex index ke- " + randomCourseIndex);
					//System.out.println("randomTimeslot index ke- " + randomTimeslot);
				}	
		}
	}
	
	// simulated annealing method
        public void getTimeslotBySimulatedAnnealing(double temperature) {
		double coolingrate = 0.1;
		schedule = new Schedule(file, conflictMatrix, totalExams);
		timeslot = schedule.schedulingByDegree(courseSorted);
		LowLevelHeuristics lowLevelHeuristics = new LowLevelHeuristics(conflictMatrix);
		
		// initial solution
		timeslotSimulatedAnnealing = schedule.getSchedule();
		initialPenalty = Evaluator.getPenalty(conflictMatrix, timeslotSimulatedAnnealing, totalStudents);
                
		
		int[][] timeslotSimulatedAnnealingSementara = Evaluator.getTimeslot(timeslotSimulatedAnnealing);
		
		for	(int i=0; i < iterasi; i++) {
			int llh = randomNumber(1, 5);
			int[][] timeslotLLH;
			switch (llh) {
				case 1:
					timeslotLLH = lowLevelHeuristics.move1(timeslotSimulatedAnnealingSementara);
					break;
				case 2:
					timeslotLLH = lowLevelHeuristics.swap2(timeslotSimulatedAnnealingSementara);
					break;
				case 3:
					timeslotLLH = lowLevelHeuristics.move2(timeslotSimulatedAnnealingSementara);
					break;
				case 4:
					timeslotLLH = lowLevelHeuristics.swap3(timeslotSimulatedAnnealingSementara);
					break;
				case 5:
					timeslotLLH = lowLevelHeuristics.move3(timeslotSimulatedAnnealingSementara);
					break;
				default:
					timeslotLLH = lowLevelHeuristics.move1(timeslotSimulatedAnnealingSementara);
					break;
			}
			
//			temperature = temperature * (1 - coolingrate);
//			temperature = temperature - coolingrate;
			System.out.println("Suhu : " + temperature);
			if (Evaluator.getPenalty(conflictMatrix, timeslotLLH, totalStudents) <= Evaluator.getPenalty(conflictMatrix, timeslotSimulatedAnnealingSementara, totalStudents)) {
				timeslotSimulatedAnnealingSementara = Evaluator.getTimeslot(timeslotLLH);
				if (Evaluator.getPenalty(conflictMatrix, timeslotLLH, totalStudents) <= Evaluator.getPenalty(conflictMatrix, timeslotSimulatedAnnealing, totalStudents)) {
					timeslotSimulatedAnnealing = Evaluator.getTimeslot(timeslotLLH);
					bestPenalty = Evaluator.getPenalty(conflictMatrix, timeslotSimulatedAnnealing, totalStudents);
				}
			}
				else if (acceptanceProbability(Evaluator.getPenalty(conflictMatrix, timeslotSimulatedAnnealingSementara, totalStudents), Evaluator.getPenalty(conflictMatrix, timeslotLLH, totalStudents), temperature) > Math.random())
					timeslotSimulatedAnnealingSementara = Evaluator.getTimeslot(timeslotLLH);
//			System.out.println("acceptance P : " + acceptanceProbability(Evaluator.getPenalty(conflict_matrix, timeslotSimulatedAnnealingSementara, jumlahmurid), Evaluator.getPenalty(conflict_matrix, timeslotLLH, jumlahmurid), temperature));
//			System.out.println("temperature : " + currentTemperature);
			// print current penalty of each iteration
			System.out.println("Iterasi: " + (i+1) + " memiliki penalty " + Evaluator.getPenalty(conflictMatrix, timeslotSimulatedAnnealingSementara, totalStudents));
//			temperature *= 1 - coolingrate;
			temperature = temperature - coolingrate;
		}
                

		deltaPenalty = ((initialPenalty-bestPenalty)/initialPenalty)*100;
                
		System.out.println("=============================================================");
		System.out.println("		  METODE SIMULATED ANNEALING				 			 "); // print best penalty
		System.out.println("\nInitial Penalty   : "+ initialPenalty); // print initial penalty
		System.out.println("Best Penalty      : " + bestPenalty); // print best penalty
		System.out.println("Delta Improvement : " + deltaPenalty + " % dari inisial solusi");
		System.out.println("Jumlah Timeslot   : " + schedule.getJumlahTimeSlot(timeslotSimulatedAnnealing) +"\n");
                //System.out.println("Waktu eksekusi yang dibutuhkan Simmulated Annealing " + ((double) (endtimeSA - starttimeSA)/1000000000) + " detik.\n");
		System.out.println("=============================================================");
	}
        
        public int[][] getTimeslotHillClimbing() { return timeslotHillClimbing; }
	public int[][] getTimeslotSimulatedAnnealing() { return timeslotSimulatedAnnealing; }
        
        public int getJumlahTimeslotHC() { return schedule.getJumlahTimeSlot(timeslotHillClimbing); }
	public int getJumlahTimeslotSimulatedAnnealing() { return schedule.getJumlahTimeSlot(timeslotSimulatedAnnealing); }
	
	private static int randomNumber(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min) + min;
	}
	
        private static int random(int number) {
		Random random = new Random();
		return random.nextInt(number);
	}
        
	private static double randomDouble() {
		Random r = new Random();
		return r.nextInt(1000) / 1000.0;
	}
	
	private static double acceptanceProbability(double currentPenalty, double newPenalty, double temperature) {
		// If the new solution is better, accept it
		if (newPenalty < currentPenalty)
			return 1.0;
		
		// If the new solution is worse, calculate an acceptance probability
		return Math.exp((currentPenalty - newPenalty) / temperature);
	}
}
