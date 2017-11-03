package apps;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression1 {

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
    public Expression1(String expr) {
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
    	scalars = new ArrayList <ScalarSymbol>();
    	arrays = new ArrayList <ArraySymbol>();
    	
    	for (int i = 0; i < expr.length(); i++){
			if (Character.isLetter(expr.charAt(i))) {
				String expression = "";
				
				while (i < expr.length() && Character.isLetter(expr.charAt(i))){
					expression += expr.charAt(i);
					i++;
				}
							
				if (i < expr.length() && expr.charAt(i) == '[') {
					ArraySymbol as = new ArraySymbol(expression);
					if(!(arrays.contains(as))){
					arrays.add(as);}
				} else {
					ScalarSymbol ss = new ScalarSymbol(expression);
					if(!(scalars.contains(ss))){
						scalars.add(ss);  }
					
				}
			}
		}
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
    }
    
    
    /**
     * Evaluates the expression, using RECURSION to evaluate subexpressions and to evaluate array 
     * subscript expressions.
     * 
     * @return Result of evaluation
     */
    public float evaluate() {
    		return evaluate1(expr);
    }
    
    private float evaluate1 (String expr){
    	Stack <Float> variables = new Stack <Float>();
    	Stack <String> operations = new Stack <String>();
    	Stack <String> temp = new Stack <String>(); 
    	Stack <Float> reverseVariables = new Stack <Float>();
        Stack <String> reverseOperations = new Stack <String>();
    	StringTokenizer st = new StringTokenizer(expr,delims,true);
       	float subans = 0;
    	float scalarValue = 0;
    	float constant = 0;
    	boolean isparen = false;
    	
    	 while (st.hasMoreTokens()) {
    		 String token = st.nextToken();
    		 System.out.println("tokens: " +token);
    		 
    		if(token.equals(" ") || token.equals("\t")){
    			continue;
    		}
    		//does the expression have parenthesis?
    		if(token.equals("(")||token.equals("[")){
    			isparen = true; 
    		}
        	
        	if(isparen == true && (!token.equals(")") && !token.equals("]"))) {
        		temp.push(token);
           		continue; 
        	}
        	
        	
        	if(token.equals(")") || token.equals("]")){
        		String subexpr = "";
        		if(token.equals(")")){        			
        			while(!temp.isEmpty() && !temp.peek().equals("(")){
        				String out = temp.pop();
        				subexpr = out + subexpr; 
        				System.out.println("popped1 "+ out);
        			}
        				System.out.println("subexpr1: "+subexpr); 
        		} 
        		else{
        			while(!temp.isEmpty() && !temp.peek().equals("[")){
        				String out = temp.pop();
        				subexpr = out + subexpr; 
        				System.out.println("popped2 "+ out);
        			}
        				System.out.println("subexpr2: "+ subexpr);
        		}
        		
        		System.out.println("evaluating subexpr: "+ subexpr);
        		subans = evaluate1(subexpr);
        		System.out.println("out of recursion. subanswer is "+ subans);
        		
        	        
        		String bracket = temp.pop();
    	
        		if(bracket.equals("[")){
        			String tkn = temp.pop();
        			ArraySymbol AStemp = new ArraySymbol(tkn);
        			int aai = arrays.indexOf(AStemp);
        			AStemp = arrays.get(aai);
        			subans = AStemp.values[(int)subans];
        		}
    	
        		if(temp.isEmpty()){
        			isparen = false;
        		} else {
        			String tempAgain = subans + "";
        			temp.push(tempAgain);
        			System.out.println("push into temp " + tempAgain);
        			continue;
        		}

	        	variables.push(subans);
	        	System.out.println("subans pushed" + variables.peek());
        	}        	
 
           	else if((token.charAt(0) >= 'a' && token.charAt(0) <= 'z') || (token.charAt(0) >= 'A' && token.charAt(0) <= 'Z')){
    	 		ArraySymbol tempAS = new ArraySymbol(token);
    	 		if(arrays.contains(tempAS)){
    	 			temp.push(token);
    	 			continue;
    	 		}
    	 		
    	 		ScalarSymbol tempSS = new ScalarSymbol(token);
                int ssi = scalars.indexOf(tempSS);
                scalarValue = scalars.get(ssi).value;
                variables.push(scalarValue);
                if (!operations.isEmpty()) {        				
    				String op = operations.peek();
    				if (op.equals("/") || op.equals("*"))
    				calculate(operations, variables,true);
        	 		}
           		}
           	else if (token.equals("+") || token.equals("-") || token.equals("/") || token.equals("*")){
				operations.push(token);
				System.out.println("pushed into op "+ operations.peek());
        	}
        	
          	else {
        		constant = Float.parseFloat(token);
        		variables.push(constant);
        		if (!operations.isEmpty()) {        				
    				String op = operations.peek();
    				if (op.equals("/") || op.equals("*"))
    				calculate(operations, variables, true);
        	 		}
        		System.out.println("pushed into variable "+variables.peek());
              		}
         
       
           	}  //while loop
    	 
    	 if(operations.isEmpty()) {
    		 return variables.pop(); 
    		 }
    
    	 while (!operations.isEmpty()) 
    		 	reverseOperations.push(operations.pop());  
         while(!variables.isEmpty()) 
                 reverseVariables.push(variables.pop()); 
         while (!reverseOperations.isEmpty()) 
         		calculate(reverseOperations, reverseVariables,false);
         		return reverseVariables.pop();        
    	}
    
    private void calculate(Stack <String> operations,Stack <Float> variables, boolean notReverse){
    	String op = operations.pop();
    	float num1 = 0;
    	float num2 = 0;
    	
    	if(notReverse){
      		num2 = variables.pop();
    		num1 = variables.pop();
    	} else {
    		num1 = variables.pop();
        	num2 = variables.pop();
    	}
    	
    	System.out.println("num1: " + num1 + " op: " + op + " num2: " + num2);
		
    	if (op.equals("/")) {
    		variables.push(num1/num2); }
    		     
        else if (op.equals("*")){
        	variables.push(num1*num2); }
        	 
        else if (op.equals("+")){
        	variables.push(num1+num2); }
        	
        else if (op.equals("-")){
        	variables.push(num1-num2);}
        	
    	System.out.println("ans: " + variables.peek());
    	
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

}
