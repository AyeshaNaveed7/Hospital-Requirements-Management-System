package application.gsoInterface;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Database.DatabaseWorkOrderRequestForm;
import Model.WorkOrderRequestFormModel;

public class UpdateAllocatedPersonForm {
    private final Connection dbConnection;
    private final WorkOrderRequestFormModel selectedJob;
    private final GSOInprogressJobs parent; // Reference to the parent class

    public UpdateAllocatedPersonForm(Connection dbConnection, WorkOrderRequestFormModel selectedJob, GSOInprogressJobs parent) {
        this.dbConnection = dbConnection;
        this.selectedJob = selectedJob;
        this.parent = parent;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Allocate a Person");

        // Create a label for the heading
        Label titleLabel = new Label("Allocate a Person");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");
        titleLabel.setAlignment(Pos.CENTER);

        // Create a label and a TextField for allocated person
        Label allocatedPersonLabel = new Label("Allocated Person:");
        TextField allocatedPersonField = new TextField();
        allocatedPersonField.setPromptText("Enter the allocated person");

        // Create a button to submit the allocated person
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            String allocatedPerson = allocatedPersonField.getText().trim();
            if (!allocatedPerson.isEmpty()) {
                int jobOrderId = Integer.parseInt(selectedJob.getJobOrder());
                DatabaseWorkOrderRequestForm dbWorkOrder =new DatabaseWorkOrderRequestForm(dbConnection);
                dbWorkOrder.updateAllocatedPerson(jobOrderId, allocatedPerson);
                parent.refreshData(); // Refresh the parent's table view
                primaryStage.close();
            } else {
                // Show an error message if the allocated person is empty
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter the allocated person.");
                alert.showAndWait();
            }
        });

        // Create a VBox to hold the elements
        VBox vbox = new VBox(titleLabel, allocatedPersonLabel, allocatedPersonField, submitButton);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        // Create the scene
        Scene scene = new Scene(vbox, 400, 200);
        primaryStage.setScene(scene);

        // Show the primaryStage
        primaryStage.show();
    }

    
}

