package lab2;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestScoreBoard {
	
	ScoreBoard sb;
	
	@Before
	public void setup() {
		sb = new ScoreBoard();
		sb.readFile("/Users/leesunny/Desktop/cmu_java/labs/out/production/labs/lab2/Scores.txt");
		sb.computeScores();
	}

	@Test
	public void testScoreSumsLength() {
		assertEquals(6, sb.scoreSums.length);
	}
	
	@Test
	public void testScoreAveragesLength() {
		assertEquals(6, sb.scoreAverages.length);
	}
	
	@Test
	public void testScoreSumValues() {
		assertEquals(24, sb.scoreSums[0]);
		assertEquals(9, sb.scoreSums[1]);
		assertEquals(28, sb.scoreSums[2]);
		assertEquals(25, sb.scoreSums[3]);
		assertEquals(22, sb.scoreSums[4]);
		assertEquals(15, sb.scoreSums[5]);
	}

	@Test
	public void testScoreAverageValues() {
		assertEquals(4.8, sb.scoreAverages[0], 0);
		assertEquals(3.0, sb.scoreAverages[1], 0);
		assertEquals(7.0, sb.scoreAverages[2], 0);
		assertEquals(6.25, sb.scoreAverages[3], 0);
		assertEquals(5.5, sb.scoreAverages[4], 0);
		assertEquals(3.75, sb.scoreAverages[5], 0);
	}
	
	@Test
	public void testGrandTotalScore(){
		assertEquals(123, sb.grandTotal);
	}
	
	@Test
	public void testGrandAverage(){
		assertEquals(20.5, sb.grandAverage, 0);
	}
}
