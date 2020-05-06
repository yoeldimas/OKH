import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
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
    static int[][] conflictMatrix, courseSorted, timeslotResult;
	
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
        
		// sort exam by degree
		courseSorted = course.sortingByDegree(conflictMatrix, totalExams);
		
		// Hill Climbing Algorithm
                long starttimeHC = System.nanoTime();
		new Optimization(file).getTimeslotByHillClimbing(conflictMatrix, courseSorted, totalExams, totalStudents, 1000000); // use hillclimbing methode for iterates 1000000 times
		long endtime = System.nanoTime();
		// end time
		double runningtime = (double) (endtime - starttimeHC)/1000000000;
                
                // Initial solution by Largest Degree
                long starttimeLD = System.nanoTime();
                Schedule schedule = new Schedule(file, conflictMatrix, totalExams);
                timeslot = schedule.schedulingByDegree(courseSorted);
                int[][] timeslotLD = schedule.getSchedule();
                long endtimeLD = System.nanoTime();
		
		System.out.println("Waktu eksekusi yang dibutuhkan adalah selama " + runningtime + " detik.");
                
                System.out.println("");
                System.out.println("Hasil Initial Solution");
                System.out.println("Jumlah timeslot yang dibutuhkan untuk menjadwalkan " + totalExams + " course dalam dataset " + filePilihanOutput + " adalah "+ schedule.getTotalTimeslots(schedule.getSchedule()) + " timeslot.");
                System.out.println("Penalti akhir : " + Evaluator.getPenalty(conflictMatrix, schedule.getSchedule(), totalStudents));
                System.out.println("\n================================================");
    }
    
    public static void writeSolFile(int[][] hasiltimeslot, String namaFileOutput) throws IOException {
		// fill hasiltimeslot array
    	timeslotResult = new int[totalExams][2];
    	for (int course = 0; course < totalExams; course++) {
    		timeslotResult[course][0] = (course+1);
    		timeslotResult[course][1] = timeslot[course];
    	}
    	
    	String directoryOutput = "D:/Backup Laptop Lama/KULIAH/SEMESTER 6/OKH/FP/Dataset/" + namaFileOutput +".sol";
        FileWriter writer = new FileWriter(directoryOutput, true);
        for (int i = 0; i <timeslotResult.length; i++) {
            for (int j = 0; j < timeslotResult[i].length; j++) {
                  writer.write(timeslotResult[i][j]+ " ");
            }
            writer.write("\n");
        }
        writer.close();
        
		System.out.println("\nFile penjadwalan " + namaFileOutput+ " berhasil dibuat");
	}
}