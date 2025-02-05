/**
 * The CyberCop class represents the main application for managing and visualizing FTC cases.
 * It handles reading case data, managing UI components, searching, modifying, and saving cases.
 *
 * @author Hsiang-Yu Lee / hsiangyl
 */
package HW3;

import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import java.util.stream.Collectors;



public class CyberCop extends Application{

	public static final String DEFAULT_PATH = "data"; //folder name where data files are stored
	public static final String DEFAULT_HTML = "/CyberCop.html"; //local HTML
	public static final String APP_TITLE = "Cyber Cop"; //displayed on top of app

	CCView ccView = new CCView();
	CCModel ccModel = new CCModel();

	GridPane cyberCopRoot;
	Stage stage;

	public static void main(String[] args) {
		launch(args);
	}
	/**
	 * Initializes the application and shows the opening scene.
	 *
	 * @param primaryStage The primary stage for this application.
	 */
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		primaryStage.setTitle("Cyber Cop");
		cyberCopRoot = ccView.setupScreen();
		setupBindings();
		Scene scene = new Scene(cyberCopRoot, ccView.ccWidth, ccView.ccHeight);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
		primaryStage.show();
	}

	/** setupBindings() binds all GUI components to their handlers.
	 * It also binds disableProperty of menu items and text-fields
	 * with ccView.isFileOpen so that they are enabled as needed
	 */
	void setupBindings() {
		// Bind disable properties for various buttons based on whether a file is open or not
		ccView.closeFileMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.addCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.modifyCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.deleteCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.titleTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.caseTypeTextField.disableProperty().bind(ccView.isFileOpen.not());

		// Bind the rest of the actions
		ccView.searchButton.setOnAction(e -> applySearchFilter());
		ccView.saveFileMenuItem.setOnAction(e -> saveFile());
		ccView.openFileMenuItem.setOnAction(e -> openFile());
		ccView.clearButton.setOnAction(e -> clearSearchFields());
		ccView.closeFileMenuItem.setOnAction(e -> closeFile());

		// Attach handlers for Modify and Delete buttons
		ccView.modifyCaseMenuItem.setOnAction(new ModifyButtonHandler());
		ccView.deleteCaseMenuItem.setOnAction(new DeleteButtonHandler());

		ccView.addCaseMenuItem.setOnAction(new AddButtonHandler());
		ccView.caseCountChartMenuItem.setOnAction(e -> showChartViewForCases());

		// Add row selection listener to update WebView when a row is clicked
		ccView.caseTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				// Get the selected case and its link
				Case selectedCase = ccView.caseTableView.getSelectionModel().getSelectedItem();
				String caseLink = selectedCase.getCaseLink();

				// Update WebView based on the case's link
				if (caseLink == null || caseLink.isBlank()) {  // No link in data
					URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  // Default HTML
					if (url != null) {
						ccView.webEngine.load(url.toExternalForm());
					}
				} else if (caseLink.toLowerCase().startsWith("http")) {  // External link
					ccView.webEngine.load(caseLink);
				} else {  // Local link
					URL url = getClass().getClassLoader().getResource(caseLink.trim());
					if (url != null) {
						ccView.webEngine.load(url.toExternalForm());
					}
				}
			}
		});
	}


	private void showChartViewForCases() {
		// Build the yearMap from caseList
		ObservableMap<String, List<Case>> yearMap = buildYearMap();

		// Call showChartView to display the chart
		showChartView(yearMap);
	}

	private ObservableMap<String, List<Case>> buildYearMap() {
		ObservableMap<String, List<Case>> yearMap = FXCollections.observableHashMap();

		// Loop through caseList and categorize cases by year
		for (Case caseObj : ccModel.getCaseList()) {
			if (caseObj.getCaseDate() != null && !caseObj.getCaseDate().isEmpty()) {
				try {
					// Parse the caseDate string to a LocalDate
					LocalDate caseDate = LocalDate.parse(caseObj.getCaseDate());
					String year = String.valueOf(caseDate.getYear());

					// Add the case to the map
					yearMap.computeIfAbsent(year, k -> new ArrayList<>()).add(caseObj);
				} catch (DateTimeParseException e) {
					System.err.println("Invalid date format: " + caseObj.getCaseDate());
				}
			}
		}

		return yearMap;
	}


	void showChartView(ObservableMap<String, List<Case>> yearMap) {
		StackPane stackPane = new StackPane();
		Stage stage = new Stage();
		CCChart ccChart = new CCChart();
		stackPane.getChildren().add(ccChart.updateChart(yearMap));
		Scene scene = new Scene(stackPane, 800, 600);
		stage.setScene(scene);
		stage.setTitle("FTC Case Count");

		// Set the Y-axis bounds
		ccChart.yAxis.setLowerBound(0);

		// Find the max count across all years to set up y-axis in chart
		int maxCount = 0;
		for (List<Case> list : yearMap.values()) {
			if (list.size() > maxCount) maxCount = list.size();
		}
		ccChart.yAxis.setUpperBound(maxCount + 5);

		// Show the chart window
		stage.show();
	}

	private void fillSearchFields(Case selectedCase) {
		ccView.titleTextField.setText(selectedCase.getCaseTitle());
		ccView.caseTypeTextField.setText(selectedCase.getCaseType());

		// Handle the caseDate as a String, extracting the year if the date is not null or empty
		if (selectedCase.getCaseDate() != null && !selectedCase.getCaseDate().isEmpty()) {
			ccView.yearComboBox.setValue(selectedCase.getCaseDate().substring(0, 4));  // Extract the year as the first 4 characters
		} else {
			ccView.yearComboBox.setValue("");  // If the date is null or empty, set an empty value
		}

		ccView.caseNumberTextField.setText(selectedCase.getCaseNumber());
		ccView.caseNotesTextArea.setText(selectedCase.getCaseNotes());
	}



	private void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Case File");

		File defaultDirectory = new File(DEFAULT_PATH);
		if (defaultDirectory.exists()) {
			fileChooser.setInitialDirectory(defaultDirectory);
		}

		// Allow both CSV and TSV files
		FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
		FileChooser.ExtensionFilter tsvFilter = new FileChooser.ExtensionFilter("TSV Files (*.tsv)", "*.tsv");
		FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter("All Files (*.csv, *.tsv)", "*.csv", "*.tsv");

		fileChooser.getExtensionFilters().addAll(allFilter, csvFilter, tsvFilter);

		// Show the file chooser dialog
		File file = fileChooser.showOpenDialog(stage);

		if (file != null) {
			String filePath = file.getAbsolutePath();
			String fileExtension = getFileExtension(file);

			// Choose the appropriate reader based on file extension
			CaseReader reader;
			if ("csv".equalsIgnoreCase(fileExtension)) {
				reader = new CSVCaseReader(filePath);
			} else if ("tsv".equalsIgnoreCase(fileExtension)) {
				reader = new TSVCaseReader(filePath);
			} else {
				showError("Unsupported file type: " + fileExtension);
				return;
			}

			try {
				// Read cases from the file
				List<Case> cases = reader.readCases(); // Get valid cases
				int totalRows = countRows(filePath);   // Total rows in the file
				int rejectedCases = totalRows - cases.size(); // Calculate rejected cases

				// Show an error alert if there are rejected cases
				if (rejectedCases > 0) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Data Error");
					alert.setHeaderText("Error");
					alert.setContentText(rejectedCases + " cases rejected.\nThe file must have cases with tab-separated date, title, type, and case number!");
					alert.showAndWait(); // Wait for user to dismiss the alert
				}

				// Import valid cases after alert
				ccModel.caseList.setAll(cases); // Update caseList with valid cases
				ccModel.originalCaseList.setAll(cases); // Update originalCaseList

				// Populate yearComboBox with all unique years
				List<String> allYears = ccModel.getAllYears();
				ccView.yearComboBox.getItems().setAll(allYears);

				// Update the TableView and other UI elements
				ccView.caseTableView.setItems(ccModel.caseList);

				// Display how many cases were loaded successfully
				ccView.messageLabel.setText(String.format("%d cases loaded.", cases.size()));
				ccView.isFileOpen.set(true);

			} catch (Exception e) {
				showError("Failed to load cases: " + e.getMessage());
			}
		}
	}

	// Utility method to count rows in a file
	private int countRows(String filePath) {
		int rowCount = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			while (br.readLine() != null) {
				rowCount++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rowCount - 1; // Subtract 1 for the header row
	}



	// Utility method to get file extension
	private String getFileExtension(File file) {
		String fileName = file.getName();
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}


	private void populateYearComboBox(List<Case> cases) {
		// Collect unique years from filtered cases
		List<String> uniqueYears = cases.stream()
				.map(caseObj -> {
					// Ensure that the case date is not null or empty, then extract the year (first 4 characters)
					String date = caseObj.getCaseDate();
					return (date != null && date.length() >= 4) ? date.substring(0, 4) : null; // Extract year from the first 4 characters
				})
				.filter(year -> year != null)  // Exclude cases with null or invalid dates
				.distinct()  // Get unique years
				.sorted()  // Sort years in ascending order
				.collect(Collectors.toList());

		// Update the yearComboBox with unique years
		ccView.yearComboBox.getItems().setAll(uniqueYears);
	}




	private void closeFile() {
		// Clear the case data from the model
		ccModel.caseList.clear();
		ccModel.originalCaseList.clear();
		ccModel.caseMap.clear();

		// Clear the TableView
		ccView.caseTableView.getItems().clear();

		// Reset the UI components
		ccView.titleTextField.clear();
		ccView.caseTypeTextField.clear();
		ccView.caseNumberTextField.clear();
		ccView.caseNotesTextArea.clear();
		ccView.messageLabel.setText("Data cleared.");

		// Disable UI components that depend on the file being open
		ccView.isFileOpen.set(false);
	}


	private class ModifyButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// Get the selected case from the table
			Case selectedCase = ccView.caseTableView.getSelectionModel().getSelectedItem();
			if (selectedCase == null) {
				showError("No case selected for modification.");
				return;
			}

			// Create and show ModifyCaseView
			ModifyCaseView modifyCaseView = new ModifyCaseView("Modify Case", selectedCase, () -> {
				// This is the callback that will run after the case is modified
				ccView.caseTableView.refresh();  // Refresh TableView after modifying case
				ccView.messageLabel.setText("Case modified successfully!");  // Update the message label
			}, ccModel);

			// Show the ModifyCaseView stage
			Stage stage = modifyCaseView.buildView();
			stage.show();  // Show the modify case window
		}
	}




	private class DeleteButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// Get the selected case from the table
			Case selectedCase = ccView.caseTableView.getSelectionModel().getSelectedItem();
			if (selectedCase == null) {
				showError("No case selected for deletion.");
				return;
			}

			// Create and show DeleteCaseView
			DeleteCaseView deleteCaseView = new DeleteCaseView("Delete Case", selectedCase, () -> {
				ccModel.caseList.remove(selectedCase); // Remove the case from the model
				ccView.caseTableView.setItems(ccModel.caseList); // Refresh the TableView
				ccView.messageLabel.setText("Case deleted successfully!");
			});

			Stage stage = deleteCaseView.buildView();
			stage.show();  // Show the delete case window
		}
	}


	private void applySearchFilter() {
		String titleKeyword = ccView.titleTextField.getText().trim();
		String caseTypeKeyword = ccView.caseTypeTextField.getText().trim();
		String yearKeyword = ccView.yearComboBox.getValue();
		String caseNumberKeyword = ccView.caseNumberTextField.getText().trim();

		// If all search fields are empty, display the entire dataset
		if (titleKeyword.isEmpty() && caseTypeKeyword.isEmpty() && (yearKeyword == null || yearKeyword.isEmpty()) && caseNumberKeyword.isEmpty()) {
			ccView.caseTableView.setItems(FXCollections.observableArrayList(ccModel.getOriginalCaseList()));
			ccView.messageLabel.setText(String.format("%d cases found", ccModel.getOriginalCaseList().size()));
			populateYearComboBox(ccModel.getOriginalCaseList());
			return;
		}

		// Perform search using the model’s searchCases method
		List<Case> filteredCases = ccModel.searchCases(titleKeyword, caseTypeKeyword, yearKeyword, caseNumberKeyword);

		// Update the TableView with filtered cases
		ccView.caseTableView.setItems(FXCollections.observableArrayList(filteredCases));

		// If there is at least one result, populate the fields with the first match
		if (!filteredCases.isEmpty()) {
			fillSearchFields(filteredCases.get(0)); // Fill with the first matched case
		}

		// Update message label with the number of cases found
		ccView.messageLabel.setText(String.format("%d cases found", filteredCases.size()));

		// Populate the yearComboBox with unique years from the filtered cases
		populateYearComboBox(filteredCases);
	}


	private void saveFile() {
		if (ccModel.caseList.isEmpty()) { // Use ccModel.caseList to check if data is available
			showError("There is no data to save.");
			return; // Prevent saving if there is no data in ccModel.caseList
		}

		// Open a FileChooser to save the file
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Cases as TSV");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TSV Files (*.tsv)", "*.tsv"));

		File file = fileChooser.showSaveDialog(stage); // Show save dialog

		if (file != null) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				// Write the header
				writer.write("Case Date\tTitle\tCase Type\tCase Number\tCategory\tCase Link\tCase Notes");
				writer.newLine();

				// Write each case as a row using data from ccModel.caseList
				for (Case caseObj : ccModel.caseList) {
					String row = String.join("\t",
							caseObj.getCaseDate() != null ? caseObj.getCaseDate() : "",
							caseObj.getCaseTitle(),
							caseObj.getCaseType(),
							caseObj.getCaseNumber(),
							caseObj.getCategory(),
							caseObj.getCaseLink(),
							caseObj.getCaseNotes());
					writer.write(row);
					writer.newLine();
				}

				// Show success message
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("Cases successfully saved to: " + file.getAbsolutePath());
				alert.showAndWait();

			} catch (IOException e) {
				// Handle save errors
				showError("Failed to save cases: " + e.getMessage());
			}
		}
	}


	private void clearSearchFields() {
		ccView.titleTextField.clear();
		ccView.caseTypeTextField.clear();
		ccView.yearComboBox.setValue(null);
		ccView.caseNumberTextField.clear();
		ccView.caseNotesTextArea.clear();

		// Reset the TableView to show all cases
		ccView.caseTableView.setItems(FXCollections.observableArrayList(ccModel.getOriginalCaseList()));

		// Update message label
		ccView.messageLabel.setText(String.format("%d cases loaded", ccModel.getOriginalCaseList().size()));
	}

	class SaveFileMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save Case Data");
			fileChooser.setInitialDirectory(new File(CCModel.DEFAULT_PATH)); // Set initial directory
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TSV Files", "*.tsv"));

			// Show save dialog and get the selected file
			File file = fileChooser.showSaveDialog(stage);
			if (file != null) {
				// Call writeCases() to save the case data to the selected file
				boolean success = ccModel.writeCases(file.getAbsolutePath());

				// If the save is successful, display a success message
				if (success) {
					ccView.messageLabel.setText("File saved: " + file.getName());
				} else {
					ccView.messageLabel.setText("Error saving file.");
				}
			}
		}
	}

	class CaseCountChartMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// Pass ccModel's yearMap to showChartView() to display the chart
			ccView.showChartView(ccModel.yearMap);
		}
	}


	private class AddButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// Define a callback to refresh the TableView after adding a case
			Runnable onCaseAdded = () -> {
				ccView.caseTableView.setItems(FXCollections.observableArrayList(ccModel.caseList));
				ccView.messageLabel.setText("New case added successfully!");
			};

			// Create and show the AddCaseView window
			AddCaseView addCaseView = new AddCaseView("Add New Case", ccModel, onCaseAdded);
			Stage addCaseStage = addCaseView.buildView();
			addCaseStage.show(); // Show the AddCaseView window
		}
	}
}