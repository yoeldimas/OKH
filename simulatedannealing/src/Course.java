import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Course {
	
	int totalStudents;

	String fileInput;
	
	public Course(String fileInput) { this.fileInput = fileInput; }
	
	public int getTotalStudents() { return this.totalStudents; }
	public int getNumberofCourses() throws IOException {
		int totalCourse = 0;
		// read course file
		BufferedReader readCourse = new BufferedReader(new FileReader(fileInput + ".crs"));
			while (readCourse.readLine() != null) 
				totalCourse++; // set how many course taken
		readCourse.close();
		
		return totalCourse;
	}
	
	public int[][] getConflictMatrix() throws IOException {
		// fill dataset array
		int[][] conflictMatrix = new int[getNumberofCourses()][getNumberofCourses()];
     	for (int i=0; i<conflictMatrix.length; i++)
     		for(int j=0; j<conflictMatrix.length; j++)
     			conflictMatrix[i][j] = 0;
     	
    	// read student file
		BufferedReader readStudent = new BufferedReader(new FileReader(fileInput + ".stu"));
		String spasi = " ";
		while ((spasi = readStudent.readLine()) != null) {
			totalStudents++;
			String tmp [] = spasi.split(" ");
			if	(tmp.length > 1) {
				for(int i=0; i<tmp.length; i++)
					for(int j=i+1; j<tmp.length; j++) {
						conflictMatrix[Integer.parseInt(tmp[i])-1][Integer.parseInt(tmp[j])-1]++;
						conflictMatrix[Integer.parseInt(tmp[j])-1][Integer.parseInt(tmp[i])-1]++;
				}
			}
		}
		readStudent.close();	
		
		return conflictMatrix;
	}
	
	public int [][] sortingByDegree(int[][] conflictMatrix, int totalCourse) {
		int[][] courseDegree = new int [totalCourse][2];
		int degree = 0;
		for (int i=0; i<courseDegree.length; i++)
			for (int j=0; j<courseDegree[0].length; j++)
				courseDegree[i][0] = i+1; // fill course_sorted column 1 with course index // fill course_sorted column 1 with course index
		
    	for (int i=0; i<totalCourse; i++) {
			for (int j=0; j<totalCourse; j++)
				if(conflictMatrix[i][j] > 0)
					degree++;
					else
						degree = degree;					
			courseDegree[i][1] = degree; // fill amount of degree for each course
			degree=0;
		}
    	// sorting by degree
    	int[][] max = new int[1][2]; // make max array with 1 row 2 column. untuk ngehandle degree
    	max[0][0] = -1;
		max[0][1] = -1;
		int x = 0;
		int[][] courseSorted = new int[totalCourse][2];
		
		for(int a=0; a<courseDegree.length; a++) {
			for(int i=0; i<courseDegree.length; i++) {
				if(max[0][1]<courseDegree[i][1]) {
					max[0][0] = courseDegree[i][0];
					max[0][1] = courseDegree[i][1];
					x = i;
				}				
			}
			courseDegree[x][0] = -2;
			courseDegree[x][1] = -2;
			courseSorted[a][0] = max[0][0];
			courseSorted[a][1] = max[0][1];
			max[0][0] = -1;
			max[0][1] = -1;
		
		}
		return courseSorted;
	}
	
	/*public int[][] sortingBySaturation(int[][] conflictmatrix, int jumlahcourse) {
		course_sorted = new int[jumlahcourse][2];
		
		return course_sorted;
	}*/
}
