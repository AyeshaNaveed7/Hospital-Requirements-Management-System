package application.HodInterface;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Database.DatabaseWorkOrderRequestForm;
import Database.UserDatabaseOperations;
import Model.WorkOrderRequestFormModel;

public class CompletedJobsView {
    private final Connection dbConnection;
    private final String username;
    private final ObservableList<WorkOrderRequestFormModel> completedJobs;
    private TableView<WorkOrderRequestFormModel> tableView;

    public CompletedJobsView(Connection dbConnection, String username) {
        this.dbConnection = dbConnection;
        this.username = username;
        this.completedJobs = FXCollections.observableArrayList();
    }



    public void createAndShowView() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Completed Jobs");

        // Create a label for "Completed Jobs"
        Label titleLabel = new Label("Completed Jobs");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");
        titleLabel.setAlignment(Pos.CENTER);

        // Create the TableView
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

     // Create columns for the TableView
        TableColumn<WorkOrderRequestFormModel, String> jobOrderColumn = new TableColumn<>("Job Order");
        jobOrderColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJobOrder()));

        //System.out.println("in view method,job id before converting into string: "+ data.getValue());
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
        		jobOrderColumn, departmentColumn,hodNameColumn, jobDescriptionColumn,
                dateOfRequestColumn, natureOfJobColumn, estimatedCostColumn, urgencyColumn,
                statusOfJobColumn, allocatedPersonColumn, dateOfCompletionColumn, remarksColumn
        );

        // Create an HBox for button alignment
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // Create a VBox to hold the title and TableView
        VBox vbox = new VBox(titleLabel, tableView, buttonBox);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        // Fetch completed job requests for the user's department and populate the TableView
        try {
            // Fetch department name based on the username (you need to implement this logic)
            String departmentName = fetchDepartmentName(username);
            DatabaseWorkOrderRequestForm dbWorkOrder = new DatabaseWorkOrderRequestForm(dbConnection);
            completedJobs.addAll(dbWorkOrder.getCompletedJobRequests(departmentName));
            tableView.setItems(completedJobs);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }

        // Create the scene
        Scene scene = new Scene(vbox, 1100, 550);
        primaryStage.setScene(scene);

        // Show the primaryStage
        primaryStage.show();
    }

    // Implement a method to fetch department name based on username
    private String fetchDepartmentName(String username) {
        // Implement logic to retrieve department name from the username
        UserDatabaseOperations userdbOperations = new UserDatabaseOperations (dbConnection);
        String userDepartment = userdbOperations.getUserDepartment(username);
        // For example, you can query the database to get the department name associated with the username
        // Return the department name
        return userDepartment; // Replace with your logic
    }
}

