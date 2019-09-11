import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

/** This class performs various operations which simulates a simple demo of how the 
 * course enrollment in the real world works.
 * 
 * This class contains following funtions:
 * add(), Edit(), List(), Search(), printCourses(), promptForAdditionalOp(), addToFav(),
 * isIdExist(), updateFile(), appendToFile(), getStringInput(), getIntInput(), 
 * showMessageBox(), showOptionBox()
 * 
 * @author Ekta Patel
 *
 */
public class CourseOperation {
	
	static String _FILEPATH;
	static String _userName;
	static String _courseDetails;
	final static int ID = 0, NAME = 1, LENGTH = 2, SUBJECT = 3, YES = 0, NO = 1;
	
	/** Constructor method of the class.
	 * @param FILEPATH
	 * @param userName
	 * @throws IOException
	 */
	public CourseOperation(String FILEPATH, String userName) throws IOException{
		_FILEPATH = FILEPATH;
		_userName = userName;
		_courseDetails = list();
	}
	
	/** This method displays the courses details and prompts four options
	 * to choose from: (1) Input a new course, (2) Edit a course, (3) List and (4) Search.
	 * Based on the user input is calls different functions to do the corresponding operations.
	 * @throws IOException
	 */
	public static void printCourses() throws IOException {	
		_courseDetails = list();
		String[] buttons = {"Input a new course", "Edit a course", "List", "Search"};
		int userInput = showOptionBox(_courseDetails,"Course Catalog",buttons);
		
		if(userInput == 0){
			add();
		}
		else if (userInput == 1){
			edit();
		}
		else if (userInput == 2){
			showMessageBox(list(), "Courses - List View");
			promptForAdditionalOp();
		}
		else if (userInput == 3){
			search();
		}
	}
	
	/** This method sequentially prompts user for the name, id, length and subject of the new course.
	 *  While getting the input from the user it also validates the response.
	 *  For e.g. it checks if the new id entered, is in a 5 digit format.
	 *  It also checks if the new id entered, doesn't already exist in the file. If it does
	 *  then it will warn the user and ask to re-enter the new id.
	 *  Once, all the info. is gathered from the user, it calls another function to append the new
	 *  course in the file.
	 *  
	 * @throws IOException
	 */
	public static void add() throws IOException {
		String courseNameToAdd = "", courseSubjectToAdd = "", courseToAdd = "";
		int courseIdToAdd = 0, courseLengthToAdd = 0;
		boolean idAlreadyExist = false;
		
		/* Prompt user to enter the NAME of the new course */
	    courseNameToAdd = getStringInput("Please Enter the course NAME you would like to add.", 
	    								"Enter the course NAME", false);
	    while(courseNameToAdd.isEmpty()){
	    	courseNameToAdd = getStringInput("You cannot enter empty string!\n\n"
	    			+ "Please Enter the course NAME you would like to add", "Warning", true);
	    }
	    
	    /* Prompt user to enter the ID of the new course  */
		courseIdToAdd = getIntInput("Please Enter the course ID you would like to add.", 
									"Enter the course ID", 5, false);
		idAlreadyExist = isIdExist(courseIdToAdd);
		
		/* Re-prompt if the ID already exists or the user input format is incorrect. */
		while(courseIdToAdd == -1  || idAlreadyExist == true) {
			if(idAlreadyExist == true){
				courseIdToAdd = getIntInput("This ID already exists!\n\n"
						+ "Please Enter the course ID you would like to add", "Warning", 5, true);
			}
			else {
				courseIdToAdd = getIntInput("Please enter 5 digits only!\n\n"
					+ "Please Enter the course ID you would like to add", "Warning", 5, true);
			}
			idAlreadyExist = isIdExist(courseIdToAdd);
		}
		
		/* Prompt user to enter the LENGTH of the new course */
		courseLengthToAdd = getIntInput("Please Enter the course LENGTH you would like to add", 
										"Enter the course LENGTH", -1, false);
		while(courseLengthToAdd == -1) {
			courseLengthToAdd = getIntInput("Please enter digits only!\n\n"
					+ "Please Enter the Course LENGTH you would like to add", "Warning", -1, true);
		}
		
		/* Prompt user to enter the SUBJECT of the new course */
		courseSubjectToAdd = getStringInput("Please Enter the course SUBJECT you would like to add", 
											"Enter the course SUBJECT", false);
		while(courseSubjectToAdd.isEmpty()){
			courseSubjectToAdd = getStringInput("You cannot enter empty string!\n\n"
					+ "Please Enter the course SUBJECT you would like to add", "Warning", true);
		}
		
		/* create a new line and concenate the given inputs and store it in a variable "courseToAdd" */
		courseToAdd = "\n" + courseIdToAdd + " " + courseNameToAdd + " " 
								+ courseLengthToAdd + " " + courseSubjectToAdd;
		
		appendToFile(courseToAdd);
		promptForAdditionalOp();
	}
	
	/**This method first prompts whether to edit the Id, Name, Length or the Subject.
	 * Then it prompts for the new value for that field. It also validates the response
	 * For e.g. it validate if the new id entered, is in a 5 digit format. If it isn't 
	 * then it warns the users and re-prompts for the new id.
	 * 
	 * And then lastly, it creates a new line of string with the new value in it and it calls 
	 * the function to replace the old value with the new one.
	 * @throws IOException
	 */
	public static void edit() throws IOException {
		Scanner scanner = new Scanner(new File(_FILEPATH));
		String[] buttons = {"ID", "Name", "Length", "Subject"};
		String replaceLine = "", updateName = "", updateSubject = "";
		int courseIdToEdit = 0, userInput = 0, updateId = 0, updateLength = 0, line = 0;
		boolean isUpdated = false, idExist = false;
		
		/* Prompt user for the ID of the course to be edited */
		courseIdToEdit = getIntInput(_courseDetails	+ "\nPlease Enter the course ID to edit the course: ",
									"Enter the course ID", 5, false);
		idExist = isIdExist(courseIdToEdit);
		
		/* Re-prompt if the ID doesn't exists or the user input format is incorrect. */
		while(courseIdToEdit == -1  || idExist == false) {
			if(courseIdToEdit == -1) {
				courseIdToEdit = getIntInput("Please enter 5 digits only!\n\n" + _courseDetails	+
		               "\nPlease Enter the course ID to edit the course: ", 
						"Enter the course ID", 5, true);
			}
			else{
				courseIdToEdit = getIntInput("This ID doesn't exists in the database!\n\n" + _courseDetails	+
			               "\nPlease Enter the course ID to edit the course: ", 
							"Enter the course ID", 5, true);
			}
			idExist = isIdExist(courseIdToEdit);
		}
		idExist = false;
		
		
		
		/* Ask user whether to edit: ID, Name, Length or Subject */
		userInput = showOptionBox("Please Choose which field to edit", "Course Details", buttons);
		
		/* If user Chooses ID, prompt user to enter new ID and validate it */
		if(userInput == ID) {
			updateId = getIntInput("Please Enter the new ID: ", "Enter the new ID", 5, false);
			idExist = isIdExist(updateId);
			while(updateId == -1 || idExist){
				if(idExist){
					updateId = getIntInput("Sorry, this ID already exists in the database!\n\n"
							+ "Please Enter a new ID", "Enter the new ID", 5, true);
				}
				else{
					updateId = getIntInput("Please enter 5 digits only!\n\n"
							+ "Please Enter a new ID: ", "Enter the new ID", 5, true);
				}
				idExist = isIdExist(updateId);
				
			}
		}
		
		/* If user Chooses NAME, prompt user to enter new ID and validate it */
		else if(userInput == NAME) {
			updateName = getStringInput("Please Enter the new NAME: ", "Enter the new NAME", false);
			while(updateName.isEmpty()){
				updateName = getStringInput("Please Enter the new NAME: ", "Enter the new NAME", true);
			}
		}
		
		/* If user Chooses LENGTH, prompt user to enter new ID and validate it */
		else if(userInput == LENGTH) {
			updateLength = getIntInput("Please Enter the new LENGTH: ", "Enter the new LENGTH", -1, false);
			while(updateLength == -1){
				updateLength = getIntInput("Please Enter the new LENGTH: ", "Enter the new LENGTH", -1, true);
			}
		}
		
		/* If user Chooses SUBJECT, prompt user to enter new ID and validate it */
		else if(userInput == SUBJECT) {
			updateSubject = getStringInput("Please Enter the new SUBJECT: ", "Enter the new SUBJECT", false);
			while(updateSubject.isEmpty()){
				updateSubject = getStringInput("Please Enter the new SUBJECT: ", "Enter the new SUBJECT", true);
			}
		}
		
		/* Parse through the file, and find the course with given id and replace it */
		while (scanner.hasNextLine() == true && isUpdated == false) {
			line++;
			String courseLine = scanner.nextLine(), name = "";
			String[] tempArray = courseLine.split(" ");
			int arrLength = tempArray.length; 
			String id = tempArray[0], length = tempArray[arrLength - 2], subject = tempArray[arrLength - 1];
		    
		    for(int i = 1; i < arrLength - 2; i++)
		    	name += (tempArray[i] + " ");
			name = name.trim();
			
			/* If the course id matches the id given by the user */
			if(Integer.parseInt(id) == courseIdToEdit){
				/* If user chose to edit ID, then create a new string and 
				 * replace the ID field with new value in the file */
				if(userInput == ID){
					replaceLine = (updateId + " " + name + " " + length + " " + subject);
					updateFile(line, replaceLine);
					isUpdated = true;
				}
				/* If user chose to edit NAME, then create a new string and
				 * replace the NAME field with new value  in the file*/
				else if(userInput == NAME){
					replaceLine = (id + " " + updateName + " " + length + " " + subject);
					updateFile(line, replaceLine);
					isUpdated = true;
				}
				/* If user chose to edit LENGTH, then create a new string and
				 *  replace the LENGTH field with new value  in the file*/
				else if(userInput == LENGTH){
					replaceLine = (id + " " + name + " " + updateLength + " " + subject);
					updateFile(line, replaceLine);
					isUpdated = true;
				}
				/* If user chose to edit SUBJECT, then create a new string 
				 * and replace the SUBJECT field with new value in the file*/
				else if(userInput == SUBJECT){
					replaceLine = (id + " " + name + " " + length + " " + updateSubject);
					updateFile(line, replaceLine);
					isUpdated = true;
				}
			}
		}
		scanner.close();
	    promptForAdditionalOp();
	}

	/** This method parses the file and formats it into a string variable and returns that string.
	 *
	 * @return
	 * @throws IOException
	 */
	public static String list() throws IOException {
		String courseDetails = "";
		Scanner scanner = new Scanner(new File(_FILEPATH));
		
		/* Parse the file and store it in a string variable "courseDetails" and return it */
		while (scanner.hasNextLine() == true) {
			String courseLine = scanner.nextLine();
			String[] tempArray = courseLine.split(" ");
			int arrLength = tempArray.length;
		    int id = Integer.parseInt(tempArray[0]);
		    int length = Integer.parseInt(tempArray[arrLength - 2]);
		    String subject = tempArray[arrLength - 1];
		    String name = "";
		    for(int i = 1; i < arrLength - 2; i++)
		    	name += (tempArray[i] + " ");
			name = name.trim();
			
			courseDetails += (           name + " .................. " +
						     "  ( ID: " + id +
						 ",  Length: " + length + 
						",  Subject: " + subject + 
						                  " )\n\n");
		}
		scanner.close();
		return courseDetails;
	}
	
	/** This method prompts the user to enter the string to search for in the file.
	 * Once, the user enters the text, then it will search through the file and see
	 * if there's any exact match between the fields and the given text by the user.
	 * Once, it finishes searching, it will then print all the courses that has
	 * the given string. If no match found, it will display the message indicating 
	 * that no match was found.
	 * 
	 * @throws IOException
	 */
	public static void search() throws IOException {
		String stringToSearch = "", searchResults = "";	
		Scanner scanner = new Scanner(new File(_FILEPATH));
		
		/* Prompt user to enter the text to be searched */
		stringToSearch = getStringInput("Please Enter the search text: ", "Search Box", false);
		
		/* Go through all the fields in the file */
		while (scanner.hasNextLine() == true) {		
			String courseLine = scanner.nextLine(), name = "";
			String[] tempArray = courseLine.split(" ");
			int arrLength = tempArray.length;
		    String id = tempArray[0], length = tempArray[arrLength - 2], subject = tempArray[arrLength - 1];
		    
		    for(int i = 1; i < arrLength - 2; i++)
		    	name += (tempArray[i] + " ");
			name = name.trim();
			
			/* If any of the fields is matched, then store the course details in the variable "searchResults" */
			if(stringToSearch.equalsIgnoreCase(id) || stringToSearch.equalsIgnoreCase(length) ||
			   stringToSearch.equalsIgnoreCase(name) || stringToSearch.equalsIgnoreCase(subject)) {
					searchResults += (           name + " .................. " +
								 "  (ID: " + id +
							 ",  Length: " + length + 
							",  Subject: " + subject + 
							                  ")\n\n");
			}
		}
		/* If there were no matches, then inform user */
		if(searchResults.isEmpty()) {
			showMessageBox("Sorry! No results were found.\n Please try searching something else", "Search Results");
		}
		/* Else, return the search results */
		else {
			showMessageBox(searchResults, "Search Results");
		}
		
		scanner.close();
		promptForAdditionalOp();
	}
	
	/**Prompts user to choose if wants to perform any additional operations. If user chooses
	 * "Yes", then it will call the function to go back to main view.
	 * If the user chooses "No", then it will prompt the user to add courses into the 
	 * favorite list.
	 * 
	 * @throws IOException
	 */
	public static void promptForAdditionalOp() throws IOException{
		String[] buttons = {"Yes", "No"};
		/* Prompt user - if they would like to perform any additional operations? */
		int userInput = showOptionBox("Would you like to perform any additional operations?",
				"Any additional operations?",buttons);
		
		/* If user chooses to perform additional operations */
		if(userInput == YES){
			printCourses();
		}
		
		/* If user chooses to NOT to perform additional operations */
		else if(userInput == NO){
			while(!addToFav());
		}
	}
	
	/** This method prompts the user for the list of Ids (separated by comma), to be added in
	 * the favorite list. It validates the format and it checks if all the Ids exists in the
	 * file. If there's any format error or at-least one Id doesn't exist, then it will re-prompt 
	 * the user to enter the lists of Ids.
	 * Finally, it will do the sum of the hours (Length) of the fav courses and will display to the user. 
	 * 
	 * @return boolean - TRUE if all the courses were found, FALSE - if at-least one course wasn't found.
	 * @throws IOException
	 */
	public static boolean addToFav() throws IOException {
		String favCourseIds, favLists = "";
		String[] favCourseId;
		int numOfFavCourses = 0, hours = 0;
		int[] favCourseIdInt;
		Scanner scanner;
		
		/* Prompt user to enter the favorite course Ids separated by comma and validate it. */
		favCourseIds = getStringInput(_courseDetails + "Please Enter the Ids of the FAVORITE courses (separated by Comma)",
						"Favorite Course IDs", false);
		while(!favCourseIds.matches("[0-9, /,]+")) {
			favCourseIds = getStringInput("Please type digits and comma only!\n\n" + _courseDetails
					+ "Please Enter the course Ids separated by Comma: ", "Warning", true);
		}
			
		favCourseIds = favCourseIds.replace(" ", ""); 
		favCourseId = favCourseIds.split(",");
		favCourseIdInt = new int[favCourseId.length];
		numOfFavCourses = favCourseIdInt.length;
		
		for (int i = 0; i < numOfFavCourses; i++){
			favCourseIdInt[i] = Integer.parseInt(favCourseId[i]);
		}
		
		scanner = new Scanner(new File(_FILEPATH));
		boolean[] found = new boolean[numOfFavCourses];
		ArrayList<Integer> notFound = new ArrayList<Integer>(); 
		
		/* Initialize all the elements in the array as FALSE */
		for(int i = 0; i < numOfFavCourses; i++)
			found[i] = false;
		
		/* Parse through the file and checks if the IDs entered by the user exists in the file. */
		while (scanner.hasNextLine() == true) {
			String courseLine = scanner.nextLine();
			String[] tempArray = courseLine.split(" ");
			int arrLength = tempArray.length;
		    int id = Integer.parseInt(tempArray[0]);
		    int length = Integer.parseInt(tempArray[arrLength - 2]);
			String name = "", subject = tempArray[arrLength - 1];
		    
		    for(int i = 1; i < arrLength - 2; i++)
		    	name += (tempArray[i] + " ");
			name = name.trim();
		    
		    for(int i = 0; i < numOfFavCourses; i++){
		    	if(favCourseIdInt[i] == id) {
		    		hours += length;
		    		found[i] = true;
		    		favLists += (           name + " .................. " +
							 "  (ID: " + id +
						 ",  Length: " + length + 
						",  Subject: " + subject + 
						                  ")\n\n");
				}
		    }    
		}
		scanner.close();

		for(int i = 0; i < found.length; i++)
		{
			/* If any of the IDs doesn't exist add that id(s) into the "notFound" list. */
			if(found[i] == false)
			{
				notFound.add(favCourseIdInt[i]);
			}
		}
		
		/* If all the IDs were found, then display the total course hours of the favorite courses. */
		if(notFound.isEmpty()){
			showMessageBox(_userName + " has signed up for " + numOfFavCourses + " courses with " + 
							(numOfFavCourses * hours) + " credits.\n\n" + favLists, _userName + "'s Enrollment Info.");
			return true;
		}
		/* If all the IDs were NOT found, then display the IDs that were not found. */
		else{
			showMessageBox("Following IDs were not found:\n\n" + notFound.toString(), "Error");
			return false;
		}
	}
	
	/** Iterates through all the Id and returns true if a given id was found in the file
	 * 
	 * @param id - id to search for
	 * @return - TRUE if id was found, FALSE if id wasn't found
	 * @throws FileNotFoundException
	 */
	public static boolean isIdExist(int id) throws FileNotFoundException{
		Scanner scanner = new Scanner(new File(_FILEPATH));
		while (scanner.hasNextLine() == true) {
			String courseLine = scanner.nextLine();
			String[] tempArray = courseLine.split(" ");
			int tempId = Integer.parseInt(tempArray[0]);
			/* If the Id exists in the file, return true */
			if(tempId == id){
				return true;				
			}
		}
		return false;
	}
	
	/** Updates a line in the file
	 * 
	 * @param lineNum - line number to be replaced
	 * @param newLineString - new text to replace the old text
	 * @throws IOException
	 */
	public static void updateFile(int lineNum, String newLineString) throws IOException{	
		Path File_Path = Paths.get(_FILEPATH);
		List<String> fileText = new ArrayList<>(Files.readAllLines(File_Path, StandardCharsets.UTF_8));
		fileText.set(lineNum-1, newLineString);
		Files.write(File_Path, fileText, StandardCharsets.UTF_8);
	}
	
	/**Adds string at the bottom of the file
	 * 
	 * @param newLineString - new string to be added at the bottom of the file
	 * @throws IOException
	 */
	public static void appendToFile(String newLineString) throws IOException{
		File file =new File(_FILEPATH);  
        FileWriter fileWritter = new FileWriter(file,true);        
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
        bufferWritter.write(newLineString);
        bufferWritter.close();
        fileWritter.close();
	}
	
	public static String getStringInput(String displayText, String titleBar, boolean isError){
		String input = "";
		if(isError) {
			input = JOptionPane.showInputDialog(null, displayText, titleBar, JOptionPane.WARNING_MESSAGE);
		}
		else {
			input = JOptionPane.showInputDialog(null, displayText, titleBar, JOptionPane.INFORMATION_MESSAGE);
		}
		return input;
	}
	
	/** Gets Input (Integer Only) from the user and returns it.
	 * 
	 * @param displayText - Text to be displayed to the user
	 * @param titleBar - Text to be displayed on the title bar of the dialog box
	 * @param length - number of characters to expect as the input
	 * @param isError - TRUE if it's an error or warning message, FALSE if it's a regular input dialog
	 * @return int
	 */
	public static int getIntInput(String displayText, String titleBar, int length, boolean isError){
		String input = "";
		
		/* If the dialog is an Error message box */
		if(isError) {
			input = JOptionPane.showInputDialog(null, displayText, titleBar, JOptionPane.WARNING_MESSAGE);
		}
		
		/* If the dialog box is a regular input box */
		else {
			input = JOptionPane.showInputDialog(null, displayText, titleBar, JOptionPane.INFORMATION_MESSAGE);
		}
		input = input.replace(" ", "");
		
		/* Validate the input is all digits and has the expected length */
		if (input.matches("[0-9]+") && (input.length() == length || length == -1)) {
			return Integer.parseInt(input);
		}
		else {
			return -1;
		}
	}

	/** Shows the message dialog box
	 * 
	 * @param displayText - Text to be displayed to the user
	 * @param titleBar - Text to be displayed on the title bar of the dialog box
	 */
	public static void showMessageBox(String displayText, String titleBar){
		JOptionPane.showMessageDialog(null, displayText, titleBar, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/** Shows the option dialog box with the buttons as an option.
	 * 
	 * @param displayText - Text to be displayed to the user
	 * @param titleBar - Text to be displayed on the title bar of the dialog box
	 * @param buttons - Array of String. Each String element representing the button label
	 * @return int
	 */
	public static int showOptionBox(String displayText, String titleBar, String[] buttons){
		int input = JOptionPane.showOptionDialog(null, displayText, titleBar, 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, null);
		return input;
	}
}