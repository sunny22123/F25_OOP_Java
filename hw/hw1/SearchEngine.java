//leehsiangyu
//hsiangyl
package hw1;

public class SearchEngine {
	
	/**searchTitle() takes a searchString and array of cases,
	 * searches for cases with searchString in their title,
	 * and if found, returns them in another array of cases.
	 * If no match is found, it returns null.
	 * Search is case-insensitive
	 * @param searchString
	 * @param cases
	 * @return
	 */
	Case[] searchTitle(String searchString, Case[] cases) {
		searchString = searchString.toLowerCase();
		int matchCount = 0;

		// Count how many matches
		for (Case c : cases) {
			if (c.caseTitle.toLowerCase().contains(searchString)) {
				matchCount++;
			}
		}
		// If no matches, return null
		if (matchCount == 0) {
			return null;
		}
		// Create an array for matching cases
		Case[] result = new Case[matchCount];
		int index = 0;
		for (Case c : cases) {
			if (c.caseTitle.toLowerCase().contains(searchString)) {
				result[index++] = c;
			}
		}
		return result;
	}
	
	/**searchYear() takes year in YYYY format as search string,
	 * searches for cases that have the same year in their date,
	 * and returns them in another array of cases.
	 * If not found, it returns null.
	 * @param year
	 * @param cases
	 * @return
	 */
		Case[] searchYear(String year, Case[] cases) {
			int matchCount = 0;
			int searchYear = Integer.parseInt(year);

			// First, count how many cases match the year
			for (Case c : cases) {
				if (c.getYear() == searchYear) {
					matchCount++;
				}
			}

			// If no matches, return null
			if (matchCount == 0) {
				return null;
			}

			// Create an array to hold matching cases
			Case[] result = new Case[matchCount];
			int index = 0;

			// Populate the result array with matching cases
			for (Case c : cases) {
				if (c.getYear() == searchYear) {
					result[index++] = c;
				}
			}

			return result;
		}


	/**searchCaseNumber() takes a caseNumber,
	 * searches for those cases that contain that caseNumber, 
	 * and returns an array of cases that match the search.
	 * If not found, it returns null.
	 * Search is case-insensitive.
	 * @param caseNumber
	 * @param cases
	 * @return
	 */
	Case[] searchCaseNumber(String caseNumber, Case[] cases) {
		caseNumber = caseNumber.toLowerCase();
		int matchCount = 0;

		// First, count how many cases match the case number
		for (Case c : cases) {
			if (c.caseNumber != null && c.caseNumber.toLowerCase().contains(caseNumber)) {
				matchCount++;
			}
		}

		// If no matches, return null
		if (matchCount == 0) {
			return null;
		}

		// Create an array to hold matching cases
		Case[] result = new Case[matchCount];
		int index = 0;

		// Populate the result array with matching cases
		for (Case c : cases) {
			if (c.caseNumber != null && c.caseNumber.toLowerCase().contains(caseNumber)) {
				result[index++] = c;
			}
		}

		return result;
	}
}
