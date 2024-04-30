package application.gsoInterface;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.Connection;

public class JobStatusSelection {
    private final Connection dbConnection;
    private final String department;
    private final String username;

    public JobStatusSelection(Connection dbConnection, String department, String username) {
        this.dbConnection = dbConnection;
        this.department = department;
        this.username = username;
    }

    // Method to create and show the job status selection view
    public void createAndShowView() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Job Status Selection");

        // Heading
        Label headingLabel = new Label("Select jobs of this department:");
        headingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        headingLabel.setAlignment(Pos.TOP_CENTER);

        // Buttons
        Button inProgressButton = new Button("In Progress (Fill Allocated Person)");
        Button completedButton = new Button("Completed");

        // Set button actions
        inProgressButton.setOnAction(event -> {
            // Create and show In Progress jobs view
            GSOInprogressJobs inProgressJobs = new GSOInprogressJobs(dbConnection, department, username);
            inProgressJobs.createAndShowView();
        });

        completedButton.setOnAction(event -> {
            // Create and show Completed jobs view
            GSOCompletedJobsView completedJobs = new GSOCompletedJobsView(dbConnection, department, username);
            completedJobs.createAndShowView();
        });

        // Create a VBox to hold buttons
        VBox buttonLayout = new VBox(10); // Vertical spacing
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(inProgressButton, completedButton);

        // Create a GridPane for the entire layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Add elements to the grid
        grid.add(headingLabel, 0, 0, 2, 1);
        GridPane.setMargin(headingLabel, new Insets(10, 0, 10, 0));
        grid.add(buttonLayout, 0, 1, 2, 1);

        // Create a scene
        Scene scene = new Scene(grid, 400, 200);

     // Set the scene to the stage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }
}

