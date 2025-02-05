/**
 *  This class reads case data from a TSV (Tab-Separated Values) file, parses each line,
 *  and converts it into a list of Case objects. It handles missing values and skips rows
 *  with insufficient data. The class processes each line of the TSV file, extracting the
 *  case information, and returns a list of valid Case objects.
 * @author Hsiang-Yu Lee / hsiangyl
 */
package HW3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSVCaseReader extends CaseReader {

    public TSVCaseReader(String filename) {
        super(filename);
    }

    /**
     * Reads cases from a TSV file, processes them, and returns a list of Case objects.
     * Handles missing values and skips invalid rows.
     *
     * @return List<Case> a list of valid Case objects
     */
    @Override
    public List<Case> readCases() {
        List<Case> cases = new ArrayList<>();
        int rejectedCases = 0; // Count of rejected rows

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;

            // Process each line in the TSV file
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue; // Skip empty lines

                try {
                    String[] fields = line.split("\t");

                    // Ensure we have enough fields and validate critical fields (date, title, type, case number)
                    if (fields.length < 7 || fields[0].trim().isEmpty() || fields[1].trim().isEmpty()
                            || fields[2].trim().isEmpty() || fields[3].trim().isEmpty()) {
                        rejectedCases++;
                        continue; // Skip rows with missing critical fields
                    }

                    // Clean and process fields
                    String dateStr = cleanField(fields[0]);  // Keep date as String
                    String title = cleanField(fields[1]);
                    String type = cleanField(fields[2]);
                    String caseNumber = cleanField(fields[3]);
                    String link = cleanField(fields[4]);
                    String category = fields.length > 5 ? cleanField(fields[5]) : "";
                    String notes = fields.length > 6 ? cleanField(fields[6]) : "";

                    // Debug log to check if category and notes are correctly read
                    System.out.println("Read case: " + title + ", Category: " + category + ", Notes: " + notes);

                    // Instead of parsing the date as LocalDate, we store it directly as a String
                    String caseDate = !dateStr.isEmpty() ? dateStr : "";  // Storing the date as a String

                    // Create a new Case object and add it to the list
                    Case newCase = new Case(caseDate, title, type, caseNumber, link, category, notes);
                    cases.add(newCase);

                } catch (Exception e) {
                    rejectedCases++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage());
        }

        // Log the rejected case count
        if (rejectedCases > 0) {
            System.out.println(rejectedCases + " cases rejected.");
        }

        // Return the list of valid cases (rejected count is logged but not thrown)
        return cases;
    }

    /**
     * Cleans a field by trimming whitespace, removing surrounding quotes, and handling escaped quotes.
     *
     * @param field The field to clean
     * @return The cleaned field
     */
    private String cleanField(String field) {
        if (field == null) return "";

        // Trim whitespace
        field = field.trim();

        // Remove surrounding quotes if present
        if (field.length() >= 2 && field.startsWith("\"") && field.endsWith("\"")) {
            field = field.substring(1, field.length() - 1);
        }

        // Replace escaped quotes with single quotes
        field = field.replace("\"\"", "\"");

        return field;
    }
}
