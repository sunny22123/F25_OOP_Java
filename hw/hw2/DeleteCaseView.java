/**
 * The DeleteCaseView class represents the view for deleting a selected case.
 * This view confirms with the user before permanently removing the case from the list.
 *
 * @author Hsiang-Yu Lee / hsiangyl
 */
package HW3;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;

public class DeleteCaseView extends CaseView {
    private final Case currentCase;
    private final Runnable onCaseDeleted;

    public DeleteCaseView(String header, Case currentCase, Runnable onCaseDeleted) {
        super(header);
        this.currentCase = currentCase;
        this.onCaseDeleted = onCaseDeleted;
        setupActions();
    }

    private void setupActions() {
        updateButton.setText("Delete Case"); // Set button text to "Delete Case"
        updateButton.setOnAction(e -> deleteCase());
        clearButton.setOnAction(e -> clearFields());
        closeButton.setOnAction(e -> stage.close());
    }

    private void deleteCase() {
        // Run the callback to delete the case from the model and refresh the TableView
        if (onCaseDeleted != null) {
            onCaseDeleted.run();
        }
        stage.close(); // Close the delete window
    }

    private void clearFields() {
        // Populate the fields with the current case's data for confirmation

        // Convert the String date to LocalDate (if necessary)
        String caseDateString = currentCase.getCaseDate();
        LocalDate caseDate = (caseDateString != null && !caseDateString.isEmpty())
                ? LocalDate.parse(caseDateString)
                : null; // Handle empty or null date string

        caseDatePicker.setValue(caseDate);  // Set the LocalDate in caseDatePicker

        titleTextField.setText(currentCase.getCaseTitle());
        caseTypeTextField.setText(currentCase.getCaseType());
        caseNumberTextField.setText(currentCase.getCaseNumber());
        categoryTextField.setText(currentCase.getCategory());
        caseLinkTextField.setText(currentCase.getCaseLink());
        caseNotesTextArea.setText(currentCase.getCaseNotes());

        // Make fields read-only to prevent accidental modification
        titleTextField.setEditable(false);
        caseTypeTextField.setEditable(false);
        caseNumberTextField.setEditable(false);
        categoryTextField.setEditable(false);
        caseLinkTextField.setEditable(false);
        caseNotesTextArea.setEditable(false);
        caseDatePicker.setDisable(true);  // Disable the date picker to prevent modification
    }


    @Override
    public Stage buildView() {
        // Populate fields with the current case's data
        clearFields();

        // Set the scene using the layout from CaseView
        Scene scene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
        stage.setScene(scene); // Set the scene for the stage

        return stage; // Return the stage so it can be shown in CyberCop
    }
}
