package application.gsoInterface;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Database.DatabaseWorkOrderRequestForm;
import Model.WorkOrderRequestFormModel;

public class GSOCompletedJobsView {

    private final Connection dbConnection;
    private final String departmentName;
    private final String username;
    private final ObservableList<WorkOrderRequestFormModel> completedJobs;
    private TableView<WorkOrderRequestFormModel> tableView;

    public GSOCompletedJobsView(Connection dbConnection, String departmentName, String username) {
        this.dbConnection = dbConnection;
        this.departmentName = departmentName;
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


        // Create a VBox to hold the title and TableView
        VBox vbox = new VBox(titleLabel, tableView);
        vbox.setSpacing(10); // Adjust spacing between elements
        vbox.setAlignment(Pos.CENTER); // Center the VBox content

        // Fetch completed jobs for the specified department and populate the TableView
        try {
            DatabaseWorkOrderRequestForm dbWorkOrder = new DatabaseWorkOrderRequestForm(dbConnection);
            completedJobs.addAll(dbWorkOrder.getCompletedAllocatedJobs(departmentName));
            tableView.setItems(completedJobs);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }

        // Create the scene
        Scene scene = new Scene(vbox, 1030, 550);
        primaryStage.setScene(scene);

        // Show the primaryStage
        primaryStage.show();
    }
}

