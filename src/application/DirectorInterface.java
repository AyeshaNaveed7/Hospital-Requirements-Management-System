package application;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Database.DatabaseWorkOrderRequestForm;
import Model.WorkOrderRequestFormModel;

public class DirectorInterface extends Application {
    private final Connection dbConnection;

    public DirectorInterface(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Director Interface");

        // Load the hospital logo image
        Image logoImage = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(150); // Set the width of the image
        logoImageView.setPreserveRatio(true);

        // Director Screen Heading
        Label directorScreenLabel = new Label("Requirment and Maintenance");
        directorScreenLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        directorScreenLabel.setAlignment(Pos.TOP_CENTER);

        // Create a TableView to display jobs
        TableView<WorkOrderRequestFormModel> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create columns for the TableView
        TableColumn<WorkOrderRequestFormModel, String> jobOrderColumn = new TableColumn<>("Job Order");
        jobOrderColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJobOrder()));

        TableColumn<WorkOrderRequestFormModel, String> departmentColumn = new TableColumn<>("Department");
        departmentColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDepartment()));

        TableColumn<WorkOrderRequestFormModel, String> hodNameColumn = new TableColumn<>("Head of Department");
        hodNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHeadOfDepartment()));

        TableColumn<WorkOrderRequestFormModel, String> jobDescriptionColumn = new TableColumn<>("Job Description");
        jobDescriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJobDescription()));

        TableColumn<WorkOrderRequestFormModel, String> dateOfRequestColumn = new TableColumn<>("Date of Request");
        dateOfRequestColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateOfRequest()));

        TableColumn<WorkOrderRequestFormModel, String> natureOfJobColumn = new TableColumn<>("Nature of Job");
        natureOfJobColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNatureOfJob()));

        TableColumn<WorkOrderRequestFormModel, String> estimatedCostColumn = new TableColumn<>("Estimated Cost");
        estimatedCostColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstCost()));

        TableColumn<WorkOrderRequestFormModel, String> urgencyColumn = new TableColumn<>("Urgency");
        urgencyColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUrgency()));

        TableColumn<WorkOrderRequestFormModel, String> statusOfJobColumn = new TableColumn<>("Status of Job");
        statusOfJobColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatusOfJob()));

        TableColumn<WorkOrderRequestFormModel, String> allocatedPersonColumn = new TableColumn<>("Allocated Person");
        allocatedPersonColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAllocatedPerson()));

        TableColumn<WorkOrderRequestFormModel, String> dateOfCompletionColumn = new TableColumn<>("Date of Completion");
        dateOfCompletionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateOfCompletion()));

        TableColumn<WorkOrderRequestFormModel, String> remarksColumn = new TableColumn<>("Remarks");
        remarksColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRemarks()));

        // Add columns to the TableView
        tableView.getColumns().addAll(
                jobOrderColumn, departmentColumn, hodNameColumn, jobDescriptionColumn,
                dateOfRequestColumn, natureOfJobColumn, estimatedCostColumn, urgencyColumn,
                statusOfJobColumn, allocatedPersonColumn, dateOfCompletionColumn, remarksColumn
        );

     // Create a custom row factory to set row background color based on urgency
        tableView.setRowFactory(tv -> {
            TableRow<WorkOrderRequestFormModel> row = new TableRow<WorkOrderRequestFormModel>() {
                @Override
                protected void updateItem(WorkOrderRequestFormModel item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setStyle(""); // No style for empty cells
                    } else if ("Urgent".equals(item.getUrgency())) {
                        setStyle("-fx-background-color: lightcoral;");
                    } else {
                        setStyle(""); // Reset style for other rows
                    }
                }
            };
            return row;
        });


        // Fetch all jobs and populate the TableView
        try {
            DatabaseWorkOrderRequestForm dbWorkOrder = new DatabaseWorkOrderRequestForm(dbConnection);
            List<WorkOrderRequestFormModel> allJobs = dbWorkOrder.getAllJobs();
            tableView.getItems().addAll(allJobs);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }

        // Create a GridPane for the entire layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER); // Center align everything
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Center the logo in the same row
        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setHalignment(HPos.CENTER);
        grid.getColumnConstraints().add(colConstraints);

        // Add elements to the grid
        grid.add(logoImageView, 0, 0, 2, 1);

        grid.add(directorScreenLabel, 0, 1, 2, 1);
        GridPane.setMargin(directorScreenLabel, new Insets(5, 0, 10, 0));

        // Add the TableView to the grid
        grid.add(tableView, 0, 2, 2, 1);

        // Create a scene
        Scene scene = new Scene(grid, 1100, 600); // Adjust the size as needed

        // Set the scene to the stage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }
}
