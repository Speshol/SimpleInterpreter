/*
 * Class:       CS 4308 Section 02
 * Term:        Fall 2021
 * Name:        Tyler Gustat
 * Instructor:  Sharon Perry
 * Project:     Deliverable 1 Scanner/Lexer
 */



package javaLexer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;





public class javaLexer {

	//Four global ArrayLists. We use lexemes to track each individual lexeme, tokens to track each kind of token,
	//and currLine to track the line where each lexeme was found. The 4th list, errors, tracks any sort of errors
	//before outputting them. The error message includes the bad character and the line number.
	static List<String> lexemes = new ArrayList<String>();
	static List<Token> tokens = new ArrayList<Token>();
	static List<Integer> currLine = new ArrayList<Integer>();
	static List<String> errors = new ArrayList<String>();

	//Returns the list of lexemes found.
	public static List<String> getLexList(){
		return lexemes;
	}

	//Returns the list of tokens found.
	public static List<Token> getTokList(){
		return tokens;
	}

	//Returns the list of line numbers for each token
	//and lexeme.
	public static List<Integer> getLineList(){
		return currLine;
	}

	//Returns the list of errors found.
	public static List<String> getErrorList(){
		return errors;
	}

	public static int getSize() {
		return tokens.size();
	}





	/* Main method. This reads some file into the fileIn and inputs each line as it's own index in the list fileIn.
	 * From there, we trim each line of the excess whitespace to simplify our analysis and make the lexer work in full.
	 * Afterwards, we iterate on each of the lines calling the lexer method to analyze every line for lexemes and tokens.
	 * After we finish our lexical analysis, we exit the main portion of the main method. We also have a try-catch block
	 * to catch any sort of I/O issues. Afterwards, we utilize a printf statement to format our output.
	 */
	public javaLexer(String fileChoice) {

		//If you don't want to use the preset file names, simple add the file path to the file you want to use.
		//I personally chose to make a resources folder in Eclipse, as I was able to locally associate it using the path
		//"resources/fileName" for the given project.

		try {
			//We are able to change fileChoice respectively to test each individual Julia test file.
			List<String> fileIn = Files.readAllLines(Paths.get(fileChoice));

			for (int i = 0; i < fileIn.size(); i++)
				fileIn.set(i, fileIn.get(i).trim());

			for (int i = 0; i < fileIn.size(); i++) {
				lexer(fileIn.get(i), i + 1);
				if (!errors.isEmpty()) {
					break;
				}

			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		/*
		// * Normally prints the lexical analysis. Currently commented out for the purpose of
		// * Syntax Analysis. 
		// * 
		if (errors.isEmpty()) {
			System.out.printf("| %-10s %n", "LEXEME --------------------------> TOKEN |");
			System.out.println("Lexical Analysis for file: " + fileChoice);
			System.out.println("|-------------------------------------------------------|");
			for (int i = 0; i < lexemes.size(); i++) {
				System.out.printf("| %-10s" + "----------------------->" + " %-12s \n", lexemes.get(i), tokens.get(i));
			}
		}
		 */

	}

	/*
	 * Accepts two parameters, the current line and a string know as input.
	 * This method is designed to analyze a given string by checking each character.
	 * As we defined in the enumeration class, an id is some single-letter ID, we 
	 * only accept int literals, and everything else is some keyword.
	 */

	public static void lexer(String input, int line) {

		//Used to track our current lexeme which we are build
		String currLex = "";

		//When a lexeme is found in full, used to pass the token type to tokens
		Token currToken;

		//Tracks our current digit
		String lexBuilder = "";

		//Used to track the length of the string
		int counter = 0;

		//In the event of some error, this ensures we don't run out of memory.
		int repeats = 0;

		//Used to tell us when the String is empty
		boolean empty = false;

		//Flag to determine if something is an integer
		boolean isitNum = true;

		//Instantiates character temp as ' '
		char temp = ' ';

		//String of characters. We use this to make sure that our identifier is only a single
		//digit. If it isn't, then characters.contains(currLex) will return false.
		final String characters = "abcdefghijklmnopqrstuvwxyz";

		//if the current line is empty, we return.
		if (input.length() == 0)
			return;

		//If the current line is a comment, we return.
		if (input.contains("//"))
			return;

		//For as long as the string isn't empty, we will loop and find lexemes + tokens. 
		while (!empty){

			//Automatically increments. If the value ever exceeds 100, we hard exit.
			repeats++;
			if(repeats > 100) {
				empty = true;
				//Any use of continue is to reset us back to the base loop.
				continue;
			}

			//If our counter ever exceeds the index of our input string, we set empty to true before return to the loop.
			//We don't exit for the means of keywords and edge cases.
			if (counter >= input.length())
				empty = true;

			//Resets our lexBuilder to prevent items being too long of a length.
			lexBuilder = "";

			//We check to make sure counter isn't >= to the length of the string. If it isn't, we get the next lexeme.
			if (counter < input.length()) {
				temp = input.charAt(counter);
				lexBuilder = Character.toString(temp);
			}



			//If the current lexeme we are building is some keyword, we will add it, add the relevant token, reset current lexeme,
			//and depending on the keyword, we will increment the counter. Afterwards, we reset to the start.
			if (currLex.contains("function") || currLex.contains("print") || currLex.contains("end") || currLex.contains("if") ||
					currLex.contains("then") || currLex.contains("else") || currLex.contains("while") || currLex.contains("for") || currLex.contains("do")
					|| currLex.contains("repeat") || currLex.contains("until")){

				//Adds the keyword detected to the lexemes table
				lexemes.add(currLex);

				//Adds KW_FUNCTION token to tokens table
				if (currLex.equals("function")) {
					currLex = "";
					currToken = Token.KW_FUNCTION;
					tokens.add(currToken);
					counter++;
				}

				//Adds KW_PRINT token to tokens table
				else if (currLex.equals("print")) {
					currLex = "";
					currToken = Token.KW_PRINT;
					tokens.add(currToken);
				}

				//Adds KW_DO token to tokens table
				else if (currLex.equals("do")) {
					currLex = "";
					currToken = Token.KW_DO;
					tokens.add(currToken);
				}

				//Adds KW_END token to tokens table
				else if (currLex.equals("end")) {
					currLex = "";
					currToken = Token.KW_END;
					tokens.add(currToken);
				}

				//Adds KW_IF token to tokens table
				else if (currLex.equals("if")) {
					currLex = "";
					currToken = Token.KW_IF;
					tokens.add(currToken);
					counter++;
				}

				//Adds KW_THEN token to tokens table
				else if (currLex.equals("then")) {
					currLex = "";
					currToken = Token.KW_THEN;
					tokens.add(currToken);
				}

				//Adds KW_ELSE token to tokens table
				else if (currLex.equals("else")) {
					currLex = "";
					currToken = Token.KW_ELSE;
					tokens.add(currToken);
				}

				//Adds KW_WHILE token to tokens table
				else if (currLex.equals("while")) {
					currLex = "";
					currToken = Token.KW_WHILE;
					tokens.add(currToken);
					counter++;
				}

				//Adds KW_FOR token to tokens table
				else if (currLex.equals("for")) {
					currLex = "";
					currToken = Token.KW_FOR;
					tokens.add(currToken);
				}


				currLine.add(line);
				continue;
			}

			//We check to see if the current lexeme we're building is a single letter. If it is, we add it to our
			//currLex. We then increment counter and head back to the top.
			if(characters.contains(lexBuilder) && !lexBuilder.equals("")) {
				currLex += lexBuilder;	
				counter++;
				continue;
			}


			//We check to see if the current lexeme we're building is a single digit or letter. If it's a letter, we add 
			//the identifier to the lexeme list before heading back to the start. If it's instead a number, we then add
			//the literal integer to the list and return to the start. If we encounter some invalid identifier, we will print
			//it out and inform the user.
			if(lexBuilder.equals(" ")&& !currLex.equals("")) {
				if (characters.contains(currLex) && currLex.length() == 1) {
					lexemes.add(currLex);
					currToken = Token.ID;
					tokens.add(currToken);
					currLine.add(line);
					currLex = "";
					counter++;
					continue;
				}
				else {
					//If the lex is a number, we will add it. Otherwise, we will note that there is some
					//illegal identifier due to the combination of a letter and a number.
					isitNum = isNum(currLex);
					if (isitNum == true) {
						lexemes.add(currLex);
						currToken = Token.LITERAL_INTEGER;
						tokens.add(currToken);
						currLine.add(line);
						currLex = "";
						counter++;
						continue;
					}

					else {
						String error = "Error detected at line " + line + ". Invalid identifier: " + currLex;
						errors.add(error);
						return;
					}

				}

			}


			/* This is the statement to handle any comparators or operands. We start by checking if our current line has some letter
			 * or digit. if there is, we'll add it to our lexeme and tokens list and then check our operands / comparators. From there,
			 * we'll check for if something is +, -, *, /, >, <, =, !, or some whitespace. In the event of <, >, and = we will check
			 * the following index for another = operator. In the event we find them, we'll increment another time or two, add it to
			 * the lexeme and token lists, and then return to the top of the while loop.
			 */
			else if(lexBuilder.equals("(") | lexBuilder.equals(")") || lexBuilder.equals("<") || lexBuilder.equals(">") || 
					lexBuilder.equals("!")|| lexBuilder.equals("=") || lexBuilder.equals("+") || lexBuilder.equals("-") || lexBuilder.equals("*")
					|| lexBuilder.equals("/") || lexBuilder.equals("") || lexBuilder.equals(" ")) {

				//If we have a letter in the currLex, we add it as an identifier to the lexemes table, it's token type to the 
				//tokens table, and the line.
				if (characters.contains(currLex) && !currLex.contentEquals("")) {
					lexemes.add(currLex);
					currToken = Token.ID;
					tokens.add(currToken);
					currLine.add(line);
					currLex = "";
				}

				//If we have a number in the currLex, we add it as a literal_integer to the lexemes table, it's token type
				//to the tokens table, and the line
				else if (!characters.contains(currLex) && !currLex.contentEquals("")){
					//If the lex is a number, we will add it. Otherwise, we will note that there is some
					//illegal identifier due to the combination of a letter and a number.
					isitNum = isNum(currLex);
					if (isitNum) {
						lexemes.add(currLex);
						currToken = Token.LITERAL_INTEGER;
						tokens.add(currToken);
						currLine.add(line);
						currLex = "";
					}
					else {
						String error = "Error detected on line " + line + ". Invalid Identifier: " + currLex;
						errors.add(error);
						return;
					}
				}

				//If we encounter whitespace, go to the next character
				if (lexBuilder.equals("")) {
					counter++;
					continue;
				}

				//If we encounter a left parentheses, add it to the lexemes table, it's tokentype to the tokens table,
				//and it's line.
				else if (lexBuilder.equals("(")) {
					counter++;
					lexemes.add(lexBuilder);
					currToken = Token.lPARENS;
					tokens.add(currToken);
					currLine.add(line);
					continue;
				}

				//If we encounter a right parentheses, add it to the lexemes table, it's tokentype to the tokens table,
				//and it's line.
				else if (lexBuilder.equals(")")) {
					counter++;
					lexemes.add(lexBuilder);
					currToken = Token.RPARENS;
					tokens.add(currToken);
					currLine.add(line);
				}

				//If we encounter a plus sign, add it to the lexemes table, it's tokentype to the tokens table,
				//and it's line.
				else if (lexBuilder.equals("+")) {
					currLex += lexBuilder;
					counter++;
					if (counter < input.length()) {
						//Check the next character for an equals sign.
						temp = input.charAt(counter);
						lexBuilder = Character.toString(temp);

						//if we encounter an equals sign, add it to the current lexeme, place += into the lexemes table,
						//add it's token type to the tokens table, and the line to the line table.
						if(lexBuilder.equals("=")) {
							counter++;
							currLex += lexBuilder;
							counter++;
							lexemes.add(currLex);
							currToken = Token.ASSIGNMENT_OPERATOR;
							tokens.add(currToken);
							currLine.add(line);
							currLex = "";
							continue;
						}
					}
					//If we don't encounter a = sign, add it to the lexemes table as +, it's token type to the tokens
					//table, and it's line to the line table.
					counter++;
					lexemes.add(currLex);
					currToken = Token.ADD_OPERATOR;
					tokens.add(currToken);
					currLine.add(line);
					currLex = "";
					continue;
				}

				//If we encounter a minus sign, add it to the lexemes table, it's tokentype to the tokens table,
				//and it's line.
				else if (lexBuilder.equals("-")) {
					currLex += lexBuilder;
					counter++;
					if (counter < input.length()) {
						//Check the next character for an equals sign.
						temp = input.charAt(counter);
						lexBuilder = Character.toString(temp);

						//if we encounter an equals sign, add it to the current lexeme, place -= into the lexemes table,
						//add it's token type to the tokens table, and the line to the line table.
						if(lexBuilder.equals("=")) {
							counter++;
							currLex += lexBuilder;
							counter++;
							lexemes.add(currLex);
							currToken = Token.ASSIGNMENT_OPERATOR;
							tokens.add(currToken);
							currLine.add(line);
							currLex = "";
							continue;
						}
					}
					//If we don't encounter a = sign, add it to the lexemes table as -, it's token type to the tokens
					//table, and it's line to the line table.
					counter++;
					lexemes.add(currLex);
					currToken = Token.SUB_OPERATOR;
					tokens.add(currToken);
					currLine.add(line);
					currLex = "";
					continue;
				}

				//If we encounter a multiply sign, add it to the lexemes table, it's tokentype to the tokens table,
				//and it's line.
				else if (lexBuilder.equals("*")) {
					currLex += lexBuilder;
					counter++;
					if (counter < input.length()) {
						//Check the next character for an equals sign.
						temp = input.charAt(counter);
						lexBuilder = Character.toString(temp);

						//if we encounter an equals sign, add it to the current lexeme, place *= into the lexemes table,
						//add it's token type to the tokens table, and the line to the line table.
						if(lexBuilder.equals("=")) {
							counter++;
							currLex += lexBuilder;
							counter++;
							lexemes.add(currLex);
							currToken = Token.ASSIGNMENT_OPERATOR;
							tokens.add(currToken);
							currLine.add(line);
							currLex = "";
							continue;
						}
					}
					//If we don't encounter a = sign, add it to the lexemes table as *, it's token type to the tokens
					//table, and it's line to the line table.
					counter++;
					lexemes.add(currLex);
					currToken = Token.MUL_OPERATOR;
					tokens.add(currToken);
					currLine.add(line);
					currLex = "";
					continue;
				}

				//If we encounter a divide sign, add it to the lexemes table, it's tokentype to the tokens table,
				//and it's line.
				else if (lexBuilder.equals("/")) {
					currLex += lexBuilder;
					counter++;
					if (counter < input.length()) {
						//Check the next character for an equals sign.
						temp = input.charAt(counter);
						lexBuilder = Character.toString(temp);

						//if we encounter an equals sign, add it to the current lexeme, place /= into the lexemes table,
						//add it's token type to the tokens table, and the line to the line table.
						if(lexBuilder.equals("=")) {
							counter++;
							currLex += lexBuilder;
							counter++;
							lexemes.add(currLex);
							currToken = Token.ASSIGNMENT_OPERATOR;
							tokens.add(currToken);
							currLine.add(line);
							currLex = "";
							continue;
						}
					}
					//If we don't encounter a = sign, add it to the lexemes table as /, it's token type to the tokens
					//table, and it's line to the line table.
					counter++;
					lexemes.add(currLex);
					currToken = Token.DIV_OPERATOR;
					tokens.add(currToken);
					currLine.add(line);
					currLex = "";
					continue;
				}

				//If we encounter an equals sign, add it to the currLex.
				else if (lexBuilder.equals("=")) {
					currLex += lexBuilder;
					counter++;
					if (counter < input.length()) {

						//Check the next character for an equals sign.
						temp = input.charAt(counter);
						lexBuilder = Character.toString(temp);

						//if we encounter an equals sign, add it to the current lexeme, place == into the lexemes table,
						//add it's token type to the tokens table, and the line to the line table.
						if(lexBuilder.equals("=")) {
							counter++;
							currLex += lexBuilder;
							counter++;
							lexemes.add(currLex);
							currToken = Token.EQ_OPERATOR;
							tokens.add(currToken);
							currLine.add(line);
							currLex = "";
							continue;
						}
					}

					//If we don't encounter another = sign, add it to the lexemes table as =, it's token type to the tokens
					//table, and it's line to the line table.
					counter++;
					lexemes.add(currLex);
					currToken = Token.ASSIGNMENT_OPERATOR;
					tokens.add(currToken);
					currLine.add(line);
					currLex = "";
					continue;
				}

				//If we encounter a less than sign, add it to the currLex.
				else if (lexBuilder.equals("<")) {
					currLex += lexBuilder;
					counter++;
					if (counter < input.length()) {
						//Check the next character for an equals sign.
						temp = input.charAt(counter);
						lexBuilder = Character.toString(temp);

						//if we encounter an equals sign, add it to the current lexeme, place <= into the lexemes table,
						//add it's token type to the tokens table, and the line to the line table.
						if(lexBuilder.equals("=")) {
							counter++;
							currLex += lexBuilder;
							counter++;
							lexemes.add(currLex);
							currToken = Token.LE_OPERATOR;
							tokens.add(currToken);
							currLine.add(line);
							currLex = "";
							continue;
						}
					}
					//If we don't encounter a = sign, add it to the lexemes table as <, it's token type to the tokens
					//table, and it's line to the line table.
					counter++;
					lexemes.add(currLex);
					currToken = Token.LT_OPERATOR;
					tokens.add(currToken);
					currLine.add(line);
					currLex = "";
					continue;
				}

				//If we encounter a greater than sign, add it to the currLex.
				else if (lexBuilder.equals(">")) {
					currLex += lexBuilder;
					counter++;
					if (counter < input.length()) {

						//Check the next character for an equals sign.
						temp = input.charAt(counter);
						lexBuilder = Character.toString(temp);

						//if we encounter an equals sign, add it to the current lexeme, place >= into the lexemes table,
						//add it's token type to the tokens table, and the line to the line table.
						if(lexBuilder.equals("=")) {
							counter++;
							currLex += lexBuilder;
							lexemes.add(currLex);
							currToken = Token.GE_OPERATOR;
							tokens.add(currToken);
							currLine.add(line);
							currLex = "";
							continue;
						}
					}

					//If we don't encounter a = sign, add it to the lexemes table as >, it's token type to the tokens
					//table, and it's line to the line table.
					counter++;
					lexemes.add(currLex);
					currToken = Token.GT_OPERATOR;
					tokens.add(currToken);
					currLine.add(line);
					currLex = "";
					continue;

				}

				//If we encounter a not sign, add it to the currLex.
				else if (lexBuilder.equals("!")) {
					currLex += lexBuilder;
					counter++;
					if (counter < input.length()) {

						//Check the next character for an equals sign.
						temp = input.charAt(counter);
						lexBuilder = Character.toString(temp);

						//if we encounter an equals sign, add it to the current lexeme, place ~= into the lexemes table,
						//add it's token type to the tokens table, and the line to the line table.
						if(lexBuilder.equals("=")) {
							counter++;
							counter++;
							currLex += lexBuilder;
							lexemes.add(currLex);
							currToken = Token.NE_OPERATOR;
							tokens.add(currToken);
							currLine.add(line);
							currLex = "";
							continue;
						}
					}

				}
			}


			//Method for invalid characters. If we ever encounter a bad character, we will note the line and the character, and then
			//exit out of the loop. Afterwards, we exit the lexing process and print the error
			isitNum = isNum(lexBuilder);
			if (!characters.contains(lexBuilder) && (isitNum == false)) {
				if (!lexBuilder.equals(")")) {
					String error = "Error detected at line " + line +". Invalid character: " + lexBuilder;
					errors.add(error);
					return;
				}
			}

			//If our current lexeme does not match any of the above, we'll add it to the currLex. If we're at the 
			//end of a line and we have some int literal, we'll go ahead and add it.
			else{
				currLex += lexBuilder;
				counter++;
				isitNum = isNum(currLex);
				if (isitNum == true) {
					if (counter >= input.length()){
						lexemes.add(currLex);
						currToken = Token.LITERAL_INTEGER;
						tokens.add(currToken);
						currLine.add(line);
						empty = true;
						continue;
					}
				}

				continue;
			} 

		}
	}

	//Checks if it is a number. If it is, we return true. Otherwise, false.
	public static boolean isNum(String in) {
		try {
			int x = Integer.parseInt(in);
		}
		catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}



