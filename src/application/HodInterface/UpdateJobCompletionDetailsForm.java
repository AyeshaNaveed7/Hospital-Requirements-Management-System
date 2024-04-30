package application.HodInterface;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import Database.DatabaseWorkOrderRequestForm;
import Model.WorkOrderRequestFormModel;

public class UpdateJobCompletionDetailsForm extends Application {
    private final Connection dbConnection;
    private final String jobOrder;
    private final ViewComplaints viewComplaints;


    public UpdateJobCompletionDetailsForm(Connection dbConnection, String jobOrder, ViewComplaints viewComplaints) {
        this.dbConnection = dbConnection;
        this.jobOrder = jobOrder;
        this.viewComplaints=viewComplaints;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Update Job Completion Details");

        // Load the hospital logo image
        Image logoImage = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(150); // Set the width of the image
        logoImageView.setPreserveRatio(true);

        // Create a GridPane to arrange the elements
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER); // Center align everything
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20)); // Adjusted top padding

        // Add the logo image to the grid and center it horizontally
        GridPane.setHalignment(logoImageView, HPos.CENTER);
        grid.add(logoImageView, 0, 0, 2, 1);

      

        // Update Job Completion Details Heading
        Label updateHeadingLabel = new Label("   Update Job Completion Details");
        updateHeadingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        updateHeadingLabel.setAlignment(Pos.CENTER);

        // Date of Job Completion Field
        Label dateOfCompletionLabel = new Label("Date of Job Completion (dd-mm-yyyy):");
        DatePicker dateOfCompletionPicker = new DatePicker();
        
     // Formatter for the desired date format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");



        // Remarks Field
        Label remarksLabel = new Label("Remarks:");
        TextArea remarksTextArea = new TextArea();
        remarksTextArea.setWrapText(true);
        remarksTextArea.setMaxWidth(250); // Adjusted width

        // Submit Button
        Button submitButton = new Button("Submit");
        HBox submitBox = new HBox(submitButton);
        submitBox.setAlignment(Pos.BOTTOM_LEFT);

        // Error Label for validation
        Label validationErrorLabel = new Label("Please fill in all required fields.");
        validationErrorLabel.setStyle("-fx-text-fill: red;");
        validationErrorLabel.setVisible(false); // Initially hidden

     // Button Actions
        submitButton.setOnAction(event -> {
            LocalDate selectedDate = dateOfCompletionPicker.getValue();

            if (selectedDate != null) {
                String formattedDate = selectedDate.format(dateFormatter);
                String remarks = remarksTextArea.getText();

                if (!remarks.isEmpty()) {
                    // Validation passed, perform update logic here
                    // Update the job completion details with the selected date and remarks

                    // Insert your update logic here, which should interact with the database
                    DatabaseWorkOrderRequestForm dbWorkOrder = new DatabaseWorkOrderRequestForm(dbConnection);
//                    String jobOrderString = selectedModel.getJobOrder();
//                    System.out.println("job order string"+jobOrderString);
//                    int jobOrderId = Integer.parseInt(jobOrderString);
                    
                    dbWorkOrder.insertCompletionAndRemarks(jobOrder, formattedDate, remarks);

                    // After successful update, refresh the TableView with updated data
                   
                    viewComplaints.refreshData(); // Refresh the data

                    // After successful update, you can show a success message
                    showAlert("Success", "Job completion details updated successfully!");

                    // Close the UpdateJobCompletionDetailsForm or navigate to another page
                    primaryStage.close();
                } else {
                    // Validation failed, show an error message
                    validationErrorLabel.setVisible(true);
                }
            } else {
                // No date selected, show an error message
                validationErrorLabel.setVisible(true);
            }
        });

        // Add elements to the grid
        
        grid.add(updateHeadingLabel, 0, 1, 2, 1); // Added the heading label
        GridPane.setMargin(updateHeadingLabel, new Insets(10, 0, 10, 0));
        grid.add(dateOfCompletionLabel, 0, 3);
        grid.add(dateOfCompletionPicker, 1, 3);
        grid.add(remarksLabel, 0, 4);
        grid.add(remarksTextArea, 1, 4);
        grid.add(submitBox, 0, 5);

        // Create a VBox for error messages
        VBox errorMessages = new VBox(validationErrorLabel);
        errorMessages.setAlignment(Pos.CENTER);
        errorMessages.setPadding(new Insets(10, 0, 0, 0));
        grid.add(errorMessages, 0, 6, 2, 1);

        // Create a scene
        Scene scene = new Scene(grid, 430, 400);

        // Set the scene to the stage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
