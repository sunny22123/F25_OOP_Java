//leehsiangyu
//hsiangyl
package hw1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class CCModel {
	Case[] cases;
	String[] fileData;

	/**loadData() takes filename as a parameter,
	 * reads the file and loads all 
	 * data as a String for each row in 
	 * fileData[] array
	 * @param filename
	 */
	void loadData(String filename) {
		int linesCount = 0;
		String line;

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			while ((line=br.readLine()) != null){
				linesCount++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		fileData = new String[linesCount];
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			int index = 0;
			while ((line = br.readLine()) != null) {
				fileData[index++] = line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**loadCases() uses the data stored in fileData array
	 * and creates Case objects for each row.
	 * These cases are loaded into the cases array.
	 * Note that you may have to traverse the fileData array twice
	 * to be able to initialize the cases array's size.
	 */
	void loadCases() {
		// deal with the data is missing
		if (fileData == null || fileData.length == 0) {
			System.out.println("no data");
			return;
		}
		cases = new Case[fileData.length]; // initialize case array

		for (int i = 0; i < fileData.length; i++) {
			String [] file = fileData[i].split("\t");
			if (fileData.length >= 2){
				String date = file[0];
				String title = file[1];
				String caseNumber = (file.length == 3) ? file[2].trim() : null;

				String caseType = null;
				if (title.endsWith("(Federal)")){
					caseType = "Federal";
					title = title.substring(0, title.lastIndexOf('(')).trim();
				} else if (title.endsWith("(Administrative)")){
					caseType = "Administrative";
					title = title.substring(0, title.lastIndexOf('(')).trim();
				}
				cases[i] = new Case(date, title, caseType, caseNumber);
			}
		}
	}


}
