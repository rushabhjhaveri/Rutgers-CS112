package apps;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	/**
	 * Expression to be evaluated
	 */
	String expr;                

	/**
	 * Scalar symbols in the expression 
	 */
	ArrayList<ScalarSymbol> scalars;   

	/**
	 * Array symbols in the expression
	 */
	ArrayList<ArraySymbol> arrays;

	/**
	 * String containing all delimiters (characters other than variables and constants), 
	 * to be used with StringTokenizer
	 */
	public static final String delims = " \t*+-/()[]";

	/**
	 * Initializes this Expression object with an input expression. Sets all other
	 * fields to null.
	 * 
	 * @param expr Expression
	 */
	public Expression(String expr) {
		this.expr = expr;
	}

	/**
	 * Populates the scalars and arrays lists with symbols for scalar and array
	 * variables in the expression. For every variable, a SINGLE symbol is created and stored,
	 * even if it appears more than once in the expression.
	 * At this time, values for all variables are set to
	 * zero - they will be loaded from a file in the loadSymbolValues method.
	 */
	public void buildSymbols() {
		/** COMPLETE THIS METHOD **/
		scalars = new ArrayList<ScalarSymbol>();
		arrays = new ArrayList<ArraySymbol>();
		/*
    	//------Hard-coded to understand LoadSymbol()------
    	ScalarSymbol a = new ScalarSymbol("a");
    	scalars.add(a);
    	ScalarSymbol b = new ScalarSymbol("b");
    	scalars.add(b);
    	ArraySymbol A = new ArraySymbol("A");
    	arrays.add(A);
    	ArraySymbol B = new ArraySymbol("B");
    	arrays.add(B);
    	ScalarSymbol d = new ScalarSymbol("d");
    	scalars.add(d);
		 */
		expr = expr.replaceAll("\\s", "").trim();
		StringTokenizer st = new StringTokenizer(expr, delims);
		int numTokens = st.countTokens();
		char ch = 0;
		int charpos = 0;
		String token = "";
		int index = 0;
		int len = 0;
		int ssi = 0;
		int asi = 0;
		while(st.hasMoreTokens()){
			token = st.nextToken();
			index = expr.indexOf(token);
			len = token.length();
			//System.out.println("numTokens:" + numTokens + " token:" + token + " index:" + index + " len:" + len + " expr.length=" + expr.length());
			if(numTokens == 1){
				if(Character.isDigit(token.charAt(0))){
					continue;
				}
				//example expr: a
				ScalarSymbol ss = new ScalarSymbol(token);
				scalars.add(ss);
				break;
			}
			if(Character.isDigit(token.charAt(0))){
				continue;
			}
			if( (index +len) >= expr.length() ){
				charpos = index + (len-1);
			}
			else{
				charpos = index + len;
			}
			//System.out.println("charpos:" + charpos);
			ch = expr.charAt(charpos);
			//System.out.println("ch:" +ch);
			if(ch == '['){
				ArraySymbol as = new ArraySymbol(token);
				asi = arrays.indexOf(as);
				if(asi == -1){
					arrays.add(as);
				}
			}
			else{
				ScalarSymbol ss = new ScalarSymbol(token);
				ssi = scalars.indexOf(ss);
				//pnly add if token has not been already added into arraylist scalars
				if(ssi == -1){
					scalars.add(ss);
				}
			}
		}
		/*
    	System.out.println("----Print Scalars----");
    	printScalars();
    	System.out.println("----Print Arrays----");
    	printArrays();
		 */

	}

	/**
	 * Loads values for symbols in the expression
	 * 
	 * @param sc Scanner for values input
	 * @throws IOException If there is a problem with the input 
	 */
	public void loadSymbolValues(Scanner sc) 
			throws IOException {
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
			int numTokens = st.countTokens();
			String sym = st.nextToken();
			ScalarSymbol ssymbol = new ScalarSymbol(sym);
			ArraySymbol asymbol = new ArraySymbol(sym);
			int ssi = scalars.indexOf(ssymbol);
			int asi = arrays.indexOf(asymbol);
			if (ssi == -1 && asi == -1) {
				continue;
			}
			int num = Integer.parseInt(st.nextToken());
			if (numTokens == 2) { // scalar symbol
				scalars.get(ssi).value = num;
			} else { // array symbol
				asymbol = arrays.get(asi);
				asymbol.values = new int[num];
				// following are (index,val) pairs
				while (st.hasMoreTokens()) {
					String tok = st.nextToken();
					StringTokenizer stt = new StringTokenizer(tok," (,)");
					int index = Integer.parseInt(stt.nextToken());
					int val = Integer.parseInt(stt.nextToken());
					asymbol.values[index] = val;              
				}
			}
		}

		//--------Debug Statements--------

		System.out.println("----Print Scalars in loadSymbol()----");
		printScalars();
		System.out.println("----Print Arrays in loadSymbol()----");
		printArrays();
	}


	/**
	 * Evaluates the expression, using RECURSION to evaluate subexpressions and to evaluate array 
	 * subscript expressions.
	 * 
	 * @return Result of evaluation
	 */
	public float evaluate() {
		/** COMPLETE THIS METHOD **/
		// following line just a placeholder for compilation
		return (evaluate1(this.expr, 0));

	}

	/**
	 * Utility method, prints the symbols in the scalars list
	 */
	public void printScalars() {
		for (ScalarSymbol ss: scalars) {
			System.out.println(ss);
		}
	}

	/**
	 * Utility method, prints the symbols in the arrays list
	 */
	public void printArrays() {
		for (ArraySymbol as: arrays) {
			System.out.println(as);
		}
	}

	private float evaluate1(String expr, int recursionCnt){
		expr = expr.replaceAll("\\s", "").trim();
		
		Stack<Float> n = new Stack<Float>();
		Stack<Character> o = new Stack<Character>();
		int strptr = 0;
		int len = expr.length();
		char ch = 0;
		char ch1 = 0;
		String token = "";
		float op1 = 0, op2 = 0;
		char oper = 0;
		float result = 0;
		StringTokenizer st = new StringTokenizer(expr, delims);
		int numTokens = st.countTokens();
		int start = 0;
		int end = 0;
		int tempptr = 0;
		int getTokenCntToSkip = 0;
		ScalarSymbol ssymbol;
		ArraySymbol asymbol;
		int asi = 0;
		int ssi = 0;
		float val = 0;
		int valIndex = 0;
		
		System.out.println("============expr: " + expr + "  Recursion Level = " + recursionCnt + "=============");
		while(strptr < len){
			ch = expr.charAt(strptr);
			System.out.println("Character ch=" + ch);
			if(Character.isDigit(ch)){
				token = st.nextToken();
				System.out.println("Token: " + token);
				if(o.isEmpty()){
					n.push(Float.parseFloat(token));
					strptr += token.length();
				}
				else{
					n.push(Float.parseFloat(token));
					ch1 = o.peek();
					if(ch1 == '/' || ch1 == '*'){
						op2 = n.pop();
						op1 = n.pop();
						oper = o.pop();
						result = compute(op1, oper, op2);
						n.push(result);
					}
					strptr += token.length();
				}
			}
			else if(Character.isLetter(ch)){
				// scalar and array variables are processed here
				token = st.nextToken();
				System.out.println("Token: " + token);
				ssymbol = new ScalarSymbol(token);
				asymbol = new ArraySymbol(token);
				ssi = scalars.indexOf(ssymbol);
				asi = arrays.indexOf(asymbol);
				val = 0;
				System.out.println("ssi = " + ssi);
				if(ssi != -1){//is a scalar
					val = scalars.get(ssi).value;
					System.out.println("val = " + val);
					if(o.isEmpty()){
						n.push(val);
						strptr += token.length();
					}
					else{
						n.push(val);
						ch1 = o.peek();
						if(ch1 == '/' || ch1 == '*'){
							op2 = n.pop();
							op1 = n.pop();
							oper = o.pop();
							result = compute(op1, oper, op2);
							n.push(result);
						}
						strptr += token.length();
					}
				}

				else if(asi != -1){//is an array
					n.push((float)asi);
					strptr += token.length();
				}
				
			}
			else{
				if(ch == '/' || ch == '*'){
					o.push(ch);
					strptr++;
				}
				else if(ch == '+' || ch == '-'){
					if(o.isEmpty()){
						o.push(ch);
						strptr++;
					}
					else{
						ch1 = o.peek();
						if(ch1 == '/' || ch1 == '*'){
							op2 = n.pop();
							op1 = n.pop();
							oper = o.pop();
							result = compute(op1, oper, op2);
							n.push(result);
							o.push(ch);
							strptr++;
						}
						else{
							op2 = n.pop();
							op1 = n.pop();
							oper = o.pop();
							result = compute(op1, oper, op2);
							n.push(result);
							o.push(ch);
							strptr++;
						}
					}
				}
				else if(ch == '('){
					start = strptr + 1;
					end = getSubStrEnd(expr,start, '(');
					System.out.println("Substring: " + expr.substring(start, end) + " start:" + start + " end: " + end);
					
					
					result = evaluate1(expr.substring(start, end), (recursionCnt+1));
					System.out.println("------------Back to recursion level:" + recursionCnt + "-----------------");
		            
					n.push(result);
					
					String oper1="+-*/";
					int tmpStrPtr1 = end + 1;
					if (tmpStrPtr1 < expr.length()) {
						char tmpCh1 = expr.charAt(tmpStrPtr1);
						if ((oper1.indexOf(tmpCh1)) >=0) {
							getTokenCntToSkip = skipTokenCnt(expr, start, end, st);
						}
					}
					
					strptr = end;
					
				}
				else if( (ch == ')') || (ch == ']')){
					strptr++;
				}
				else if(ch == '['){
					start = strptr + 1;
					end = getSubStrEnd(expr, start, '[');
					System.out.println("Substring: " + expr.substring(start, end) + " start:" + start + " end: " + end);
					
					result = evaluate1(expr.substring(start, end), (recursionCnt+1));
					asi = n.pop().intValue();
					valIndex = (int)result;
					if(valIndex < arrays.get(asi).values.length){
						n.push((float)arrays.get(asi).values[valIndex]);
					}
					else{
						System.out.println("Array Index Out of Bounds! Exiting..");
						System.exit(-1);
					}
					
					String oper1="+-*/";
					int tmpStrPtr1 = end + 1;
					if (tmpStrPtr1 < expr.length()) {
						char tmpCh1 = expr.charAt(tmpStrPtr1);
						if ((oper1.indexOf(tmpCh1)) >=0) {
							getTokenCntToSkip = skipTokenCnt(expr, start, end, st);
						}
					}
					
					strptr = end;
					
				}
			}	
		}  // end while (strptr < str.length();
		while(!o.isEmpty()){
			op2 = n.pop();
			op1 = n.pop();
			oper = o.pop();
			result = compute(op1, oper, op2);
			n.push(result);
		}
		return n.pop();
	}

	private float compute(float op1, char oper, float op2){
		float result = 0;
		switch(oper){
		case '/':  
			if (op2 == 0) {
				System.out.println("Divide by zero...exiting");
				System.exit(-1);
			}
			result = op1/op2;
			break;
		case '*': result = op1*op2;
		break;
		case '+': result = op1+op2;
		break;
		case '-': result = op1 - op2;
		break;
		}
		return result;
	}
	private int getSubStrEnd(String expr, int start, char chkParenOrBracket) {
		
		Stack<Character> PorB = new Stack<Character>();
		int tempptr = start;
		char closingParenOrBracket;
		
		if (chkParenOrBracket == '(') {
			closingParenOrBracket = ')';
		} else  {
			closingParenOrBracket = ']';
		}
		
		PorB.push(chkParenOrBracket);
		while(tempptr < expr.length()){
			if(expr.charAt(tempptr) == chkParenOrBracket){
				PorB.push(chkParenOrBracket);
			}
			else if(expr.charAt(tempptr) == closingParenOrBracket){
				PorB.pop();
			}
			if(PorB.isEmpty()){
				break;
			}
			tempptr++;
		}
		if(!PorB.isEmpty()){
			if (chkParenOrBracket == '(')  {
			    System.out.println("Parentheses mismatch! Exiting...");
			} else if (chkParenOrBracket == '(')  {
				System.out.println("Square Bracket mismatch! Exiting...");
			}
			System.exit(-1);
		}
		
		return tempptr;
	}
	
	private int skipTokenCnt(String expr, int start, int end, StringTokenizer st){
		int tempStart = start;
		char ch = 0;
		String str = "";
		int retCnt = 0;
		while(tempStart < end){
			ch = expr.charAt(tempStart);
			if(Character.isDigit(ch) || Character.isLetter(ch)){
				str = st.nextToken();
				System.out.println("Skipping token " + str);
				retCnt++;
				tempStart += str.length();
			}
			else{
				tempStart++;
			}
		}
		return retCnt;
	}
}

