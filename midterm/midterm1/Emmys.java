//hsiangyl

package exam1;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Emmys {

	String[] filedata; //used to store rows of data read from the data file

	String[] series;	//names of series that received awards
	String[] networks;  //names of networks e.g. HBO on which series was aired
	int[][] awardTable; //table that has rows for series and columns for networks. 
						//Each cell [i,j] will show number of awards for i'th series on j'th network
						//i and j are as per their respective indices in series and networks arrays. 	

	//do not change this method
	public static void main(String[] args) {
		Emmys emmys = new Emmys();
		emmys.filedata = emmys.loadData("/Users/leesunny/Desktop/cmu_java/midterm/src/exam1/awards.csv");
		emmys.networks = emmys.getNetworks();
		emmys.series = emmys.getSeries();
		emmys.awardTable = emmys.buildAwardTable();
		emmys.printAwardTable();
	}

	/** loadData() takes file name and reads
	 * each row as an element in an array of Strings.
	 * It returns that array.
	 * @param filename
	 * @return
	 */
	String[] loadData(String filename) {
		String[] data = new String[100];
		int count = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				data[count++] = line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] result = new String[count];
		System.arraycopy(data, 0, result, 0, count);
		return result;
	}

	/** getNetworks returns an array 
	 * of unique network names read from filedata
	 * @return
	 */
	String[] getNetworks() {
		String[] tempNetworks = new String[filedata.length];
		int count = 0;

		for (String row : filedata) {
			String[] columns = row.split(",");
			String network = columns[4].trim();

			boolean exists = false;
			for (int i = 0; i < count; i++) {
				if (tempNetworks[i].equals(network)) {
					exists = true;
					break;
				}
			}

			if (!exists) {
				tempNetworks[count++] = network;
			}
		}

		String[] finalNetworks = new String[count];
		System.arraycopy(tempNetworks, 0, finalNetworks, 0, count);

		// Sort the networks array
		for (int i = 0; i < finalNetworks.length - 1; i++) {
			for (int j = i + 1; j < finalNetworks.length; j++) {
				if (finalNetworks[i].compareTo(finalNetworks[j]) > 0) {
					String temp = finalNetworks[i];
					finalNetworks[i] = finalNetworks[j];
					finalNetworks[j] = temp;
				}
			}
		}

		return finalNetworks;
	}

	
	/** getSeries returns an array of
	 * unique series names read from filedata
	 * @return
	 */
	String[] getSeries() {
		String[] tempSeries = new String[filedata.length];
		int count = 0;

		for (String row : filedata) {
			String[] columns = row.split(",");
			String seriesName = columns[3].trim();

			boolean exists = false;
			for (int i = 0; i < count; i++) {
				if (tempSeries[i].equals(seriesName)) {
					exists = true;
					break;
				}
			}

			if (!exists && !seriesName.isEmpty()) {
				tempSeries[count++] = seriesName;
			}
		}

		String[] finalSeries = new String[count];
		System.arraycopy(tempSeries, 0, finalSeries, 0, count);

		// Sort the series array
		for (int i = 0; i < finalSeries.length - 1; i++) {
			for (int j = i + 1; j < finalSeries.length; j++) {
				if (finalSeries[i].compareTo(finalSeries[j]) > 0) {
					String temp = finalSeries[i];
					finalSeries[i] = finalSeries[j];
					finalSeries[j] = temp;
				}
			}
		}

		return finalSeries;
	}

	/** buildWardTable() builds a matrix of total awards
	 * for a series on a network. The rows represent the series
	 * and columns represent the network.
	 * @return
	 */
	int[][] buildAwardTable() {
		int[][] table = new int[series.length][networks.length];

		for (String row : filedata) {
			String[] columns = row.split(",");
			String seriesName = columns[3].trim();
			String networkName = columns[4].trim();

			int seriesIndex = -1;
			int networkIndex = -1;

			// Find index of the series
			for (int i = 0; i < series.length; i++) {
				if (series[i].equals(seriesName)) {
					seriesIndex = i;
					break;
				}
			}

			// Find index of the network
			for (int i = 0; i < networks.length; i++) {
				if (networks[i].equals(networkName)) {
					networkIndex = i;
					break;
				}
			}

			// Increment the award count
			if (seriesIndex != -1 && networkIndex != -1) {
				table[seriesIndex][networkIndex]++;
			}
		}

		return table;

	}

	//do not change this method
	void printAwardTable() {
		System.out.println("************* EMMY Award Winners 2021 *************");
		System.out.printf("%45s", " ");
		for (String s : networks) System.out.printf("%10s\t", s);  	//print top row of networks
		System.out.println();
		int count = 0;
		for (int i = 0; i < awardTable.length; i++) {  			//for each series
			System.out.printf("%2d. %-45s", ++count, series[i]);				//print series name
			for (int j = 0; j < awardTable[i].length; j++) {	//print no. of awards
				System.out.printf("%s\t\t", (awardTable[i][j] == 0 ? "-": awardTable[i][j]));
			}
			System.out.println();
		}
	}
}
