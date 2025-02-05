//hsiang yu lee, hsiangyl
package lab4;

import java.util.Scanner;
import java.io.IOException;


public class QuizTime {

	Quiz[] quizzes;
	Scanner input = new Scanner(System.in);

	public QuizTime() {
		try {
			quizzes = new Quiz[]{new MathQuiz(), new EnglishQuiz("/Users/leesunny/Downloads/EnglishQuizWords.txt")};
		} catch (IOException e) {
			System.err.println("Error loading English quiz: " + e.getMessage());
			// Fallback to only MathQuiz if EnglishQuiz fails to load
			quizzes = new Quiz[]{new MathQuiz()};
		}
	}


	public static void main(String[] args) {
		QuizTime quizTime = new QuizTime();
		quizTime.startQuiz();

	}

	//do not change this method
	void startQuiz() {
		int choice = 0;
		while (true) {
			System.out.println("*** Welcome to QuizTime!***");
			System.out.println("1. Math Quiz");
			System.out.println("2. English Quiz");
			System.out.println("3. Exit");
			System.out.println("Enter choice: ");
			choice = input.nextInt();
			input.nextLine();
			if (choice == 1 || choice == 2) {
				runQuiz(choice);
			} else {
				System.out.println("Your Math score: " + MathQuiz.score + "/" + MathQuiz.count);
				System.out.println("Your English score: " + EnglishQuiz.score + "/" + EnglishQuiz.count);
				System.out.println("Your total score: " + Quiz.score + "/" + (MathQuiz.count + EnglishQuiz.count));
				break;

			}
		}
	}

	/**
	 * runQuiz() uses choice to index into quizzes array, and then invokes
	 * createQuestion() and createAnswer() methods.
	 * It then prints the question, takes user input, invokes checkAnswer() to check answer
	 * and then prints whether the answer is Correct or Incorrect.
	 *
	 * @param choice
	 */
	void runQuiz(int choice) {
		Quiz selectedQuiz = quizzes[choice - 1];  // Get the quiz based on the user's choice

		while (true) {
			selectedQuiz.createQuestion();
			selectedQuiz.createAnswer();

			System.out.println("Question: " + selectedQuiz.questionString);
			System.out.print("Enter your answer: ");
			String userAnswer = input.nextLine().trim();

			boolean isCorrect = selectedQuiz.checkAnswer(userAnswer);

			System.out.println(isCorrect ? "Correct!" : "Incorrect. The correct answer was: " + selectedQuiz.answerString);

			System.out.print("Do you want to answer another question? (yes/no): ");
			String continueAnswer = input.nextLine().trim().toLowerCase();
			if (!continueAnswer.equals("yes")) {
				break;
			}
		}
	}
}
