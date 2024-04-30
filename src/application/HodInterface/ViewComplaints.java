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

import Database.DatabaseWorkOrderRequestForm;
import Model.WorkOrderRequestFormModel;

public class ViewComplaints {
    private final Connection dbConnection;
    private final ObservableList<WorkOrderRequestFormModel> jobRequests;
    private TableView<WorkOrderRequestFormModel> tableView;

    public ViewComplaints(Connection dbConnection) {
        this.dbConnection = dbConnection;
        this.jobRequests = FXCollections.observableArrayList();
    }

    public void createAndShowView() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("View Complaints");

        Label titleLabel = new Label("Registered Complaints");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");
        titleLabel.setAlignment(Pos.CENTER);

        Label descriptionLabel = new Label("Choose the record to update the job completion details.");
        descriptionLabel.setAlignment(Pos.CENTER);

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

        // Create a button to update completion details
        Button updateButton = new Button("Update Completion Details");
        updateButton.setDisable(true);

        updateButton.setOnAction(event -> {
            WorkOrderRequestFormModel selectedJob = tableView.getSelectionModel().getSelectedItem();
            if (selectedJob != null) {
                String jobOrderString = selectedJob.getJobOrder();
                

                UpdateJobCompletionDetailsForm updateForm = new UpdateJobCompletionDetailsForm(dbConnection, jobOrderString,this);
                Stage updateStage = new Stage();
                updateForm.start(updateStage);
            }
        });

        HBox buttonBox = new HBox(updateButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        VBox vbox = new VBox(titleLabel, descriptionLabel, tableView, buttonBox);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 1030, 550);
        primaryStage.setScene(scene);

        try {
            DatabaseWorkOrderRequestForm dbWorkOrder = new DatabaseWorkOrderRequestForm(dbConnection);
            jobRequests.addAll(dbWorkOrder.getInProgressJobRequests());
            tableView.setItems(jobRequests);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateButton.setDisable(false);
            } else {
                updateButton.setDisable(true);
            }
        });

        primaryStage.show();
    }

    public void refreshData() {
        jobRequests.clear();
        try {
            DatabaseWorkOrderRequestForm dbWorkOrder = new DatabaseWorkOrderRequestForm(dbConnection);
            jobRequests.addAll(dbWorkOrder.getInProgressJobRequests());
            tableView.setItems(jobRequests);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}