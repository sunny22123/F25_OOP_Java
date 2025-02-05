/**
 * The ModifyCaseView class represents the view for modifying an existing case.
 * This class allows the user to edit the details of a selected case and save the modifications.
 *
 * @author Hsiang-Yu Lee / hsiangyl
 */
package HW3;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ModifyCaseView extends CaseView {
    private final Case currentCase; // Current case being modified
    private final Runnable onCaseModified; // Callback to refresh UI
    private final CCModel ccModel; // Reference to model for validation

    public ModifyCaseView(String header, Case currentCase, Runnable onCaseModified, CCModel ccModel) {
        super(header);
        this.currentCase = currentCase;
        this.onCaseModified = onCaseModified;
        this.ccModel = ccModel;

        // Populate fields with the current case's data
        populateFields();

        // Set up button actions
        setupActions();
    }

    private void populateFields() {
        // Pre-fill all fields with the current case's data

        // Convert the caseDate string to a LocalDate if it's not null or empty
        if (currentCase.getCaseDate() != null && !currentCase.getCaseDate().isEmpty()) {
            try {
                LocalDate caseDate = LocalDate.parse(currentCase.getCaseDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                caseDatePicker.setValue(caseDate);  // Set the parsed LocalDate in the date picker
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format: " + currentCase.getCaseDate());
            }
        }

        titleTextField.setText(currentCase.getCaseTitle());
        caseTypeTextField.setText(currentCase.getCaseType());
        caseNumberTextField.setText(currentCase.getCaseNumber());
        categoryTextField.setText(currentCase.getCategory());
        caseLinkTextField.setText(currentCase.getCaseLink());
        caseNotesTextArea.setText(currentCase.getCaseNotes());
    }

    private void setupActions() {
        // Set action for the Update button
        updateButton.setOnAction(e -> modifyCase());

        // Set action for the Clear button (reset fields to current case's data)
        clearButton.setOnAction(e -> populateFields());

        // Set action for the Close button
        closeButton.setOnAction(e -> stage.close());
    }

    private void modifyCase() {
        // Collect updated information from input fields
        LocalDate caseDate = caseDatePicker.getValue();  // Get the LocalDate from the date picker
        String title = titleTextField.getText().trim();
        String caseType = caseTypeTextField.getText().trim();
        String caseNumber = caseNumberTextField.getText().trim();
        String category = categoryTextField.getText().trim();
        String caseLink = caseLinkTextField.getText().trim();
        String caseNotes = caseNotesTextArea.getText().trim();

        // Convert LocalDate to String for caseDate if it's not null
        String caseDateString = (caseDate != null) ? caseDate.toString() : "";  // Convert LocalDate to String

        // Create a new Case object with updated data
        Case updatedCase = new Case(caseDateString, title, caseType, caseNumber, caseLink, category, caseNotes);

        // Validate the updated case (excluding the current case to allow unchanged fields)
        String validationError = ccModel.validateCase(updatedCase, currentCase);
        if (validationError != null) {
            showError(validationError);
            return;
        }

        // Proceed with updating the case in the model
        ccModel.updateCase(currentCase, updatedCase); // Update the case in the model

        // Trigger callback to refresh the table and show the success message
        onCaseModified.run();  // This will execute the refresh and update the message label

        System.out.println("Title: " + title);
        System.out.println("Case Type: " + caseType);
        System.out.println("Case Number: " + caseNumber);
        System.out.println("Category: " + category);
        System.out.println("Notes: " + caseNotes);

        // Close the ModifyCaseView window after successful modification
        stage.close();
    }


    // Method to show error messages
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public Stage buildView() {
        // Populate fields with the current case's data for modification
        populateFields();

        // Set the scene using the layout from CaseView
        Scene scene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
        stage.setScene(scene); // Set the scene for the stage

        return stage; // Return the stage so it can be shown in CyberCop
    }

}
