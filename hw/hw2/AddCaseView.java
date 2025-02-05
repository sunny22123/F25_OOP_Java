/**
 * Adds a new case to the system.
 * Validates the case and checks for duplicates before adding it to the list.
 *
 * @param newCase The Case object to be added.
 * @author Hsiang-Yu Lee / hsiangyl
 */

package HW3;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.time.LocalDate;

public class AddCaseView extends CaseView {
    private final CCModel ccModel;
    private final Runnable onCaseAdded; // Declare onCaseAdded

    public AddCaseView(String header, CCModel ccModel, Runnable onCaseAdded) {
        super(header);
        this.ccModel = ccModel;
        this.onCaseAdded = onCaseAdded; // Initialize onCaseAdded
        setupActions();
    }

    private void setupActions() {
        updateButton.setOnAction(e -> addCase());
        clearButton.setOnAction(e -> clearFields());
        closeButton.setOnAction(e -> stage.close());
    }

    private void addCase() {
        // Collect information from input fields
        LocalDate caseDate = caseDatePicker.getValue();
        String title = titleTextField.getText().trim();
        String caseType = caseTypeTextField.getText().trim();
        String caseNumber = caseNumberTextField.getText().trim();
        String category = categoryTextField.getText().trim();
        String caseLink = caseLinkTextField.getText().trim();
        String caseNotes = caseNotesTextArea.getText().trim();

        // Validate inputs
        if (title.isEmpty() || caseDate == null || caseType.isEmpty() || caseNumber.isEmpty() || category.isEmpty()) {
            showError("All fields (title, year, case type, case number, category) must be filled!");
            return;
        }

        // Check for duplicate case number
        if (ccModel.isDuplicateCaseNumber(caseNumber, null)) {
            showError("The case number already exists. Please enter a unique case number.");
            return;
        }

        // Convert LocalDate to String for caseDate
        String caseDateString = (caseDate != null) ? caseDate.toString() : "";  // Convert to String

        // Create a new Case and add it to the model
        Case newCase = new Case(caseDateString, title, caseType, caseNumber, caseLink, category, caseNotes);
        ccModel.addCase(newCase);  // This method will automatically sort the list

        // Run the callback to refresh the TableView
        if (onCaseAdded != null) {
            onCaseAdded.run();
        }

        stage.close(); // Close the window
    }

    private void clearFields() {
        // Clear all input fields and set default date
        caseDatePicker.setValue(LocalDate.now());
        titleTextField.clear();
        caseTypeTextField.clear();
        caseNumberTextField.clear();
        categoryTextField.clear();
        caseLinkTextField.clear();
        caseNotesTextArea.clear();
    }

    // Add the showError method here
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // Show error and wait for user acknowledgment
    }

    @Override
    public Stage buildView() {
        // Initialize fields for adding a new case
        clearFields();

        // Set the scene using the layout from CaseView
        Scene scene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
        stage.setScene(scene); // Set the scene for the stage

        return stage; // Return the stage so it can be shown in CyberCop
    }
}
