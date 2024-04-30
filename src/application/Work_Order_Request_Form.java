package application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Database.DatabaseWorkOrderRequestForm;
import Database.UserDatabaseOperations;
import Model.WorkOrderRequestFormModel;

public class Work_Order_Request_Form {
    private final Connection dbConnection;
    private final String username;

    public Work_Order_Request_Form(Connection dbConnection, String username) {
        this.dbConnection = dbConnection;
        this.username = username;
    }

    public Scene createScene() throws SQLException {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Work Order Request Form");
        
        DatabaseWorkOrderRequestForm dbWorkOrder = new DatabaseWorkOrderRequestForm(dbConnection);
        UserDatabaseOperations userdbOperations = new UserDatabaseOperations (dbConnection);

        // Create a GridPane for the form
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Add a title label
        Label titleLabel = new Label("Work Order Request Form");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18pt;");
        GridPane.setConstraints(titleLabel, 0, 0, 4, 1, HPos.CENTER, VPos.CENTER);
        grid.getChildren().add(titleLabel);

        // Add a separator line
        Separator separator = new Separator();
        GridPane.setConstraints(separator, 0, 1, 4, 1, HPos.CENTER, VPos.CENTER);
        grid.getChildren().add(separator);

        // Add form elements (labels, text fields, buttons)
        int row = 2;

        
        
     // Department
        Label departmentLabel = new Label("Department:");
        GridPane.setConstraints(departmentLabel, 0, row);
        grid.getChildren().add(departmentLabel);

        // Call the method to get the user's department
        String userDepartment = userdbOperations.getUserDepartment(username);

        TextField departmentField = new TextField(userDepartment); // Pre-fill with the user's department
        departmentField.setEditable(false); // Make it read-only
        GridPane.setConstraints(departmentField, 1, row++);
        grid.getChildren().add(departmentField);

        
     // Head of Department
        Label headOfDepartmentLabel = new Label("Head of Department:");
        GridPane.setConstraints(headOfDepartmentLabel, 0, row);
        grid.getChildren().add(headOfDepartmentLabel);

        TextField headOfDepartmentField = new TextField(username); // Pre-fill with the username
        headOfDepartmentField.setEditable(false); // Make it read-only
        GridPane.setConstraints(headOfDepartmentField, 1, row++);
        grid.getChildren().add(headOfDepartmentField);

     // Job Order
        Label jobOrderLabel = new Label("Job Order:");
        GridPane.setConstraints(jobOrderLabel, 0, row);
        grid.getChildren().add(jobOrderLabel);

        // Call the method to get the next job order
        
        int nextJobOrder = dbWorkOrder.getNextJobOrder();

        TextField jobOrderField = new TextField(String.valueOf(nextJobOrder)); // Pre-fill with the next job order
        jobOrderField.setEditable(false); // Make it read-only
        GridPane.setConstraints(jobOrderField, 1, row++);
        grid.getChildren().add(jobOrderField);
        
        
        

        // Job Description
        Label jobDescriptionLabel = new Label("Job Description:");
        GridPane.setConstraints(jobDescriptionLabel, 0, row);
        grid.getChildren().add(jobDescriptionLabel);

        TextArea jobDescriptionArea = new TextArea();
        jobDescriptionArea.setPrefColumnCount(40); // Increased width
        jobDescriptionArea.setPrefRowCount(3); // Reduced height
        GridPane.setConstraints(jobDescriptionArea, 1, row++, 3, 1);
        grid.getChildren().add(jobDescriptionArea);

        
        
     // Date of Request (Pre-fill with current date and time)
        Label dateOfRequestLabel = new Label("Date of Request:");
        GridPane.setConstraints(dateOfRequestLabel, 0, row);
        grid.getChildren().add(dateOfRequestLabel);

        LocalDateTime currentDateTime = LocalDateTime.now(); // Get current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy "); // Define a date-time format
        String currentDateTimeString = currentDateTime.format(formatter); // Format the current date and time
        TextField dateOfRequestField = new TextField(currentDateTimeString); // Pre-fill with current date and time
        dateOfRequestField.setEditable(false); // Make it read-only
        GridPane.setConstraints(dateOfRequestField, 1, row++);
        grid.getChildren().add(dateOfRequestField);

        // Nature of Job
        Label natureOfJobLabel = new Label("Nature of Job:");
        GridPane.setConstraints(natureOfJobLabel, 0, row);
        grid.getChildren().add(natureOfJobLabel);

        TextField natureOfJobField = new TextField();
        GridPane.setConstraints(natureOfJobField, 1, row++);
        grid.getChildren().add(natureOfJobField);

        // Est Cost
        Label estCostLabel = new Label("Est Cost:");
        GridPane.setConstraints(estCostLabel, 0, row);
        grid.getChildren().add(estCostLabel);

        TextField estCostField = new TextField();
        GridPane.setConstraints(estCostField, 1, row++);
        grid.getChildren().add(estCostField);

        // Urgency Toggle Group
        ToggleGroup urgencyGroup = new ToggleGroup();
        Label urgencyLabel = new Label("Urgency:");
        GridPane.setConstraints(urgencyLabel, 0, row);
        grid.getChildren().add(urgencyLabel);

        RadioButton urgentButton = new RadioButton("Urgent");
        urgentButton.setToggleGroup(urgencyGroup);
        urgentButton.setStyle("-fx-text-fill: red;");
        GridPane.setConstraints(urgentButton, 1, row);

        RadioButton generalButton = new RadioButton("General");
        generalButton.setToggleGroup(urgencyGroup);
        generalButton.setStyle("-fx-text-fill: blue;");
        GridPane.setConstraints(generalButton, 2, row++);
        grid.getChildren().addAll(urgentButton, generalButton);
        
        generalButton.setSelected(true);

     // Status of Job Toggle Group
        ToggleGroup statusGroup = new ToggleGroup();
        Label statusOfJobLabel = new Label("Status of Job:");
        GridPane.setConstraints(statusOfJobLabel, 0, row);
        grid.getChildren().add(statusOfJobLabel);

        RadioButton inProgressButton = new RadioButton("In Progress");
        inProgressButton.setToggleGroup(statusGroup);
        inProgressButton.setStyle("-fx-text-fill: green;");
        inProgressButton.setSelected(true); // Set "In Progress" as the default selection
        GridPane.setConstraints(inProgressButton, 1, row);

        RadioButton completedButton = new RadioButton("Completed");
        completedButton.setToggleGroup(statusGroup);
        completedButton.setStyle("-fx-text-fill: green;");
        GridPane.setConstraints(completedButton, 2, row++);
        grid.getChildren().addAll(inProgressButton, completedButton);



        // Allocated Person
        Label allocatedPersonLabel = new Label("Allocated Person:");
        GridPane.setConstraints(allocatedPersonLabel, 0, row);
        grid.getChildren().add(allocatedPersonLabel);

        TextField allocatedPersonField = new TextField();
        GridPane.setConstraints(allocatedPersonField, 1, row++);
        grid.getChildren().add(allocatedPersonField);
        
     // Disable the Allocated Person field for HOD (Head of Department) users
        if (!userdbOperations.getUserRole(username).equals("GSO")) {
            allocatedPersonField.setEditable(false);
        }

        // Separator Line
        Separator separator2 = new Separator();
        GridPane.setConstraints(separator2, 0, row++, 4, 1, HPos.CENTER, VPos.CENTER);
        grid.getChildren().add(separator2);

        // Date of Completion
        Label dateOfCompletionLabel = new Label("Date of Completion:");
        GridPane.setConstraints(dateOfCompletionLabel, 0, row);
        grid.getChildren().add(dateOfCompletionLabel);

        TextField dateOfCompletionField = new TextField();
        GridPane.setConstraints(dateOfCompletionField, 1, row++);
        grid.getChildren().add(dateOfCompletionField);

        // Remarks
        Label remarksLabel = new Label("Remarks:");
        GridPane.setConstraints(remarksLabel, 0, row);
        grid.getChildren().add(remarksLabel);

        TextField remarksField = new TextField();
        GridPane.setConstraints(remarksField, 1, row++);
        grid.getChildren().add(remarksField);

     // Initially, disable the "Date of Completion" and "Remarks" fields
        dateOfCompletionField.setDisable(true); // Add this line
        remarksField.setDisable(true);
        // Submit Button
        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #0073e6; -fx-text-fill: white;");
        GridPane.setConstraints(submitButton, 0, row, 4, 1, HPos.CENTER, VPos.CENTER);
        grid.getChildren().add(submitButton);

        // Add a click event handler to the Submit button
        submitButton.setOnAction(event -> {
            // Retrieve user inputs from the form fields
            String department = departmentField.getText();
            String headOfDepartment = headOfDepartmentField.getText();
            String jobOrder = jobOrderField.getText();
            String jobDescription = jobDescriptionArea.getText();
            String dateOfRequest = dateOfRequestField.getText();
            String natureOfJob = natureOfJobField.getText();
            String estCost = estCostField.getText();
            String urgency = ((RadioButton) urgencyGroup.getSelectedToggle()).getText();
            String allocatedPerson = allocatedPersonField.getText();
            String dateOfCompletion = dateOfCompletionField.getText();
            String remarks = remarksField.getText();

            if (allocatedPerson.isEmpty()) {
                // Allocated Person is not filled, insert data into the database
                String statusOfJob = inProgressButton.isSelected() ? "In Progress" : "Completed";
                
                // Create a WorkOrderRequestFormModel object with the retrieved data
                WorkOrderRequestFormModel model = new WorkOrderRequestFormModel(jobOrder,
                    department, headOfDepartment,  jobDescription, dateOfRequest,
                    natureOfJob, estCost, urgency, statusOfJob
                );

                // TODO: Call your insertRecord method to insert the data into the database
                // Replace 'yourDatabaseOperations' with the actual instance of your DatabaseWorkOrderRequestForm class
                dbWorkOrder.insertRecordTillAllocatedPerson(model);
            } else {
                // Allocated Person is filled, update data in the database
                String statusOfJob = "Completed"; // Status should be set to Completed when Date of Completion is filled

                // TODO: Update the existing record in the database with the new values
                // You'll need to write an updateRecord method in your DatabaseWorkOrderRequestForm class
                // that updates the record based on the job order or another unique identifier

                // Assuming updateRecord method signature:
                int convertedNumber = Integer.parseInt(jobOrder);
                //dbWorkOrder.updateCompletionAndRemarks(convertedNumber, dateOfCompletion, remarks);
            }

            // Show a success alert
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Submission Result");
            successAlert.setHeaderText("Success!");
            successAlert.setContentText("The form has been submitted successfully.");
            successAlert.showAndWait();

            // Close the form or perform any other necessary actions
            primaryStage.close();
        });

        // Add logic to enable/disable fields based on allocated person input
        allocatedPersonField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isAllocatedPersonFilled = !newValue.trim().isEmpty();

            // Enable/disable natureOfJobField, estCostField, and statusGroup based on 'isAllocatedPersonFilled'
            natureOfJobField.setDisable(isAllocatedPersonFilled);
            estCostField.setDisable(isAllocatedPersonFilled);
            statusGroup.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(isAllocatedPersonFilled));

            // Enable/disable inProgressButton and completedButton based on 'isAllocatedPersonFilled'
            inProgressButton.setDisable(isAllocatedPersonFilled);
            completedButton.setDisable(!isAllocatedPersonFilled);

            // Enable/disable dateOfCompletionField and remarksField based on 'isAllocatedPersonFilled'
            dateOfCompletionField.setDisable(!isAllocatedPersonFilled);
            remarksField.setDisable(!isAllocatedPersonFilled);

            // Disable the Job Description field when Allocated Person is filled
            jobDescriptionArea.setDisable(isAllocatedPersonFilled);
        });

        Scene scene = new Scene(grid, 700, 650); // Adjusted width
        primaryStage.setScene(scene);

        return scene;
    }
}