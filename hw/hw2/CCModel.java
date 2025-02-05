/**
 * The CCModel class represents the model layer in the CyberCop application.
 * It handles all business logic related to case data, including reading from files,
 * processing case information, validating input, and managing collections of cases.
 *
 * @author Hsiang-Yu Lee / hsiangyl
 */
package HW3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CCModel {
	public static final String DEFAULT_PATH = "data";
	ObservableList<Case> caseList = FXCollections.observableArrayList();
	ObservableList<Case> originalCaseList = FXCollections.observableArrayList();
	ObservableMap<String, Case> caseMap = FXCollections.observableHashMap();
	ObservableMap<String, List<Case>> yearMap = FXCollections.observableHashMap();
	ObservableList<String> yearList = FXCollections.observableArrayList();

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	/**
	 * Reads cases from the specified file and parses them into Case objects.
	 * It handles both TSV and CSV formats and skips invalid or incomplete rows.
	 *
	 * @param filePath The path of the file containing case data.
	 * @return A list of Case objects that were successfully read from the file.
	 */
	public void readCases(String filePath) throws IOException {
		System.out.println("Reading file: " + filePath); // Debug print
		caseList.clear();
		originalCaseList.clear();
		caseMap.clear();


		// Determine if the file is a TSV or CSV based on the extension
		boolean isTSV = filePath.toLowerCase().endsWith(".tsv");

		// List to temporarily store cases
		List<Case> tempCases = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			int lineNumber = 0;

			while ((line = reader.readLine()) != null) {
				lineNumber++;
				System.out.println("Processing line " + lineNumber + ": " + line); // Debug print

				// Parse line based on file type (TSV or CSV)
				String[] fields = isTSV ? line.split("\t") : parseCSVLine(line);

				// Ensure fields array has exactly 7 elements by filling missing fields with empty strings
				String dateStr = fields.length > 0 ? cleanField(fields[0]) : "";
				String title = fields.length > 1 ? cleanField(fields[1]) : "";
				String type = fields.length > 2 ? cleanField(fields[2]) : "";
				String caseNumber = fields.length > 3 ? cleanField(fields[3]) : "";
				String link = fields.length > 4 ? cleanField(fields[4]) : "";
				String category = fields.length > 5 ? cleanField(fields[5]) : "";
				String notes = fields.length > 6 ? cleanField(fields[6]) : "";

				// Add the case to the temporary list
				Case caseObj = new Case(dateStr, title, type, caseNumber, link, category, notes);
				tempCases.add(caseObj);

				System.out.println("Created case from line " + lineNumber + ": " + caseObj.getCaseTitle()); // Debug print
			}

			// Sort the cases by date (most recent first)
			tempCases.sort((case1, case2) -> {
				if (case1.getCaseDate() == null || case2.getCaseDate() == null) {
					return 0;  // Handle null values if necessary
				}
				return case2.getCaseDate().compareTo(case1.getCaseDate()); // Reverse order for descending
			});

			// Now add sorted cases to the model lists
			caseList.addAll(tempCases);
			originalCaseList.addAll(tempCases); // Add to original list as well
			for (Case caseObj : tempCases) {
				System.out.println("Imported case: " + caseObj.getCategory() + ", " + caseObj.getCaseNotes()); // Debug log
				caseMap.put(caseObj.getCaseNumber(), caseObj);
			}

			System.out.println("Total cases loaded: " + caseList.size()); // Debug print
		}
	}



	private String cleanField(String field) {
		if (field == null || field.trim().isEmpty()) return "";
		return field.replaceAll("^\"|\"$", "").trim();
	}

	private String[] parseCSVLine(String line) {
		List<String> fields = new ArrayList<>();
		StringBuilder currentField = new StringBuilder();
		boolean inQuotes = false;

		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);

			if (c == '"') {
				inQuotes = !inQuotes;
			} else if (c == ',' && !inQuotes) {
				fields.add(currentField.toString());
				currentField = new StringBuilder();
			} else {
				currentField.append(c);
			}
		}
		fields.add(currentField.toString());

		return fields.toArray(new String[0]);
	}

	public ObservableList<Case> getOriginalCaseList() {
		return originalCaseList;
	}

	public ObservableList<Case> getCaseList() {
		return caseList;
	}
	/**
	 * Retrieves a list of all the unique years from the case list.
	 * This method processes the case list and extracts the year from each case's date.
	 *
	 * @return A list of unique years as strings.
	 */
	public List<String> getAllYears() {
		return originalCaseList.stream()
				.map(caseObj -> caseObj.getCaseDate().length() >= 4 ? caseObj.getCaseDate().substring(0, 4) : null)
				.filter(year -> year != null)
				.distinct()
				.sorted()
				.collect(Collectors.toList());
	}

	void buildYearMapAndList() {
		yearMap.clear();
		yearList.clear();

		for (Case c : caseList) {
			if (c.getCaseDate() != null && c.getCaseDate().length() >= 4) {
				// Extract the year from the caseDate string (first 4 characters)
				String year = c.getCaseDate().substring(0, 4);
				yearMap.computeIfAbsent(year, k -> new ArrayList<>()).add(c);
			}
		}

		yearList.addAll(yearMap.keySet());
		yearList.sort(String::compareTo);  // Sort the years
	}

	public boolean writeCases(String filePath) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			// Write the header
			writer.write("Case Date\tTitle\tCase Type\tCase Number\tLink\tCategory\tNotes");
			writer.newLine();  // Move to the next line

			// Write the case data
			for (Case caseObj : caseList) {
				String caseDate = caseObj.getCaseDate();
				String title = caseObj.getCaseTitle();
				String caseType = caseObj.getCaseType();
				String caseNumber = caseObj.getCaseNumber();
				String link = caseObj.getCaseLink();
				String category = caseObj.getCategory();
				String notes = caseObj.getCaseNotes();

				// Write each case as a TSV line
				writer.write(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s",
						caseDate, title, caseType, caseNumber, link, category, notes));
				writer.newLine();  // Move to the next line
			}

			// Return true if the file is written successfully
			return true;
		} catch (IOException e) {
			e.printStackTrace();  // Log the exception
			return false;  // Return false in case of an error
		}
	}

	public List<Case> searchCases(String title, String caseType, String year, String caseNumber) {
		return originalCaseList.stream()
				.filter(caseObj -> matchesTitle(caseObj, title))
				.filter(caseObj -> matchesCaseType(caseObj, caseType))
				.filter(caseObj -> matchesYear(caseObj, year))
				.filter(caseObj -> matchesCaseNumber(caseObj, caseNumber))
				.collect(Collectors.toList());
	}

	private boolean matchesTitle(Case caseObj, String searchTitle) {
		if (searchTitle == null || searchTitle.isEmpty()) return true;
		return caseObj.getCaseTitle() != null &&
				caseObj.getCaseTitle().toLowerCase().contains(searchTitle.toLowerCase());
	}

	private boolean matchesCaseType(Case caseObj, String searchCaseType) {
		if (searchCaseType == null || searchCaseType.isEmpty()) return true;
		return caseObj.getCaseType() != null &&
				caseObj.getCaseType().equalsIgnoreCase(searchCaseType);
	}

	private boolean matchesYear(Case caseObj, String searchYear) {
		if (searchYear == null || searchYear.isEmpty()) return true;

		String caseYear = null;
		if (caseObj.getCaseDate() != null && caseObj.getCaseDate().length() >= 4) {
			caseYear = caseObj.getCaseDate().substring(0, 4);  // Extract the year from the caseDate string
		}

		return caseYear != null && caseYear.equals(searchYear);
	}

	private boolean matchesCaseNumber(Case caseObj, String searchCaseNumber) {
		if (searchCaseNumber == null || searchCaseNumber.isEmpty()) return true;
		return caseObj.getCaseNumber() != null &&
				caseObj.getCaseNumber().contains(searchCaseNumber);
	}

	// Validates a case and returns an error message if validation fails
	public String validateCase(Case caseObj, Case excludeCase) {
		if (caseObj.getCaseTitle().isEmpty() || caseObj.getCaseDate().isEmpty() ||
				caseObj.getCaseType().isEmpty() || caseObj.getCaseNumber().isEmpty()) {
			return "All fields (title, year, case type, case number) must be filled!";
		}

		// Category and Notes can be empty
		if (isDuplicateCaseNumber(caseObj.getCaseNumber(), excludeCase)) {
			return "The case number already exists. Please enter a unique case number.";
		}

		return null; // No validation errors
	}


	// Check for duplicate case numbers
	public boolean isDuplicateCaseNumber(String caseNumber, Case excludeCase) {
		return caseList.stream()
				.anyMatch(c -> !c.equals(excludeCase) && c.getCaseNumber().equalsIgnoreCase(caseNumber));
	}

	public void sortCasesByDate() {
		// Sort the caseList by caseDate in descending order (latest to oldest)
		caseList.sort((case1, case2) -> {
			if (case1.getCaseDate() == null || case2.getCaseDate() == null) {
				return 0;  // Handle null values if necessary
			}
			return case2.getCaseDate().compareTo(case1.getCaseDate()); // Reverse order
		});
	}

	// Add a case to the list
	public void addCase(Case newCase) {
		caseList.add(newCase);
		originalCaseList.add(newCase);
		caseMap.put(newCase.getCaseNumber(), newCase);

		// Sort the caseList after adding the new case
		sortCasesByDate();
	}

	// Update a case in the list
	public void updateCase(Case currentCase, Case updatedCase) {
		currentCase.setCaseDate(updatedCase.getCaseDate());
		currentCase.setCaseTitle(updatedCase.getCaseTitle());
		currentCase.setCaseType(updatedCase.getCaseType());
		currentCase.setCaseNumber(updatedCase.getCaseNumber());
		currentCase.setCategory(updatedCase.getCategory());
		currentCase.setCaseLink(updatedCase.getCaseLink());
		currentCase.setCaseNotes(updatedCase.getCaseNotes());

		// Update the caseMap and caseList with the updated case
		caseMap.put(updatedCase.getCaseNumber(), updatedCase);
		// If the case is already in the list, it will be updated automatically
		// because we are working with the same reference in caseList
		caseList.set(caseList.indexOf(currentCase), updatedCase);
	}

}
