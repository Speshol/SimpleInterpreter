package javaLexer;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
	//Main function of the parser. We first call the lexer to generate a list of tokens, lexemes, 
	//the lines they are found on, and any errors that are encountered in the lexical analysis (that
	//being invalid characters).
	public static void main(String args[]) {
		boolean exitSys = false;

		while (exitSys == false) {

			String file1 = "resources/Test1.jl";
			String file2 = "resources/Test2.jl";
			String file3 = "resources/Test3.jl";

			System.out.println("Choose which file to use. \n1) Test1.jl\n2) Test2.jl\n3) Test3.jl\n4) Exit Interpreter");
			Scanner in = new Scanner(System.in);
			int userChoice = 0;
			String fileChoice = "";

			boolean fileChosen = false;


			/* File choice system. Allows a user to choose from one of the three potential
			 * files to check for errors or bad characters. This will loop until we receive
			 * a valid input, that being a number 1 through 3. ANy bad inputs will let the 
			 * user know, throw them out, and start again.
			 */
			while(fileChosen == false) {
				System.out.print("> ");	
				try {
					userChoice = in.nextInt();

					switch(userChoice) {

					case 1:
						fileChoice = file1;
						fileChosen = true;
						break;
					case 2:
						fileChoice = file2;
						fileChosen = true;
						break;
					case 3:
						fileChoice = file3;
						fileChosen = true;
						break;

					case 4:
						fileChoice = "";
						fileChosen = true;
						exitSys = true;
						break;

					default:
						System.out.println("Please choose a file 1->3.\n");
						continue;
					}
				}catch (NumberFormatException e) {
					System.out.println("Please choose a number 1->3");
				}
				catch (InputMismatchException e) {
					in.next();
					System.out.println("Please choose a number 1->3");
				}
			}


			if (fileChoice.length() > 0) {
				javaParser parser = new javaParser(fileChoice);

				List<String> errors = new ArrayList<String>();
				errors = parser.getErrors();

				List<String> printList = new ArrayList<String>();
				printList = parser.getPrintList();

				if (!errors.isEmpty()) {
					for(String error : errors) {
						System.out.println(error);
					}
				}
				else
					for(String printer : printList)
						System.out.println(printer);
				
				printList.clear();
				errors.clear();

			}
			System.out.println("");
		}
		
		System.out.println("Thank you for using my simple Interpreter! \n Exiting. . .");
	}

}
