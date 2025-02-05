//HsiangYuLee/Hsiangyl
package lab1;

import java.util.Scanner;

public class Calculator {
	String expression; // stores the arithmetic expression input by the user
	double result = 0; // stores the result

	// do not change this method
	public static void main(String[] args) {
		Calculator c = new Calculator();
		c.getUserInput();
		c.calculate();
		System.out.printf(" = %f", c.result);
	}

	// get user input to initialize 'expression' variable
	public void getUserInput() {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the expression with operands and operators separated by a space: ");
		expression = input.nextLine();
	}

	public void calculate() {
		// deal with blank input
		if (expression.trim().isEmpty()) {
			result = 0;
			return;
		}

		// split every token
		String[] tokens = expression.split(" ");

		// Regex for a number and operator
		String numberRegex = "[+-]?[0-9]+"; // integers only
		String operatorRegex = "[-+/*]";

		try {
			// The first token must be a number
			if (!tokens[0].matches(numberRegex)) {
				System.out.println("Invalid expression: First token must be a number.");
				return;
			}
			result = Double.parseDouble(tokens[0]);

			// Iterate remaining tokens
			for (int i = 1; i < tokens.length; i += 2) {
				String operator = tokens[i]; // Get the operator

				// Check the operator is valid
				if (!operator.matches(operatorRegex)) {
					System.out.println("Invalid expression: Invalid operator.");
					return;
				}

				// Check the next token is a number
				if (!tokens[i + 1].matches(numberRegex)) {
					System.out.println("Invalid expression: Invalid operand.");
					return;
				}

				double nextOperand = Double.parseDouble(tokens[i + 1]); // Get the next operand

				// Perform the operation
				switch (operator) {
					case "+":
						result += nextOperand;
						break;
					case "-":
						result -= nextOperand;
						break;
					case "*":
						result *= nextOperand;
						break;
					case "/":
						if (nextOperand == 0) {
							System.out.println("Invalid expression: Division by zero.");
							return;
						}
						result /= nextOperand;
						break;
					default:
						System.out.println("Invalid expression: Unknown error.");
						return;
				}
			}
		} catch (Exception e) {
			System.out.println("Please enter a valid expression.");
		}
	}
}
