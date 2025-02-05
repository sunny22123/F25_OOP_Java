//HsiangYuLee/Hsiangyl

package lab2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ScoreBoard {
	StringBuilder fileContent = new StringBuilder();
	int[] scoreSums;
	double[] scoreAverages;
	int grandTotal;
	double grandAverage;
	int playerCount;

	// DO NOT change this method
	/**
	 * initiates the program and runs all other methods in a sequence
	 **/
	public static void main(String[] args) {
		ScoreBoard sb = new ScoreBoard();
		sb.readFile("/Users/leesunny/Desktop/cmu_java/labs/out/production/labs/lab2/Scores.txt");
		sb.computeScores();
		sb.printReport();
	}

	/**
	 * readFile() method reads the file data into fileContent.
	 * It preserves the line-breaks.
	 * @param fileName
	 */
	public void readFile(String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				fileContent.append(line).append("\n");
				playerCount++; // Count the number of players (lines)
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * computeScores() takes the fileContent and calculates four things
	 * 1. For each player, the total score in scoreSums array
	 * 2. For each player, the average score in scoreAverages array
	 * 3. The grand total of all scores in grandTotal
	 * 4. The grand average of all scores in grandAverage
	 */
	public void computeScores() {
		scoreSums = new int[playerCount];
		scoreAverages = new double[playerCount];
		grandTotal = 0;

		String[] lines = fileContent.toString().split("\n");
		for (int i = 0; i < playerCount; i++) {
			String[] scores = lines[i].split(",");
			int sum = 0;
			for (String score : scores) {
				sum += Integer.parseInt(score.trim());
			}
			scoreSums[i] = sum;
			scoreAverages[i] = (double) sum / scores.length;
			grandTotal += sum;
		}

		grandAverage = (double) grandTotal / playerCount;
	}

	/**
	 * printReport() prints the output as shown in the problem statement
	 */
	public void printReport() {
		for (int i = 0; i < playerCount; i++) {
			System.out.printf("Player %d. Total score = %d\tAverage score = %.2f%n",
					i + 1, scoreSums[i], scoreAverages[i]);
		}
		System.out.println("----------------------------------------");
		System.out.printf("Grand total score: %d. Grand average score: %.2f%n",
				grandTotal, grandAverage);
	}
}