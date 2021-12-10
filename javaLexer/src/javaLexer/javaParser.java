/*
 * Class:       CS 4308 Section 02
 * Term:        Fall 2021
 * Name:        Tyler Gustat
 * Instructor:  Sharon Perry
 * Project:     Deliverable 2+3: Parser + Interpreter
 */


package javaLexer;

import java.util.*;

public class javaParser {

	//The lists we create are used to retrieve the values of the lexemes, tokens, errors, and lines
	//for each respective token and lexeme in the event of some error.
	public static List<String> errors;
	public static List<Token> oldTokens;
	public static List<String> oldLex;
	public static List<Integer> lines;
	public static List<String> variables = new ArrayList<String>();
	public static List<Integer> variableVals = new ArrayList<Integer>();
	public static List<String> printList = new ArrayList<String>();
	
	public static List<String> getErrors(){
		return errors;
	}
	
	public static List<String> getPrintList(){
		return printList;
	}

	//These are four variables we use to help track our parsing status. The token
	//currTok is used to compare and check the value of our current token, currPos tracks
	//and currTok tracks which token we are going to retrieve. The string error is used to 
	//create some error message if we encounter one. The whileFlag is used for when we enter
	//a while loop. It becomes true so that we know when to leave the loop. The ifCounter
	//variable is used to track the total number of if statements encountered, in the event of 
	//looping. If an else statement is encountered while we have yet to see some if statement, we
	//will track the error, and return it.
	static int currPos = 0;
	static Token currTok;
	static String error;
	static boolean whileFlag = false;
	static boolean ifBool = true;
	static int ifCounter = 0;

	public javaParser(String fileChoice) {

		//Creates the lexer, which retrieves the lexemes, tokens, their line numbers,
		//and the first error it encounters from the selected input file.
		javaLexer lexer = new javaLexer(fileChoice);

		//Uses the getter methods to instantiate the values of the lexemes, tokens, line numbers
		//and errors for the parser to use.
		errors = lexer.getErrorList();
		oldTokens = lexer.getTokList();
		oldLex = lexer.getLexList();
		lines = lexer.getLineList();

		//If there aren't any lexical errors, we'll enter the actual parsing / interpretation of the program
		if (errors.isEmpty()) {
			//Enters the program block, which is the main body of the function.
			program();

			//When we return from the end of the function, if there are any errors we will output them and let
			//the user know the program is invalid. Otherwise, we will tell the user it's a valid program. For the
			//purpose of interpretation, this is commented out.
			/*
			if (!errors.isEmpty()) {
				for(String error : errors)
					System.out.println(error);
				System.out.println("\n\nInvalid Program");
			}

			else {
				System.out.println("\n\nValid program");
				for(String printer : printList) {
					System.out.println(printer);
				}
			}
			*/


		}
	}


	//Program function. This is where the syntax analysis begins. We print out the basic setup 
	//of the program based on our definitions of what our language looks like. We start by
	//checking the first four tokens. If we don't encounter function, an id, and (), we know
	//that the program is invalid, and exit. Otherwise, we enter the block. The function
	//setTok advances us to the next token.
	public static void program() {
		//All print statements are commented out for the purpose of interpretation only.
		//System.out.print("<program> --> ");
		setTok();

		if(currTok != Token.KW_FUNCTION) {
			error = "Syntatic Error encountered on line " + lines.get(currPos) +".\nExpected Token: "
					+ "function.\nEncountered token: " + currTok;
			errors.add(error);
			return;

		}
		else
			//All print statements are commented out for the purpose of interpretation only.
			//System.out.print("function ");
		setTok();

		if(currTok != Token.ID) {
			error = "Syntatic Error encountered on line " + lines.get(currPos) +".\nExpected Token: "
					+ "ID.\nEncountered token: " + currTok;
			errors.add(error);
			return;

		}
		else
			//All print statements are commented out for the purpose of interpretation only.
			//System.out.print("<" + currTok + ": " + oldLex.get(currPos - 1) + "> ");
		setTok();


		if(currTok != Token.lPARENS) {
			error = "Syntatic Error encountered on line " + lines.get(currPos) +".\nExpected Token: "
					+ "LPARENS.\nEncountered token: " + currTok;
			errors.add(error);
			return;

		}
		else
			//All print statements are commented out for the purpose of interpretation only.
			//System.out.print("(");
		setTok();

		if(currTok != Token.RPARENS) {
			error = "Syntatic Error encountered on line " + lines.get(currPos) +".\nExpected Token: "
					+ "RPARENS.\nEncountered token: " + currTok;
			errors.add(error);
			return;

		}
		else
			//All print statements are commented out for the purpose of interpretation only.
			//System.out.print(") <block> end\n");
		setTok();

		//Enter the block portion of our program.
		block();

		if(currTok != Token.KW_END && errors.isEmpty()) {
			error = "Syntatic Error encountered on line " + lines.get(currPos) +".\nExpected Token: "
					+ "KW_END.\nEncountered token: " + currTok;
			errors.add(error);
			return;

		}
		return;

	}

	//The block of the program is where the chunk of it lies. Due to the definition of a block,
	//we understand that it is always defined as either a statement or a statement followed by
	//another block. To simulate this, we use a do-while loop. Until we encounter the end keyword,
	//we will not exit the block. Additionally, if we enter another block, if the whileFlag isn't false
	//we will then exit that secondary block as well.
	public static void block() {
		//All print statements are commented out for the purpose of interpretation only.
		//System.out.println("<block> --> <statement> | <statement> <block>");
		do {
			if (!errors.isEmpty())
				return;
			statement();
		} while(currTok != Token.KW_END && whileFlag == false);
	}

	//The statement function is used to simulate the definition of some statement. Within
	//our sublanguage of Julia, we know we only have four statements to worry about: Assignment,
	//print, if, and while statements. We also include the then-else statements to advance to the
	//next token if they are encountered. We use an integer known as ifCount to track the number of
	//if statements encountered. If we are at 0 and we enter the else portion of an if-statement, 
	//we know there's an error and return. Otherwise, we continue on.
	public static void statement() {
		Token caseToken = currTok;

		switch(caseToken) {

		case ID:
			//All print statements are commented out for the purpose of interpretation only.
			//System.out.println("<statement> --> <assignment_statement>");
			assignmentStatement();
			break;

		case KW_PRINT:
			//All print statements are commented out for the purpose of interpretation only.
			//System.out.println("<statement> --> <print_statement>");
			String printVar = printStatement();
			boolean numeric;
			numeric = isNum(printVar);
			if (printVar.length() > 0) {
				if (ifBool == true) {
					if (variables.contains(printVar)) {
						printList.add(variableVals.get(variables.indexOf(printVar)).toString());
						ifBool = false;
					}
					else if (numeric) {
						printList.add(printVar);
						ifBool = false;
					}
				}
				else {
					ifBool = true;
				}
			}
			break;

		case KW_WHILE:
			//All print statements are commented out for the purpose of interpretation only.
			//System.out.println("<statement> --> <while_statement>");
			whileFlag = true;
			whileStatement();
			if (!errors.isEmpty())
				return;
			break;

		case KW_IF:
			ifCounter++;
			//All print statements are commented out for the purpose of interpretation only.
			//System.out.println("<statement> --> <if_statement>");
			ifStatement();
			break;

		case KW_THEN:
			setTok();
			break;

		case KW_ELSE:
			if(ifCounter == 0) {
				error = "Syntatic Error encountered on line " + lines.get(currPos) +".\nExpected a preceding IF token.";
				errors.add(error);
				return;
			}
			ifCounter--;
			setTok();
			return;


		default:
			error = "Syntatic Error encountered on line " + lines.get(currPos) +".\nExpected Token: "
					+ "KW_IF, ID, KW_WHILE, or KW_PRINT.\nEncountered token: " + currTok;
			errors.add(error);
			return;
		}
		return;
	}

	/* Whenever we encounter some ID immediately after entering a block statement, we know it
	 * is for some assignment statement. This allow us to detect an assignment statement which
	 * the parser will recognize. From there, we increment and check for an assignment operator.
	 * If we don't find one, we will throw an error. Else, we will advance and check for an
	 * arithmetic expression. If we fail to find one, we will throw an error and return.
	 */
	public static void assignmentStatement() {
		//All print statements are commented out for the purpose of interpretation only.
		//System.out.println("<assignment_statement> --> <"+ currTok + ": "+ oldLex.get(currPos - 1) +"> <assignment_operator> <arithmetic_expression>");
		
		//This variable is used to track the kind of assignment statement. If it's =, we will just set the value to be the assigned variable.
		//Otherwise, if it's some other kind, we will add, subtract, multiply by, or divide by the value.
		String oldVariable = oldLex.get(currPos);
		
		//Variable tracks the actual variable we are using.
		String variable = oldLex.get(currPos - 1);
		setTok();

		if (currTok == Token.ASSIGNMENT_OPERATOR) {
			if (oldVariable.equals("=")) {
				setTok();
				int value = arithExpr();
				if (value >= 0) {
					//This statement adds the variable to the variable table if it isn't already there. If it already exists,
					//we will instead just reset the value to the new desired value.
					if(!variables.contains(variable)) {
						variables.add(variable);
						variableVals.add(value);
					}
					else
						variableVals.add(variables.indexOf(variable), value);
				}
			}
			
			//This pressuposes the variable exists. If it doesn't exist, we encounter a runtime error.
			else if (oldVariable.equals("+=")) {
				setTok();
				int adder = arithExpr();
				
				//This statement adds the given adder value to the variable value.
				variableVals.set(variables.indexOf(variable), variableVals.get(variables.indexOf(variable)) + adder);
			}
			else if (oldVariable.equals("-=")) {
				setTok();
				int adder = arithExpr();

				//This statement subtracts the given adder value from the variable value.
				variableVals.set(variables.indexOf(variable), variableVals.get(variables.indexOf(variable)) - adder);
			}
			else if (oldVariable.equals("*=")) {
				setTok();
				int adder = arithExpr();

				//This statement multiplies the given adder value to the variable value.
				variableVals.set(variables.indexOf(variable), variableVals.get(variables.indexOf(variable)) * adder);
			}
			else if (oldVariable.equals("/=")) {
				setTok();
				int adder = arithExpr();

				//This statement divides the given adder value from the variable value.
				variableVals.set(variables.indexOf(variable), variableVals.get(variables.indexOf(variable)) / adder);
			}
		}

		else {
			error = "Syntatic Error encountered on line " + lines.get(currPos) +".\nExpected Token: "
					+ "ASSIGNMENT_OPERATOR.\nEncountered token: " + currTok;
			errors.add(error);
			return;
		}
	}

	/* This block handles whenever we want to break down what an arithmetic expression is. When called,
	 * we check to see if there is some identifier or literal integer. If we encounter either, we will advance
	 * and check for some arithmetic operator. In the event we find one of those, we will again call another
	 * arithmetic expression to see what it is evaluating. In the event we don't encounter an operator
	 * after advancing, we will instead just return the independent identifier or literal integer. If we encounter a
	 * literal integer or id, we will print it's value/character alongside the token. In the event we enter this and we don't have an 
	 * identifier or integer, we will instead throw an error and return it.
	 * 
	 * !!!! Changes as of addition of parsing functionality !!!!
	 * This method now also returns a value of integer. This is done to evaluate arithmetic expressions which occur as the result of an
	 * assignment statement. This works by tracking the old value, and then performing the preferred operation using the 2ndary values found.
	 * Due to the recursive nature of this function, it will run until the end of the give expression. That is, until it encounters two numbers
	 * or variables back to back without some operator in between.
	 */
	public static int arithExpr() {
		Token arithTok = currTok;

		switch(arithTok) {

		case ID:
			Token letterTok = currTok;
			String letter = oldLex.get(currPos - 1);
			setTok();
			if ((currTok == Token.ADD_OPERATOR) || (currTok == Token.SUB_OPERATOR) || (currTok == Token.MUL_OPERATOR) || (currTok == Token.DIV_OPERATOR)) {
				//All print statements are commented out for the purpose of interpretation only. 
				//System.out.println("<arithmetic_expression> --> <" + letterTok + ": " + letter + "> <" + currTok + "> <arithmetic_expr>");
				Token opTok = currTok;
				setTok();
				int newVal = arithExpr();
				//Following series of else if statements are used to modify the value of some existing variable
				//with another new value encountered.
				if (opTok == Token.ADD_OPERATOR) 
					return variableVals.get(variables.indexOf(letter)) + newVal;
				
				else if (opTok == Token.SUB_OPERATOR)
					return variableVals.get(variables.indexOf(letter)) - newVal;
				
				else if (opTok == Token.MUL_OPERATOR)
					return variableVals.get(variables.indexOf(letter)) * newVal;
				
				else if (opTok == Token.DIV_OPERATOR)
					return variableVals.get(variables.indexOf(letter)) / newVal;
			}
			//All print statements are commented out for the purpose of interpretation only.
			//System.out.println("<arithmetic_expression> --> <" + letterTok + ": " + oldLex.get(currPos - 2) + ">");
			return -50;

		case LITERAL_INTEGER:
			int value = Integer.parseInt(oldLex.get(currPos - 1));
			Token intTok = currTok;
			setTok();
			if ((currTok == Token.ADD_OPERATOR) || (currTok == Token.SUB_OPERATOR) || (currTok == Token.MUL_OPERATOR) || (currTok == Token.DIV_OPERATOR)) {
				//All print statements are commented out for the purpose of interpretation only.
				//System.out.println("<arithmetic_expression> --> <" + intTok +": " + value +"> <" + currTok + "> <arithmetic_expr>");
				Token opTok = currTok;
				setTok();
				int newVal = arithExpr();
				
				//The following series of events are used to evaluate when we attempt to combine two
				//numbers through the basic math operations. We return the combo with the given number.
				if (opTok == Token.ADD_OPERATOR) 
					return value + newVal;
				
				else if (opTok == Token.SUB_OPERATOR)
					return value - newVal;
				
				else if (opTok == Token.MUL_OPERATOR)
					return value * newVal;
				
				else if (opTok == Token.DIV_OPERATOR)
					return value / newVal;
			}
			//All print statements are commented out for the purpose of interpretation only.
			//System.out.println("<arithmetic_expression> --> <" + intTok + ": " + value +">");
			//setTok();
			return value;

		default:
			error = "Syntatic Error encountered on line " + lines.get(currPos) +".\nExpected Token: "
					+ "ID or LITERAL_INT.\nEncountered token: " + currTok;
			errors.add(error);
			return -20;
		}
	}

	/* If we encounter a print statement, we enter this block. Upon entering it, we check
	 * immediately for a set of parentheses. If we encounter the left parentheses, we will
	 * continue and check for an arithmetic expression. Otherwise, we will throw an error.
	 * If we encounter an arithmetic expression, and it is valid, we will then check for the
	 * right parentheses. Again, if we don't encounter it, we will throw an error. Otherwise,
	 * we exit the print statement.
	 * 
	 * !!!! UPDATE FOR IMPLEMENTATION OF INTERPRETER !!!!
	 * To coincide with the addition of print functionality, we now return a string of the item we want to print now. This
	 * is done to help us keep track of the print statements as they appear. In the instance of our first 2 test files, we
	 * will return the variable to be printed.
	 */
	public static String printStatement(){
		//All print statements are commented out for the purpose of interpretation only.
		//System.out.println("<print_statement> --> print ( <arithmetic_expr> )");
		setTok();
		
		//Creates a variable to track the statement we will print.
		String printVar;

		if(currTok == Token.lPARENS) {
			setTok();
			
			//Sets the variable we are printing to be the item contained within the parentheses of the 
			//print statement.
			printVar = oldLex.get(currPos - 1);
			arithExpr();
			if(currTok == Token.RPARENS) {
				setTok();
			}
			else {
				error = "Syntatic Error encountered on line " + lines.get(currPos) +".\nExpected Token: "
						+ "RPARENS.\nEncountered token: " + currTok;
				errors.add(error);
				return printVar;	
			}
		}

		else {
			error = "Syntatic Error encountered on line " + lines.get(currPos) +".\nExpected Token: "
					+ "LPARENS.\nEncountered token: " + currTok;
			errors.add(error);
			return "";	
		}
		return printVar;


	}

	/* The while statement is the trickiest of the statements to handle. When we encounter
	 * the while token, we enter this block. We then advance the counter, and check for a
	 * boolean expression. After evaluating it, if an error isn't found, we we will enter
	 * the block portion of the while statement. We will run the block, and then when we
	 * encounter an end statement we will leave the while loop from the block. If we don't
	 * find an end statement when we exit the second block, we throw an error. Otherwise, we
	 * will exit, reset the whileFlag, and advance the token for the next statement to be 
	 * evaluated, if there is one. 
	 * 
	 * !!!! UPDATE FOR IMPLEMENTATION WITH INTERPRETER !!!!
	 * To coincide with the addition of interpretation within the parser, the while statement has been modified. We use two
	 * instance lists, boolExpr and bloExpr, to track the block and boolean expression respectively. From there, after checking to make
	 * sure that the syntax of the statement is correct, we send it to a new function, boolEval, to evaluate the boolean expression. We
	 * also send the type of token, in this instance a while statement, to inform the expression to use the set of while statement versions.
	 */
	public static void whileStatement() {
		//All print statements are commented out for the purpose of interpretation only.
		//System.out.println("<while_statement> --> while <boolexpr> <block> end");
		String lex = oldLex.get(currPos);
		setTok();

		//Tracks the boolean expression. As we have it set up, we can track boolean expressions up to length 3,
		//that being it contains some base value or variable, the operator, and a 2ndary value or variable.
		List<String> boolExpr = new ArrayList<String>();
		boolExpr.add(lex);
		for (int i = currPos; i < currPos + 2; i++) {
			boolExpr.add(oldLex.get(i));
		}
		boolExpr();

		//Tracks the arithmetic expression. As we have it set up, we can track boolean expressions up to length 3,
		//that being it contains some base value or variable, the operator, and a 2ndary value or variable. I would
		//need to retool the entire parser to introduce support for expressions longer than 3.
		String var2 = oldLex.get(currPos - 1);
		List<String> bloExpr = new ArrayList<String>();
		bloExpr.add(var2);
		int newPtr = currPos;
		while (oldTokens.get(newPtr) != Token.KW_END) {
			bloExpr.add(oldLex.get(newPtr));
			newPtr++;
		}
		
		block();
		if (currTok != Token.KW_END) {
			error = "Syntatic Error encountered on line " + lines.get(currPos) +".\nExpected Token: "
					+ "KW_END.\nEncountered token: " + currTok;
			errors.add(error);
			return;
		}
		whileFlag = false;
		evalBool(boolExpr, bloExpr);
		setTok();
	}

	/* When an if token is encountered, we enter this if statement. We advance the token, and then check for
	 * a boolean expression. After we evaluate the boolean expression, we return and if there isn't an error
	 * we will evaluate the next block. If we encounter a then statement, we will advance, and the same will
	 * happen with an else statement. After we finish evaluating the following blocks, we check for an end statement
	 * and if we find one, we advance to the next statement before returning to our parent block. Otherwise, an error
	 * is thrown.
	 */
	public static void ifStatement() {
		//All print statements are commented out for the purpose of interpretation only.
		//System.out.println("<if_statement> --> if <boolexpr> then <block> else <block> end");
		String lex = oldLex.get(currPos);
		setTok();
		
		//Tracks the boolean expression. As we have it set up, we can track boolean expressions up to length 3,
		//that being it contains some base value or variable, the operator, and a 2ndary value or variable.
		List<String> boolExpr = new ArrayList<String>();
		boolExpr.add(lex);
		for (int i = currPos; i < currPos + 2; i++) {
			boolExpr.add(oldLex.get(i));
		}
		
		boolExpr();
		
		//Uses the existing variable to track if the statement evaluates to true. If it is, we adjust the
		//ifBool variable to be true. Otherwise, we set it to false. We use this to track whether, when encountering
		//two statements, to execute the first or the 2nd. If it's false, we'll do the second. Otherwise, we do the first.
		boolean ifStmt = ifEval(boolExpr);
		if (ifStmt == true) {
			ifBool = true;
		}
		else {
			ifBool = false;
		}

		block();
		if (currTok == Token.KW_END) {
			setTok();
		}
		else {
			error = "Syntatic Error encountered on line " + lines.get(currPos) +".\nExpected Token: "
					+ "KW_END.\nEncountered token: " + currTok;
			errors.add(error);
			return;
		}
		
		//Resets the vriable in case we encounter another if statement.
		ifBool = true;
	}


	/* The boolean expression function is used to check for some boolean expression. We begin
	 * by checking for some arithmetic expression. If we find one, we return to this block and
	 * check for a relative operator. If we don't find we, we output an error. Otherwise, we check
	 * for another arithmetic expression. If we don't find another one, we'll return an error. Otherwise,
	 * we will just turn.
	 */
	public static void boolExpr() {
		//All print statements are commented out for the purpose of interpretation only.
		//System.out.println("<boolexpr> --> <arithexpr> <relativeop> <arithexpr>");
		String boolExpr = "";
		String num = "";
		arithExpr();
		if((currTok == Token.LT_OPERATOR) || (currTok == Token.LE_OPERATOR) || (currTok == Token.GT_OPERATOR) || (currTok == Token.GE_OPERATOR) || (currTok == Token.NE_OPERATOR) || (currTok == Token.EQ_OPERATOR)) {
			//All print statements are commented out for the purpose of interpretation only.
			//System.out.println("<relativeop> --> <" + currTok +">");
			boolExpr += oldLex.get(currPos - 1);
			setTok();
			boolExpr += arithExpr();
			return ;
		}
		else {
			error = "Syntatic Error encountered on line " + lines.get(currPos) +".\nExpected Token: "
					+ "LT_OPERATOR, GT_OPERATOR, LE_OPERATOR, GE_OPERATOR, NE_OPERATOR, or EQ_OPERATOR .\nEncountered token: " + currTok;
			errors.add(error);
			return ;
		}
	}

	//Handles the task of advancing the token we're handling and passing it to the parser
	public static void setTok() {
		currTok = oldTokens.get(currPos);
		currPos++;
	}
	
	/* This is a new method included in order to help the while statement with execution of the contents
	 * within the while statement. As it currently stands, the limitation is that it is only able to evaluate
	 * arithmetic expressions. I likely would be unable to implement opposite evaluations for the boolean expression
	 * without retooling the entire parser to be more consistent with a tree-like structure. As it stands, this will
	 * evaluate some bool expression of length three, and takes 2 variables as parameters. These two parameters are the lists
	 * bool and expression, which represent the boolean expression and the block expression itself. In it's current state, this
	 * only supports an assignment instruction of length 3. 
	 */
	public static boolean evalBool(List<String> bool, List<String> expression) {
		
		//Tracks the variable we are checking, the operator to compare with, and the value to evaluate the comparison.
		String var, op, val;
		var = bool.get(0);
		op = bool.get(1);
		val = bool.get(2);
		

		//We use the operand as the case for which we attempt to evaluate the expression. We loop infinitely
		//until we find the comparison is false. The block operation is handled in the evalExpr method, which we
		//call within the while loop.
		switch(op) {
		case "<":
			while (variableVals.get(variables.indexOf(var)) < Integer.parseInt(val)) {
				evalExpr(expression);
			}
			break;
			
		case "<=":
			while (variableVals.get(variables.indexOf(var)) <= Integer.parseInt(val)) {
				evalExpr(expression);
			}
			break;
			
		case ">":
			while (variableVals.get(variables.indexOf(var)) > Integer.parseInt(val)) {
				evalExpr(expression);
			}
			break;
			
		case ">=":
			while (variableVals.get(variables.indexOf(var)) >= Integer.parseInt(val)) {
				evalExpr(expression);
			}
			break;
			
		case "!=":
			while (variableVals.get(variables.indexOf(var)) != Integer.parseInt(val)) {
				evalExpr(expression);
			}
			break;
			
		case "==":
			while (variableVals.get(variables.indexOf(var)) == Integer.parseInt(val)) {
				evalExpr(expression);
			}
			break;
		}
		return false;
		
	}

	
	/* This method evaluates an arithmetic expression of length three. As it is currently tooled, we only handle
	 * the assignment statements of addition, multiplication, subtraction, and division. We continously evaluate
	 * these until the while statement in the calling function returns false. Otherwise, we modify the variable's
	 * value until it evaluates as fall with the following operators: <, <=, >, >=, !=, and ==.
	 */
	public static void evalExpr(List<String> expression) {
		String var, op, val;		
		var = expression.get(0);
		op = expression.get(1);
		val = expression.get(2);
		
		switch(op) {
			case "+=":
				variableVals.set(variables.indexOf(var), variableVals.get(variables.indexOf(var)) + Integer.parseInt(val));
				break;
			case "-=":
				variableVals.set(variables.indexOf(var), variableVals.get(variables.indexOf(var)) + Integer.parseInt(val));
				break;
			case "*=":
				variableVals.set(variables.indexOf(var), variableVals.get(variables.indexOf(var)) + Integer.parseInt(val));
				break;
			case "/=":
				variableVals.set(variables.indexOf(var), variableVals.get(variables.indexOf(var)) + Integer.parseInt(val));
				break;
		}
		
	}
	
	/* The final completely new function added. We use this to check if the given boolean expression is true.
	 * We call this from the if statement with an if statement, and return true if it is true. Otherwise, we
	 * return false. This is done to allow us to determine if we want to interpret the if statement, or the 
	 * else statement. By default, we return false if we can't evaluate it.
	 */
	public static boolean ifEval(List<String> boolExpr) {
		String var, op, val;
		var = boolExpr.get(0);
		op = boolExpr.get(1);
		val = boolExpr.get(2);
		
		switch (op) {
			case "<":
				if (variableVals.get(variables.indexOf(var)) < Integer.parseInt(val))
					return true;
				break;
				
			case "<=":
				if (variableVals.get(variables.indexOf(var)) <= Integer.parseInt(val))
					return true;
				break;
				
			case ">":
				if (variableVals.get(variables.indexOf(var)) > Integer.parseInt(val))
					return true;
				break;
				
			case ">=":
				if (variableVals.get(variables.indexOf(var)) >= Integer.parseInt(val))
					return true;
				break;
				
			case "!=":
				if (variableVals.get(variables.indexOf(var)) != Integer.parseInt(val))
					return true;
				break;
				
			case "==":
				if (variableVals.get(variables.indexOf(var)) == Integer.parseInt(val))
					return true;
				break;
		}
		return false;
	}
	
	//This method is included from LExer to allow us to check if a given input is a number. We use
	//this to help us print singular normals which are unassociated with variables.
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


