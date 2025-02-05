//hsiang yu lee, hsiangyl
package lab4;

import java.util.Random;

public class MathQuiz extends Quiz {

    private int operand1;
    private int operand2;
    private char operator;

    public static int score = 0;  // Add this static field
    public static int count = 0;  // Add this static field

    @Override
    public void createQuestion() {
        Random random = new Random();
        operand1 = random.nextInt(10);  // Random number between 0 and 9
        operand2 = random.nextInt(10);  // Random number between 0 and 9

        // Randomly select an operator: +, -, or *
        char[] operators = {'+', '-', '*'};
        operator = operators[random.nextInt(3)];

        // Format the question string without spaces to match test expectations
        questionString = operand1 + String.valueOf(operator) + operand2;
    }

    @Override
    public void createAnswer() {
        int result = switch (operator) {
            case '+' -> operand1 + operand2;
            case '-' -> operand1 - operand2;
            case '*' -> operand1 * operand2;
            default -> throw new IllegalArgumentException("Unknown operator");
        };
        answerString = String.valueOf(result);
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        Quiz.count++;  // Increment the shared count
        if (userAnswer.equals(answerString)) {
            Quiz.score++;  // Increment the shared score
            MathQuiz.score++;  // Increment MathQuiz-specific score
            return true;
        }
        return false;
    }
}