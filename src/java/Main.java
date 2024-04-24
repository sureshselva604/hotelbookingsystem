package com.example.jwt_authentication.Service.survey;



import java.util.*; 
import java.util.Stack; 
 
public class Main { 
 
   // Function to evaluate a mathematical expression given 
   // in string form 
   public static double
   evaluateExpression(String expression) 
   { 
       char[] tokens = expression.toCharArray(); 
 
       // Stacks to store operands and operators 
       Stack<Double> values = new Stack<>(); 
       Stack<Character> operators = new Stack<>(); 
 
       // Iterate through each character in the expression 
       for (int i = 0; i < tokens.length; i++) { 
           if (tokens[i] == ' ') 
               continue; 
 
           // If the character is a digit or a decimal 
           // point, parse the number 
           if ((tokens[i] >= '0' && tokens[i] <= '9') 
               || tokens[i] == '.') { 
               StringBuilder sb = new StringBuilder(); 
               // Continue collecting digits and the 
               // decimal point to form a number 
               while (i < tokens.length 
                      && (Character.isDigit(tokens[i]) 
                          || tokens[i] == '.')) { 
                   sb.append(tokens[i]); 
                   i++; 
               } 
               // Parse the collected number and push it to 
               // the values stack 
               values.push( 
                   Double.parseDouble(sb.toString())); 
               i--; // Decrement i to account for the extra 
                    // increment in the loop 
           } 
           else if (tokens[i] == '(') { 
               // If the character is '(', push it to the 
               // operators stack 
               operators.push(tokens[i]); 
           } 
           else if (tokens[i] == ')') { 
               // If the character is ')', pop and apply 
               // operators until '(' is encountered 
               while (operators.peek() != '(') { 
                   values.push(applyOperator( 
                       operators.pop(), values.pop(), 
                       values.pop())); 
               } 
               operators.pop(); // Pop the '(' 
           } 
           else if (tokens[i] == '+' || tokens[i] == '-'
                    || tokens[i] == '*'
                    || tokens[i] == '/') { 
               // If the character is an operator, pop and 
               // apply operators with higher precedence 
               while (!operators.isEmpty() 
                      && hasPrecedence(tokens[i], 
                                       operators.peek())) { 
                   values.push(applyOperator( 
                       operators.pop(), values.pop(), 
                       values.pop())); 
               } 
               // Push the current operator to the 
               // operators stack 
               operators.push(tokens[i]); 
           } 
       } 
 
       // Process any remaining operators in the stack 
       while (!operators.isEmpty()) { 
           values.push(applyOperator(operators.pop(), 
                                     values.pop(), 
                                     values.pop())); 
       } 
 
       // The result is the only remaining element in the 
       // values stack 
       return values.pop(); 
   } 
 
   // Function to check if operator1 has higher precedence 
   // than operator2 
   private static boolean hasPrecedence(char operator1, 
                                        char operator2) 
   { 
       if (operator2 == '(' || operator2 == ')') 
           return false; 
       return (operator1 != '*' && operator1 != '/') 
           || (operator2 != '+' && operator2 != '-'); 
   } 
 
   // Function to apply the operator to two operands 
   private static double applyOperator(char operator, 
                                       double b, double a) 
   { 
       switch (operator) { 
       case '+': 
           return a + b; 
       case '-': 
           return a - b; 
       case '*': 
           return a * b; 
       case '/': 
           if (b == 0) 
               throw new ArithmeticException( 
                   "Cannot divide by zero"); 
           return a / b; 
       } 
       return 0; 
       
   } 
 
   // Driver Code 
   public static void main(String[] args) 
   { 
       String exp = "1.0*(5*102.0-180.0)*25/100+169456.0+10.0*23.0"; 
       double result = evaluateExpression(exp); 
       System.out.println("Result: " + result); 
   } 
}

