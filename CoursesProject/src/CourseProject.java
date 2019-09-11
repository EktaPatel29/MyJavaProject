import java.io.*;
import javax.swing.JOptionPane;

public class CourseProject {

	public static String FILEPATH = "src/courses.txt";
	public static String userName = "";
	
	public static void main(String[] args) throws IOException {
		// Get the user's name using JOptionPane
		userName = JOptionPane.showInputDialog(null, "Please Enter Your Name: ",
					"Enter Name", JOptionPane.INFORMATION_MESSAGE);
		
		// Validate if the username is not null, if it is then re-prompt.
		while(userName.isEmpty()){
			userName = JOptionPane.showInputDialog(null, "Cannot enter empty string.\n\nPlease Enter Your Name: ",
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
		
		// Create an instance of a class CourseOperation and
		// call printCourses function to get started.
		CourseOperation co = new CourseOperation(FILEPATH, userName);
		co.printCourses();
	}
}
