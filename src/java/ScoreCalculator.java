package com.example.jwt_authentication.Service.surveyjson;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoreCalculator {
    public static void main(String[] args) {
        // Given map containing question IDs and scores
        Map<Integer, Integer> scoresMap = new HashMap<>();
        scoresMap.put(1, 34);
        scoresMap.put(2, 45);
        scoresMap.put(3, 56);
        scoresMap.put(4, 89);
        scoresMap.put(5, 90);
        scoresMap.put(7, 75);

        // Given formula string
        String formula = "Sum(q1,q3,q5)-avg(q2,q7,q4)*25/100";

        // Replace question IDs with respective values in the formula
        String replacedFormula = replaceQuestionIdsWithValues(scoresMap, formula);
        System.out.println("ReplacedFormula:"+replacedFormula);
        // Calculate the total score using the replaced formula
        int totalScore = evaluateFormula(replacedFormula);
         System.out.println("Total Score: " + totalScore);
    }

    public static String replaceQuestionIdsWithValues(Map<Integer, Integer> scoresMap, String formula) {
        // Pattern to match question IDs in the formula
        Pattern pattern = Pattern.compile("q(\\d+)");
        Matcher matcher = pattern.matcher(formula);

        // Replace each question ID with its respective value from scoresMap
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            int questionId = Integer.parseInt(matcher.group(1));
            int value = scoresMap.getOrDefault(questionId, 0); // Default to 0 if question ID not found
            matcher.appendReplacement(sb, String.valueOf(value));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    
    public static int evaluateFormula(String formula) {
        // Split the formula into tokens (numbers, operators, parentheses)
        String[] tokens = formula.split("(?<=[-+*/()])|(?=[-+*/()])");

        // Stack for operands
        Deque<Integer> operands = new ArrayDeque<>();
        // Stack for operators
        Deque<Character> operators = new ArrayDeque<>();

        for (String token : tokens) {
            token = token.trim();
            if (token.isEmpty()) continue; // Skip empty tokens

            char firstChar = token.charAt(0);
            if (Character.isDigit(firstChar)) {
                // If the token is a number, push it onto the operands stack
                operands.push(Integer.parseInt(token));
            } else if (firstChar == '(') {
                // If the token is an opening parenthesis, push it onto the operators stack
                operators.push(firstChar);
            } else if (firstChar == ')') {
                // If the token is a closing parenthesis, evaluate the expression inside the parentheses
                while (operators.peek() != '(') {
                    evaluate(operands, operators);
                }
                operators.pop(); // Pop the opening parenthesis
            } else {
                // If the token is an operator
                while (!operators.isEmpty() && precedence(firstChar) <= precedence(operators.peek())) {
                    evaluate(operands, operators);
                }
                operators.push(firstChar);
            }
        }

        // Evaluate the remaining operators
        while (!operators.isEmpty()) {
            evaluate(operands, operators);
        }

        // The final result is the top element of the operands stack
        return operands.pop();
    }

    // Helper method to evaluate the expression based on the operator
    private static void evaluate(Deque<Integer> operands, Deque<Character> operators) {
        int b = operands.pop();
        int a = operands.pop();
        char op = operators.pop();
        switch (op) {
            case '+':
                operands.push(a + b);
                break;
            case '-':
                operands.push(a - b);
                break;
            case '*':
                operands.push(a * b);
                break;
            case '/':
                operands.push(a / b);
                break;
        }
    }

    // Helper method to determine the precedence of operators
    private static int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    
}
