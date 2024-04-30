package application;
import Database.DatabaseConnection;
import Database.UserDatabaseOperations;
import Login.UserRole;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        Connection dbConnection = DatabaseConnection.getConnection();
        UserDatabaseOperations userDbOperations = new UserDatabaseOperations(dbConnection);
        primaryStage.setTitle("Begum Noor Memorial Hospital - Login");

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

        // Name Field
        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();

        // Password Field
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        // Login Button
        Button loginButton = new Button("Log In");
        // Align login button to bottom-right
        HBox loginBox = new HBox(loginButton);
        loginBox.setAlignment(Pos.BOTTOM_RIGHT);

        // Button Actions
        loginButton.setOnAction(event -> {
            String username = nameTextField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                // Empty username or password, show an alert
                showAlert("Error", "Please enter a complete username and password.");
            } else {
                // Attempt to log in using the provided credentials
                boolean loggedIn = userDbOperations.loginUser(username, password);

                if (loggedIn) {
                    // Successfully logged in, show a success alert with "Change Details" button
                    Alert loginSuccessAlert = new Alert(AlertType.CONFIRMATION);
                    loginSuccessAlert.setTitle("Login Successful");
                    loginSuccessAlert.setHeaderText("Welcome, " + username + "!");
                    loginSuccessAlert.setContentText("What would you like to do?");

                    ButtonType changeDetailsButton = new ButtonType("Change Details");
                    ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);

                    loginSuccessAlert.getButtonTypes().setAll(changeDetailsButton, okButton);

                    Optional<ButtonType> result = loginSuccessAlert.showAndWait();

                    if (result.isPresent() && result.get() == changeDetailsButton) {
                        // User clicked "Change Details," open the ChangeLoginDetailsForm
                        ChangeLoginDetailsForm changeDetailsForm = new ChangeLoginDetailsForm(dbConnection, username);
                        changeDetailsForm.start(new Stage());

                        primaryStage.hide(); // Hide the main stage
                    } else if (result.isPresent() && result.get() == okButton) {
                        // User clicked "OK," show the respective interface
                    	 // Attempt to log in using the provided credentials
                        UserRole userRole = userDbOperations.getUserRole(username);

                        if (userRole != null) {
                            // Successfully retrieved user's role

                            // Open the corresponding interface based on the user's role
                            switch (userRole) {
                                case GSO:
                                    openGSOInterface(dbConnection, primaryStage,username);
                                    break;
                                case DIRECTOR:
                                    openDirectorInterface(dbConnection, primaryStage);
                                    break;
                                // Add cases for other roles as needed
                                default:
                                	 openHODInterface(dbConnection, primaryStage, username);
                                    break;
                            }
                        }
                      
                    }
                } else {
                    // Login failed, show an error alert
                    showAlert("Error", "Invalid username or password.");
                }
            }
        });

        // Add elements to the grid
        grid.add(systemTitleLabel, 0, 1, 2, 1);
        GridPane.setMargin(systemTitleLabel, new Insets(10, 0, 10, 0)); // Add margin below the systemTitleLabel
        grid.add(nameLabel, 0, 2);
        grid.add(nameTextField, 1, 2);
        grid.add(passwordLabel, 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(loginBox, 1, 4); // Use the HBox with loginButton

        // Create a scene
        Scene scene = new Scene(grid, 400, 400);

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
