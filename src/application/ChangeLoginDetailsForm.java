package application;
import Database.UserDatabaseOperations;
import Login.User;
import Login.UserRole;
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

public class ChangeLoginDetailsForm extends Application {
    private final Connection dbConnection;
    private final String currentUsername; // To store the current user's name

    public ChangeLoginDetailsForm(Connection dbConnection, String currentUsername) {
        this.dbConnection = dbConnection;
        this.currentUsername = currentUsername;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Change Login Details");

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

       

        // System Title Label
        Label systemTitleLabel = new Label("Requirement and Maintenance System");
        systemTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        systemTitleLabel.setAlignment(Pos.CENTER);

        // New Name Field
        Label newNameLabel = new Label("New Name:");
        TextField newNameTextField = new TextField();
        newNameTextField.setText(currentUsername); // Pre-populate with the current username

        // New Password Field
        Label newPasswordLabel = new Label("New Password:");
        PasswordField newPasswordField = new PasswordField();

        // Confirm New Password Field
        Label confirmNewPasswordLabel = new Label("Confirm New Password:");
        PasswordField confirmNewPasswordField = new PasswordField();

        // Submit Button
        Button submitButton = new Button("Submit");
        HBox submitBox = new HBox(submitButton);
        submitBox.setAlignment(Pos.BOTTOM_LEFT);

        // Error Label for Password Mismatch
        Label passwordMismatchLabel = new Label("Passwords do not match.");
        passwordMismatchLabel.setStyle("-fx-text-fill: red;");
        passwordMismatchLabel.setVisible(false); // Initially hidden

        // Button Actions
        submitButton.setOnAction(event -> {
            String newName = newNameTextField.getText();
            String newPassword = newPasswordField.getText();
            String confirmNewPassword = confirmNewPasswordField.getText();

            if (newPassword.equals(confirmNewPassword)) {
                // Passwords match, perform update logic here
                // Update the login details with the new name and password
                UserDatabaseOperations userDbOperations = new UserDatabaseOperations(dbConnection);

                // Retrieve the user based on the current username
                User currentUser = userDbOperations.getUserByUsername(currentUsername);

                if (currentUser != null) {
                    currentUser.setName(newName);
                    currentUser.setPassword(newPassword);

                    boolean updated = userDbOperations.updateUser(currentUser);

                    if (updated) {
                        // Update successful, show a success message or navigate to another page
                        showAlert("Success", "Login details updated successfully!");
                        // Close the ChangeLoginDetailsForm or navigate to another page
                        // User clicked "OK," show the HOD interface
                        UserRole userRole = userDbOperations.getUserRole(newName);

                        if (userRole != null) {
                            // Successfully retrieved user's role

                            // Open the corresponding interface based on the user's role
                            switch (userRole) {
                                case GSO:
                                    openGSOInterface(dbConnection, primaryStage, newName);
                                    break;
                                case DIRECTOR:
                                    openDirectorInterface(dbConnection, primaryStage);
                                    break;
                                // Add cases for other roles as needed
                                default:
                                	 openHODInterface(dbConnection, primaryStage, newName);
                                    break;
                            }
                        }
                    } else {
                        // Update failed, show an error message
                        showAlert("Error", "Failed to update login details. Please try again.");
                    }
                } else {
                    showAlert("Error", "User not found. Please log in again.");
                }
            } else {
                // Passwords do not match, show an error message
                passwordMismatchLabel.setVisible(true);
            }
        });

        

        // Add elements to the grid
       
        grid.add(systemTitleLabel, 0, 1, 2, 1);
        GridPane.setMargin(systemTitleLabel, new Insets(10, 0, 10, 0));
        grid.add(newNameLabel, 0, 3);
        grid.add(newNameTextField, 1, 3);
        grid.add(newPasswordLabel, 0, 4);
        grid.add(newPasswordField, 1, 4);
        grid.add(confirmNewPasswordLabel, 0, 5);
        grid.add(confirmNewPasswordField, 1, 5);
        grid.add(submitBox, 0, 6);

        // Create a VBox for error messages
        VBox errorMessages = new VBox(passwordMismatchLabel);
        errorMessages.setAlignment(Pos.CENTER);
        errorMessages.setPadding(new Insets(10, 0, 0, 0));
        grid.add(errorMessages, 0, 7, 2, 1);

        // Create a scene
        Scene scene = new Scene(grid, 410, 400);

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
    private void openHODInterface(Connection dbConnection, Stage primaryStage, String username) {
        // Create and show the HOD interface
        HODInterface hodInterface = new HODInterface(dbConnection, username);
        hodInterface.start(new Stage());

        // Close the login stage
        primaryStage.close();
    }

    private void openGSOInterface(Connection dbConnection, Stage primaryStage, String username) {
        // Create and show the GSO interface
        GSOInterface gsoInterface = new GSOInterface(dbConnection, username);
        gsoInterface.start(new Stage());

        // Close the login stage
        primaryStage.close();
    }

    private void openDirectorInterface(Connection dbConnection, Stage primaryStage) {
        // Create and show the Director interface
        DirectorInterface directorInterface = new DirectorInterface(dbConnection);
        directorInterface.start(new Stage());

        // Close the login stage
        primaryStage.close();
    }
    
}
