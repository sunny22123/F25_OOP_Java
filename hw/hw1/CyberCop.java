//leehsiangyu
//hsiangyl
package hw1;
import java.util.Scanner;

public class CyberCop {

	public static final String DATAFILE = "FTC-Cases-TSV.txt";
	CCModel ccModel = new CCModel();
	SearchEngine searchEngine = new SearchEngine();

	Scanner input = new Scanner(System.in);

	/**main() instantiates CyberCop and then invokes dataManager's loadData
	 * and loadCases() methods
	 * It then invokes showMenu to get user input
	 * @param args
	 */
	//Do not change this method
	public static void main(String[] args) {
		CyberCop cyberCop = new CyberCop();
		cyberCop.ccModel.loadData(DATAFILE);
		cyberCop.ccModel.loadCases();
		cyberCop.showMenu();
	}

	/**showMenu() shows the menu. 
	 * Based on the user choice, it invokes one of the methods:
	 * printSearchResults(), printCaseTypeSummary(), or printYearwiseSummary()
	 * The program exits when user selects Exit option. 
	 * See the hand-out for the expected layout of menu-UI
	 */
	void showMenu() {
		while (true) {
			System.out.println("\n*** Welcome to CyberCop! ***");
			System.out.println("1. Search cases for a company");
			System.out.println("2. Search cases in a year");
			System.out.println("3. Search case number");
			System.out.println("4. Print Case-Type Summary");
			System.out.println("5. Print Year-Wise Summary");
			System.out.println("6. Exit");
			System.out.print("Enter your choice: ");

			String choice = input.nextLine().trim();

			switch (choice) {
				case "1":
					System.out.print("Enter search keyword: ");
					String searchTitle = input.nextLine().trim();
					Case[] titleResults = searchEngine.searchTitle(searchTitle, ccModel.cases);
					printSearchResults(searchTitle, titleResults);
					break;
				case "2":
					String searchYear;
					while (true) {
						System.out.print("Search cases in a year (YYYY): ");
						searchYear = input.nextLine().trim();
						if (!searchYear.isEmpty()) {
							break;
						}
						System.out.println("Year cannot be empty. Please enter a valid year.");
					}
					Case[] yearResults = searchEngine.searchYear(searchYear, ccModel.cases);
					printSearchResults(searchYear, yearResults);
					break;
				case "3":
					System.out.print("Enter case number: ");
					String caseNumber = input.nextLine().trim();
					Case[] caseNumberResults = searchEngine.searchCaseNumber(caseNumber, ccModel.cases);
					printSearchResults(caseNumber, caseNumberResults);
					break;
				case "4":
					printCaseTypeSummary();
					break;
				case "5":
					printYearWiseSummary();
					break;
				case "6":
					System.out.println("Thank you for using CyberCop. Goodbye!");
					return;
				default:
					System.out.println("Invalid choice. Please enter a number between 1 and 6.");
					break;
			}

			System.out.println("\nPress Enter to continue...");
			input.nextLine();
		}
	}

	/**printSearcjResults() takes the searchString and array of cases as input
	 * and prints them out as per the format provided in the handout
	 * @param searchString
	 * @param cases
	 */
	void printSearchResults(String searchString, Case[] cases) {
		if (cases == null || cases.length == 0) {
			System.out.println("No matching cases found for: " + searchString);
			return;
		}

		System.out.println(cases.length + " case(s) found for " + searchString);
		System.out.println("------------------------------------------------------------");
		System.out.printf("%-5s %-10s %-50s %-15s %-15s\n", "#", "Last update", "Case Title", "Case Type", "Case/File Number");
		System.out.println("------------------------------------------------------------");

		for (int i = 0; i < cases.length; i++) {
			Case c = cases[i];
			String truncatedTitle = c.caseTitle.length() > 50 ? c.caseTitle.substring(0, 47) + "..." : c.caseTitle;
			System.out.printf("%-5d %-10s %-50s %-15s %-15s\n",
					i + 1,
					c.caseDate,
					truncatedTitle,
					c.caseType != null ? c.caseType : "",
					c.caseNumber != null ? c.caseNumber : "");
		}
	}

	/**printCaseTypeSummary() prints a summary of
	 * number of cases of different types as per the 
	 * format given in the handout.
	 */
	void printCaseTypeSummary() {
		int administrativeCount = 0;
		int federalCount = 0;
		int unknownCount = 0;

		for (Case c : ccModel.cases) {
			if (c.caseType == null || c.caseType.trim().isEmpty()) {
				unknownCount++;
			} else if (c.caseType.equalsIgnoreCase("Administrative")) {
				administrativeCount++;
			} else if (c.caseType.equalsIgnoreCase("Federal")) {
				federalCount++;
			} else {
				unknownCount++;
			}
		}

		System.out.println("---------------------------------------");
		System.out.println("*** Case Type Summary Report ***");
		System.out.println("No. of Administrative cases: " + administrativeCount);
		System.out.println("No. of Federal cases: " + federalCount);
		System.out.println("No. of unknown case types: " + unknownCount);
		System.out.println("---------------------------------------");
	}

	/**printYearWiseSummary() prints number of cases in each year
	 * as per the format given in the handout
	 */
	void printYearWiseSummary() {
		System.out.println("*** Year-wise Summary Report ***");
		System.out.println("*** Number of FTC cases per year ***");
		int startYear = 1997;
		int endYear = 2021;
		int[] yearCounts = new int[endYear - startYear + 1];

		for (Case c : ccModel.cases) {
			int year = c.getYear();
			if (year >= startYear && year <= endYear) {
				yearCounts[year - startYear]++;
			}
		}

		for (int i = 0; i < yearCounts.length; i++) {
			int year = startYear + i;
			System.out.println(year + ": " + yearCounts[i]);
				}
			}
		}



