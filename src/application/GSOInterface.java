package application;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.Connection;

import application.gsoInterface.JobStatusSelection;

public class GSOInterface extends Application {
    private String selectedDepartment;
    private final Connection dbConnection;
    private final String username;

    public GSOInterface(Connection dbConnection, String username) {
        this.dbConnection = dbConnection;
        this.username = username;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("GSO Interface");

        // Load the hospital logo image
        Image logoImage = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(150); // Set the width of the image
        logoImageView.setPreserveRatio(true);

        // GSO Screen Heading
        Label gsoScreenLabel = new Label("GSO Screen");
        gsoScreenLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gsoScreenLabel.setAlignment(Pos.TOP_CENTER);

        // Text Line
        Label textLabel = new Label("Choose your desired department:");
        textLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        textLabel.setAlignment(Pos.CENTER);

        // Create two VBox containers for department buttons
        VBox leftDepartmentBox = new VBox(10); // Vertical spacing
        VBox rightDepartmentBox = new VBox(10); // Vertical spacing

        // Buttons for 12 departments
        Button[] departmentButtons = new Button[12];
        departmentButtons[0] = new Button("X.Ray");
        departmentButtons[1] = new Button("Emergency");
        departmentButtons[2] = new Button("LAB & Blood Bank");
        departmentButtons[3] = new Button("OPD");
        departmentButtons[4] = new Button("Inpatient");
        departmentButtons[5] = new Button("Bin Qutab college of Health Sciences");
        departmentButtons[6] = new Button("Rehabilitation");
        departmentButtons[7] = new Button("Eye");
        departmentButtons[8] = new Button("Dental");
        departmentButtons[9] = new Button("Dialysis");
        departmentButtons[10] = new Button("Hospital General maintenance");
        departmentButtons[11] = new Button("Surgical");

        // Add department buttons to the VBox containers
        for (int i = 0; i < departmentButtons.length; i++) {
            final int index = i;
            if (i < 6) {
                leftDepartmentBox.getChildren().add(departmentButtons[i]);
            } else {
                rightDepartmentBox.getChildren().add(departmentButtons[i]);
            }
            departmentButtons[i].setOnAction(event -> {
                // Store the selected department when a department button is clicked
                selectedDepartment = departmentButtons[index].getText();
            });
        }

        // Create an HBox to separate the department button columns with a line
        HBox departmentButtonsHBox = new HBox(20, leftDepartmentBox, new javafx.scene.control.Separator(), rightDepartmentBox);
        departmentButtonsHBox.setAlignment(Pos.CENTER);

        // Submit Button
        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white;"); // Blue color
        submitButton.setOnAction(event -> {
            // Store the selected department for future use
            // You can access the selected department using the "selectedDepartment" variable
            // For example, to get the selected department's name, use: selectedDepartment

            // Create and show the next stage where the user can select job status
            JobStatusSelection jobStatusSelection = new JobStatusSelection(dbConnection, selectedDepartment, username);
            jobStatusSelection.createAndShowView();
        });

        // Apply a background color when the "Submit" button is pressed
        submitButton.setOnMousePressed(event -> {
            submitButton.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        });

        // Remove the background color when the mouse is released
        submitButton.setOnMouseReleased(event -> {
            submitButton.setBackground(Background.EMPTY);
        });

        // Create a GridPane for the entire layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Center the logo in the same row
        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setHalignment(HPos.CENTER);
        grid.getColumnConstraints().add(colConstraints);

        // Add elements to the grid
        grid.add(logoImageView, 0, 0, 2, 1);
        grid.add(gsoScreenLabel, 0, 1, 2, 1);
        GridPane.setMargin(gsoScreenLabel, new Insets(10, 0, 10, 0));
        grid.add(textLabel, 0, 2, 2, 1);
        grid.add(departmentButtonsHBox, 0, 3, 2, 1);
        grid.add(submitButton, 0, 5, 2, 1); // Adjusted position

        // Create a scene
        Scene scene = new Scene(grid, 400, 600);

        // Set the scene to the stage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }
}
