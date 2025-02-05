package exam1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestEmmy {
	
	static Emmys emmys;
	
	int[][] testTable = {
			{3,0,0,0,0,0}, 
			{0,2,0,0,0,0}, 
			{0,0,4,0,0,0}, 
			{0,2,0,0,0,0},
			{0,0,0,1,0,0},
			{0,0,0,0,3,0},
			{0,0,0,0,0,1},
			{0,0,1,0,0,0}};
							
	@BeforeAll
	public static void setupEmmys() {
		emmys = new Emmys();
		emmys.filedata = emmys.loadData("awards.csv");
		emmys.series = emmys.getSeries();
		emmys.networks = emmys.getNetworks();
		emmys.awardTable = emmys.buildAwardTable();
	}

	@Test
	void test1_fileDataLength() {
		assertEquals(17, emmys.filedata.length);
	}
	
	@Test
	void test2_seriesLength() {
		assertEquals(8, emmys.series.length);
	}
	
	@Test
	void test3_seriesContent() {
		assertTrue(Arrays.asList(emmys.series).contains("Ted Lasso"));
		assertTrue(Arrays.asList(emmys.series).contains("Mare Of Easttown"));
		assertTrue(Arrays.asList(emmys.series).contains("The Crown"));
		assertTrue(Arrays.asList(emmys.series).contains("Last Week Tonight With John Oliver"));
		assertTrue(Arrays.asList(emmys.series).contains("SATURDAY NIGHT LIVE"));
		assertTrue(Arrays.asList(emmys.series).contains("Hacks"));
		assertTrue(Arrays.asList(emmys.series).contains("RUPAUL'S DRAG RACE"));
		assertTrue(Arrays.asList(emmys.series).contains("The Queen's Gambit"));
	}
	
	@Test
	void test4_networksLength() {
		assertEquals(6, emmys.networks.length);
	}
	
	@Test
	void test5_networksContent() {
		assertTrue(Arrays.asList(emmys.networks).contains("Apple TV+"));
		assertTrue(Arrays.asList(emmys.networks).contains("HBO"));
		assertTrue(Arrays.asList(emmys.networks).contains("NetFlix"));
		assertTrue(Arrays.asList(emmys.networks).contains("NBC"));
		assertTrue(Arrays.asList(emmys.networks).contains("HBO Max"));
		assertTrue(Arrays.asList(emmys.networks).contains("VH1"));
	}
	
	@Test
	void test6_awardsTableRowCount() {
		assertEquals(8, emmys.awardTable.length );
	}
	
	@Test
	void test7_awardsTableColCount() {
		assertEquals(6, emmys.awardTable[0].length);
	}
	
	@Test
	void test8_awardsTableContent() {
		for (int i = 0; i < emmys.awardTable.length; i++) {
			for (int j = 0; j < emmys.awardTable[i].length; j++) {
				assertEquals(testTable[i][j], emmys.awardTable[i][j]);
			}
		}
	}
}
