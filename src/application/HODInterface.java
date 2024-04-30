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
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.Connection;
import java.sql.SQLException;

import Login.User;
import application.HodInterface.CompletedJobsView;
import application.HodInterface.ViewComplaints;

public class HODInterface extends Application {
    private final Connection dbConnection;
    private final String username;

    public HODInterface(Connection dbConnection, String currentUser) {
        this.dbConnection = dbConnection;
        this.username = currentUser;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("HOD Interface");

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

        // HOD Screen Heading
        Label hodScreenLabel = new Label("HOD Screen");
        hodScreenLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        hodScreenLabel.setAlignment(Pos.TOP_CENTER);

        // Text Line
        Label textLabel = new Label("Please select your desired option:");
        textLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        textLabel.setAlignment(Pos.CENTER);

        // Buttons
        Button registerComplaintButton = new Button("Register Complaint");
        registerComplaintButton.setOnAction(event -> {
            // Create and show the Work order request form
            Work_Order_Request_Form workOrderRequestForm = new Work_Order_Request_Form(dbConnection, username);
            Scene workOrderRequestScene;
			try {
				workOrderRequestScene = workOrderRequestForm.createScene();
				// Create a new stage to display the form
	            Stage workOrderRequestStage = new Stage();
	            workOrderRequestStage.setScene(workOrderRequestScene);
	            workOrderRequestStage.show();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            

        });

        Button viewComplaintButton = new Button("View Complaints");
        viewComplaintButton.setOnAction(event -> {
            // Create and show the ViewComplaints stage
            try {
                ViewComplaints viewComplaints = new ViewComplaints(dbConnection); // Pass the database connection
                viewComplaints.createAndShowView(); // Call the method to create and show the ViewComplaints stage
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Button completedJobsButton = new Button("Completed Jobs");
        completedJobsButton.setOnAction(event -> {
            // Create and show the ViewComplaints stage
            try {
                CompletedJobsView completedJobs = new CompletedJobsView(dbConnection, username); // Pass the database connection
                completedJobs.createAndShowView(); // Call the method to create and show the ViewComplaints stage
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Vertical layout for buttons
        VBox buttonLayout = new VBox(20); // Vertical spacing
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(registerComplaintButton, viewComplaintButton, completedJobsButton);

        // Add elements to the grid
        grid.add(hodScreenLabel, 0, 1, 2, 1);
        GridPane.setMargin(hodScreenLabel, new Insets(10, 0, 10, 0));
        grid.add(textLabel, 0, 2, 2, 1);
        grid.add(buttonLayout, 0, 3, 2, 1);

        // Create a scene
        Scene scene = new Scene(grid, 400, 500);

        // Set the scene to the stage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }
}
