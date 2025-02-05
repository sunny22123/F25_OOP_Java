//hsiang yu lee, hsiangyl
package lab4;

import java.util.Random;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;

public class EnglishQuiz extends Quiz {

    public String[] wordStrings;   // Stores word:meaning pairs
    private int questionIndex;      // Index of the selected question
    public static int score = 0;
    public static int count = 0;

    public EnglishQuiz(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename));
        wordStrings = lines.toArray(new String[0]);
    }

    @Override
    public void createQuestion() {
        Random random = new Random();
        questionIndex = random.nextInt(wordStrings.length);
        String[] parts = wordStrings[questionIndex].split(":");
        questionString = parts[1].trim();  // Use the meaning for the question
    }

    @Override
    public void createAnswer() {
        String[] parts = wordStrings[questionIndex].split(":");
        answerString = parts[0].trim();  // Use the word as the answer
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        Quiz.count++;  // Increment the shared count
        if (userAnswer.equalsIgnoreCase(answerString)) {
            Quiz.score++;  // Increment the shared score
            EnglishQuiz.score++;  // Increment EnglishQuiz-specific score
            return true;
        }
        return false;
    }
}