import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    static String folderDataset = "D:/Backup Laptop Lama/KULIAH/SEMESTER 6/OKH/FP/Dataset/";
    static String fileName[][] = {
            {"car-f-92", "Carleton92"}, {"car-s-91", "Carleton91"},
            {"ear-f-83", "EarlHaig83"}, {"hec-s-92", "EdHEC92"}, 
            {"kfu-s-93", "KingFahd93"}, {"lse-f-91", "LSE91"},
            {"pur-s-93", "pur93"}, {"rye-s-93", "rye92"},
            {"sta-f-83", "St.Andrews83"}, {"tre-s-92", "Trent92"}, 
            {"uta-s-92", "TorontoAS92"}, {"ute-s-92", "TorontoE92"},
            {"yor-f-83", "YorkMills83"}
        };
    
    static String file, filePilihanInput, filePilihanOutput;
    
    static int totalExams;
    
    static int timeslot[]; // fill with course & its timeslot
    static int[][] conflictMatrix, courseSorted, hasiltimeslot; // fill with conflict matrix	
	
	private static Scanner scanner;
	
    public static void main(String[] args) throws IOException {
        scanner = new Scanner(System.in);
        for	(int i=0; i< fileName.length; i++)
        	System.out.println(i+1 + ". Penjadwalan " + fileName[i][1]);
        
        System.out.print("\nPilih dataset yang akan dijadwalkan : ");
        int pilih = scanner.nextInt();
        
        filePilihanInput = fileName[pilih-1][0];
        filePilihanOutput = fileName[pilih-1][1];
        
        file = folderDataset + filePilihanInput;
        
		
        Course course = new Course(file);
        int totalExams = course.getNumberofCourses();
        
        conflictMatrix = course.getConflictMatrix();
        int totalStudents = course.getTotalStudents();
        
                System.out.println("\nPenjadwalan Dataset " + filePilihanOutput +" Dengan Metode Simulated Annealing");
        
		// sort exam by degree
		courseSorted = course.sortingByDegree(conflictMatrix, totalExams);
		
		Optimization optimization = new Optimization(file, conflictMatrix, courseSorted, totalExams, totalStudents, 1000);
                
                //LargestDegee
                long starttimeLargestDegree = System.nanoTime();
		Schedule schedule = new Schedule(file, conflictMatrix, totalExams);
		timeslot = schedule.schedulingByDegree(courseSorted);
		int[][] timeslotByLargestDegree = schedule.getSchedule();
		long endtimeLargestDegree = System.nanoTime();
		
                //Simulated Annealing Algorithm
		long starttimeSA = System.nanoTime();
                optimization.getTimeslotBySimulatedAnnealing(100.0);
                long endtimeSA = System.nanoTime();
		
                //Hill Climbing Algorithm
                long starttimeHC = System.nanoTime(); 
                optimization.getTimeslotByHillClimbing(); // use hillclimbing methode for iterates 1000 times
		long endtimeHC = System.nanoTime();
                
                System.out.println("Penjadwalan Dataset " + filePilihanOutput +"\n");
                System.out.println("Jumlah Timeslot (menggunakan \"Constructive Heuristics\") 	: " + schedule.getJumlahTimeSlot(schedule.getSchedule()));
		System.out.println("Penalti Akhir \"Constructive Heuristics\" 			: " + Evaluator.getPenalty(conflictMatrix, schedule.getSchedule(), totalStudents));
		System.out.println("Waktu eksekusi yang dibutuhkan \"Constructive Heuristics\"        : " + ((double) (endtimeLargestDegree - starttimeLargestDegree)/1000000000) + " detik.\n");
		
		System.out.println("Jumlah Timeslot (menggunakan Hill Climbing)                     : " + optimization.getJumlahTimeslotHC());
		System.out.println("Penalti Akhir Hill Climbing                                     : " + Evaluator.getPenalty(conflictMatrix, optimization.getTimeslotHillClimbing(), totalStudents));
		System.out.println("Waktu eksekusi yang dibutuhkan Hill Climbing                    : " + ((double) (endtimeHC - starttimeHC)/1000000000) + " detik.\n");
                
                System.out.println("Jumlah Timeslot (menggunakan Simulated Annealing) 		: " + optimization.getJumlahTimeslotSimulatedAnnealing());
		System.out.println("Penalti Akhir Simulated Annealing 				: " + Evaluator.getPenalty(conflictMatrix, optimization.getTimeslotSimulatedAnnealing(), totalStudents));
		System.out.println("Waktu eksekusi yang dibutuhkan Simulated Annealing              : " + ((double) (endtimeSA - starttimeSA)/1000000000) + " detik.\n");
                System.out.println("=============================================================");
    }
    
    public static void writeSolFile(int[][] hasiltimeslot, String namaFileOutput) throws IOException {
    	String directoryOutput = "D:/Backup Laptop Lama/KULIAH/SEMESTER 6/OKH/FP/Dataset/" + namaFileOutput +".sol";
        FileWriter writer = new FileWriter(directoryOutput, true);
        for (int i = 0; i <Main.hasiltimeslot.length; i++) {
            for (int j = 0; j < Main.hasiltimeslot[i].length; j++) {
                  writer.write(Main.hasiltimeslot[i][j]+ " ");
            }
            writer.write("\n");
        }
        writer.close();
        
		System.out.println("\nFile penjadwalan " + namaFileOutput+ " berhasil dibuat");
	}
}