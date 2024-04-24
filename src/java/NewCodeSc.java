package com.example.jwt_authentication.Service.surveyjson;

import java.util.*;
import java.util.regex.*;

public class NewCodeSc {
    public static void main(String[] args) {
    	
        // Given map containing question IDs and scores
        Map<String, Double> scoresMap = new HashMap<>();
        scoresMap.put("q1", 34.0);
        scoresMap.put("q2", 45.0);
        scoresMap.put("q3", 56.0);
        scoresMap.put("q4", 89.0);
        scoresMap.put("q5", 90.0);
        scoresMap.put("q7", 75.0);
        scoresMap.put("LOS", 10.0);
        scoresMap.put("ERAdmit", 23.0);
        scoresMap.put("Eps", 1.0);

        // Given formula string
        String formula = "Eps*(5*Sum(q1,q3,q5)-Sum(q1,q3,q5))*25/100+Product(q1,q3,q4)+LOS*ERAdmit";

        // Replace question IDs with respective values in the formula
        String replacedFormula = replaceQuestionIdsWithValues(scoresMap, formula);
        System.out.println("Replaced Formula: " + replacedFormula);

        // Evaluate the modified formula
        double totalScore = evaluateExpression(replacedFormula);
        System.out.println("Total Score: " + totalScore);
    }

    public static String replaceQuestionIdsWithValues(Map<String, Double> scoresMap, String formula) {
        // Pattern to match operations, question IDs, and variables in the formula
        Pattern pattern = Pattern.compile("([a-zA-Z]+)\\((.*?)\\)|([a-zA-Z]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(formula);

        // Replace each operation with its evaluated value
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String operation = matcher.group(1);
            String variable = matcher.group(3);
            String[] tokens = matcher.group(2) != null ? matcher.group(2).split(",") : null;
            double result = 0;

            if (operation != null) {
                switch (operation.toLowerCase()) {
                    case "sum":
                        result = calculateSum(scoresMap, tokens);
                        break;
                    case "avg":
                        result = calculateAverage(scoresMap, tokens);
                        break;
                    case "product":
                        result = calculateProduct(scoresMap, tokens);
                        break;
                    case "mean":
                        result = calculateMean(scoresMap, tokens);
                        break;
                    case "median":
                        result = calculateMedian(scoresMap, tokens);
                        break;
                    case "mode":
                        result = calculateMode(scoresMap, tokens);
                        break;
                }
            } else if (variable != null) {
                // Check if the variable is present in the scoresMap
                if (scoresMap.containsKey(variable)) {
                    result = scoresMap.get(variable);
                } else {
                    throw new IllegalArgumentException("Unknown variable: " + variable);
                }
            }

            matcher.appendReplacement(sb, String.valueOf(result));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


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
  
      // Method to calculate the sum of scores for given question IDs
    private static double calculateSum(Map<String, Double> scoresMap, String[] questionIds) {
        double sum = 0;
        for (String id : questionIds) {
            sum += scoresMap.getOrDefault(id.trim(), 0.0);
        }
        return sum;
    }

    // Method to calculate the average of scores for given question IDs
    private static double calculateAverage(Map<String, Double> scoresMap, String[] questionIds) {
        double sum = calculateSum(scoresMap, questionIds);
        return sum / questionIds.length;
    }

    // Method to calculate the product of scores for given question IDs
    private static double calculateProduct(Map<String, Double> scoresMap, String[] questionIds) {
        double product = 1;
        for (String id : questionIds) {
            product *= scoresMap.getOrDefault(id.trim(), 1.0);
        }
        return product;
    }

    // Method to calculate the mean of scores for given question IDs
    private static double calculateMean(Map<String, Double> scoresMap, String[] questionIds) {
        double sum = calculateSum(scoresMap, questionIds);
        return sum / questionIds.length;
    }

    // Method to calculate the median of scores for given question IDs
    private static double calculateMedian(Map<String, Double> scoresMap, String[] questionIds) {
        // Sort the scores
        List<Double> scores = new ArrayList<>();
        for (String id : questionIds) {
            scores.add(scoresMap.getOrDefault(id.trim(), 0.0));
        }
        Collections.sort(scores);

        // Calculate the median
        int size = scores.size();
        if (size % 2 == 0) {
            return (scores.get(size / 2 - 1) + scores.get(size / 2)) / 2;
        } else {
            return scores.get(size / 2);
        }
    }

    // Method to calculate the mode of scores for given question IDs
    private static double calculateMode(Map<String, Double> scoresMap, String[] questionIds) {
        // Count occurrences of each score
        Map<Double, Integer> countMap = new HashMap<>();
        for (String id : questionIds) {
            double score = scoresMap.getOrDefault(id.trim(), 0.0);
            countMap.put(score, countMap.getOrDefault(score, 0) + 1);
        }

        // Find the score with the maximum occurrence
        double mode = 0.0;  
        int maxCount = 0;
        for (Map.Entry<Double, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                mode = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        return mode;
    }
}
