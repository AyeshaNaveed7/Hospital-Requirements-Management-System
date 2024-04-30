package application.gsoInterface;

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
import Model.WorkOrderRequestFormModel;

public class GSOInprogressJobs {
    private final Connection dbConnection;
    private final String departmentName;
    private final String username;
    private final ObservableList<WorkOrderRequestFormModel> inProgressJobs;
    private TableView<WorkOrderRequestFormModel> tableView;

    public GSOInprogressJobs(Connection dbConnection, String departmentName, String username) {
        this.dbConnection = dbConnection;
        this.departmentName = departmentName;
        this.username=username;
        this.inProgressJobs = FXCollections.observableArrayList();
    }

    public void createAndShowView() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("In Progress Jobs");

        // Create a label for "In Progress Jobs"
        Label titleLabel = new Label("In Progress Jobs (Fill Allocated Person)");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");
        titleLabel.setAlignment(Pos.CENTER);

        // Create a label for description
        Label descriptionLabel = new Label("Please fill the allocated person for each job.");
       

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

        // Create a button to update allocated person
        Button updateButton = new Button("Update Allocated Person");
        updateButton.setDisable(true); // Initially disabled until a row is selected
        
        

        // Handle the button click event
        updateButton.setOnAction(event -> {
            WorkOrderRequestFormModel selectedJob = tableView.getSelectionModel().getSelectedItem();
            if (selectedJob != null) {
                // Open a new window or dialog for updating the allocated person
               UpdateAllocatedPersonForm updateForm = new UpdateAllocatedPersonForm(dbConnection, selectedJob, this);
                Stage updateStage = new Stage();
                updateForm.start(updateStage);
            }
        });

        // Create an HBox for button alignment
        HBox buttonBox = new HBox(updateButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // Create a VBox to hold the title, description, TableView, and button
        VBox vbox = new VBox(titleLabel, descriptionLabel, tableView, buttonBox);
        vbox.setSpacing(10); // Adjust spacing between elements
        vbox.setAlignment(Pos.CENTER); // Center the VBox content

        // Fetch "In Progress" job requests for the specified department and populate the TableView
        try {
            DatabaseWorkOrderRequestForm dbWorkOrder = new DatabaseWorkOrderRequestForm(dbConnection);
            inProgressJobs.addAll(dbWorkOrder.getInProgressUnallocatedJobs(departmentName));
            tableView.setItems(inProgressJobs);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }

        // Allow selecting a row in the TableView
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateButton.setDisable(false); // Enable the button when a row is selected
            } else {
                updateButton.setDisable(true); // Disable the button when no row is selected
            }
        });

        // Create the scene
        Scene scene = new Scene(vbox, 1030, 550);
        primaryStage.setScene(scene);

        // Show the primaryStage
        primaryStage.show();
    }
    
    public void refreshData() {
        try {
            inProgressJobs.clear(); // Clear existing data
            DatabaseWorkOrderRequestForm dbWorkOrder = new DatabaseWorkOrderRequestForm(dbConnection);
            inProgressJobs.addAll(dbWorkOrder.getInProgressUnallocatedJobs(departmentName)); // Fetch and add new data
            tableView.setItems(inProgressJobs);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }
    
}}

