/*
 * Class:       CS 4308 Section 02
 * Term:        Fall 2021
 * Name:        Tyler Gustat
 * Instructor:  Sharon Perry
 * Project:     Interpreter Project
 */


package javaLexer;

public enum Token {

	/* Establishes the various symbols the Lexer will be recognizing
	 * over its runtime. They are defined as follows:
	 * 
	 * WHAT WE RECOGNIZE				WHAT WE RETURN
	 * -----------------------------------------------------------
	 * id 						--> 	letter
	 * literal_integer 			--> 	digit literal_integer | digit
	 * assignment_operator		--> 	=
	 * le_operator	 			--> 	<=
	 * lt_operator	 			--> 	<
	 * ge_operator	 			--> 	>=
	 * gt_operator				--> 	>
	 * eq_operator				--> 	==
	 * ne_operator				--> 	~=
	 * add_operator				--> 	+
	 * sub_operator				--> 	-
	 * mul_operator				--> 	*
	 * div_operator				--> 	/
	 * lparens					-->		(
	 * rparens					-->		)
	 * 
	 * We also include the keywords in this table for the sake of
	 * simplicity and space. They are denoted by KW_somekeyword and
	 * will simply return the name of the keyword if they are found.
	 */
	ID,
	LITERAL_INTEGER,
	ASSIGNMENT_OPERATOR,
	LE_OPERATOR,
	LT_OPERATOR,
	GE_OPERATOR,
	GT_OPERATOR,
	EQ_OPERATOR,
	NE_OPERATOR,
	ADD_OPERATOR,
	SUB_OPERATOR,
	MUL_OPERATOR,
	DIV_OPERATOR,
	lPARENS,
	RPARENS,
	KW_FUNCTION,
	KW_PRINT,
	KW_END,
	KW_IF,
	KW_THEN,
	KW_ELSE,
	KW_WHILE,
	KW_FOR,
	KW_DO;
}
