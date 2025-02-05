/**
 * @author Hsiang-Yu Lee / hsiangyl
 */
package HW3;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DataException extends RuntimeException {
    private final String message;
    private static boolean isAlertDisplayed = false; // Track if the alert is already displayed

    public DataException(String message) {
        super(message);
        this.message = message;
        if (!isAlertDisplayed) { // Show alert only if it hasn't been displayed yet
            isAlertDisplayed = true; // Mark as displayed
            showAlert(message);
            isAlertDisplayed = false; // Reset for future errors after closing the alert
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Data Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait(); // Wait for user interaction before continuing
    }

    @Override
    public String getMessage() {
        return message;
    }
}
