package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.WorkOrderRequestFormModel;

public class DatabaseWorkOrderRequestForm {
    private Connection dbconnection;

    public DatabaseWorkOrderRequestForm(Connection connection) {
        this.dbconnection = connection;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try {
            Statement statement = dbconnection.createStatement();
            String checkTableExistsSQL = "SELECT count(*) FROM user_tables WHERE table_name = 'WORK_ORDER_REQUEST_FORM'";
            ResultSet resultSet = statement.executeQuery(checkTableExistsSQL);
            resultSet.next();
            int tableCount = resultSet.getInt(1);
            if (tableCount == 0) {
                String createTableSQL = "CREATE TABLE WORK_ORDER_REQUEST_FORM (" +
                        "job_order_id NUMBER PRIMARY KEY," +
                        "department VARCHAR2(50) NOT NULL," +
                        "hod_name VARCHAR2(100) NOT NULL," +
                        "job_description VARCHAR2(500) NOT NULL," +
                        "date_of_request DATE DEFAULT SYSDATE," +
                        "nature_of_job VARCHAR2(50) NOT NULL," +
                        "estimated_cost NUMBER(10, 2) NOT NULL," +
                        "urgency VARCHAR2(20) CHECK (urgency IN ('Urgent', 'General'))," +
                        "status_of_job VARCHAR2(20) DEFAULT 'In Progress' CHECK (status_of_job IN ('Completed', 'In Progress'))," +
                        "allocated_person VARCHAR2(100)," +
                        "date_of_completion DATE," +
                        "remarks VARCHAR2(500)" +
                        ")";
                statement.execute(createTableSQL);
            } else {
                // Table already exists, no action needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//to insert all data, despite of any permission
    public void insertRecord(WorkOrderRequestFormModel model) {
        String insertSQL = "INSERT INTO WORK_ORDER_REQUEST_FORM " +
            "(job_order_id, department, hod_name, job_description, date_of_request, nature_of_job, estimated_cost, urgency, status_of_job, allocated_person, date_of_completion, remarks) " +
            "VALUES (job_order_id_seq.NEXTVAL, ?, ?, ?, TO_DATE(?, 'DD-MM-YYYY'), ?, ?, ?, ?, ?, TO_DATE(?, 'DD-MM-YYYY'), ?)";

        try (PreparedStatement preparedStatement = dbconnection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, model.getDepartment());
            preparedStatement.setString(2, model.getHeadOfDepartment());
            preparedStatement.setString(3, model.getJobDescription());
            preparedStatement.setString(4, model.getDateOfRequest()); // Assuming date format is 'DD-MM-YYYY'
            preparedStatement.setString(5, model.getNatureOfJob());
            preparedStatement.setString(6, model.getEstCost());
            preparedStatement.setString(7, model.getUrgency());
            preparedStatement.setString(8, model.getStatusOfJob());
            preparedStatement.setString(9, model.getAllocatedPerson());
            preparedStatement.setString(10, model.getDateOfCompletion()); // Assuming date format is 'DD-MM-YYYY'
            preparedStatement.setString(11, model.getRemarks());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 // To fill the record till allocated person
    public void insertRecordTillAllocatedPerson(WorkOrderRequestFormModel model) {
        String insertSQL = "INSERT INTO WORK_ORDER_REQUEST_FORM " +
            "(job_order_id, department, hod_name, job_description, date_of_request, nature_of_job, estimated_cost, urgency, status_of_job) " +
            "VALUES (job_order_id_seq.NEXTVAL, ?, ?, ?, TO_DATE(?, 'DD-MM-YYYY'), ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = dbconnection.prepareStatement(insertSQL, new String[] {"job_order_id"})) {
            preparedStatement.setString(1, model.getDepartment());
            preparedStatement.setString(2, model.getHeadOfDepartment());
            preparedStatement.setString(3, model.getJobDescription());
            preparedStatement.setString(4, model.getDateOfRequest()); // Assuming date format is 'DD-MM-YYYY'
            preparedStatement.setString(5, model.getNatureOfJob());
            preparedStatement.setString(6, model.getEstCost());
            preparedStatement.setString(7, model.getUrgency());
            preparedStatement.setString(8, model.getStatusOfJob());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Insertion failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedJobOrderId = generatedKeys.getInt(1);
                    // You can use 'generatedJobOrderId' as the newly generated job order number
                } else {
                    throw new SQLException("Insertion failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


//use to update allocated person field (used in GSO interface)
    public void updateAllocatedPerson(int jobOrderId, String allocatedPerson) {
        String updateSQL = "UPDATE WORK_ORDER_REQUEST_FORM SET allocated_person = ? WHERE job_order_id = ?";

        try (PreparedStatement preparedStatement = dbconnection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, allocatedPerson);
            preparedStatement.setInt(2, jobOrderId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    //to update the completion details (used in HOD interface)
    public void insertCompletionAndRemarks(String jobOrder, String dateOfCompletion, String remarks) {
        try {
            int jobOrderId = Integer.parseInt(jobOrder);
            String updateSQL = "UPDATE WORK_ORDER_REQUEST_FORM SET date_of_completion = ?, remarks = ?, status_of_job = 'Completed' WHERE job_order_id = ?";

            try (PreparedStatement preparedStatement = dbconnection.prepareStatement(updateSQL)) {
                preparedStatement.setString(1, dateOfCompletion);
                preparedStatement.setString(2, remarks);
                preparedStatement.setInt(3, jobOrderId);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: jobOrder is not a valid integer.");
        }
    }





    
//get next job order (used to pre fill the job order during submission of job request
    public int getNextJobOrder() {
        try {
            String sql = "SELECT MAX(job_order_id) + 1 FROM WORK_ORDER_REQUEST_FORM"; // Replace with your actual table name
            Statement statement = dbconnection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any database exceptions here
        }

        return 1; // Return 1 as the default job order if no records exist
    }
  //this method to extract the jobs whose allocated person is filled and status of job is in progress  
    public List<WorkOrderRequestFormModel> getInProgressJobRequests() throws SQLException {
        List<WorkOrderRequestFormModel> jobRequests = new ArrayList<>();

        String query = "SELECT * FROM WORK_ORDER_REQUEST_FORM WHERE status_of_job = 'In Progress' AND allocated_person IS NOT NULL";

        try (PreparedStatement preparedStatement = dbconnection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                // Retrieve data from the result set and create WorkOrderRequestFormModel objects
                int jobOrder = resultSet.getInt("job_order_id");
                String department = resultSet.getString("department");
                String headOfDepartment = resultSet.getString("hod_name");
                String jobDescription = resultSet.getString("job_description");
                String dateOfRequest = resultSet.getString("date_of_request");
                String natureOfJob = resultSet.getString("nature_of_job");
                String estimatedCost = resultSet.getString("estimated_cost");
                String urgency = resultSet.getString("urgency");
                String statusOfJob = resultSet.getString("status_of_job");
                String allocatedPerson = resultSet.getString("allocated_person");
                String dateOfCompletion = resultSet.getString("date_of_completion");
                String remarks = resultSet.getString("remarks");
                String jobOrderId = Integer.toString(jobOrder);

                // Create a WorkOrderRequestFormModel object
                WorkOrderRequestFormModel jobRequest = new WorkOrderRequestFormModel(jobOrderId,
                        department, headOfDepartment,  jobDescription, dateOfRequest,
                        natureOfJob, estimatedCost, urgency, statusOfJob, allocatedPerson,
                        dateOfCompletion, remarks);

                // Add the job request to the list
                jobRequests.add(jobRequest);
            }
        }

        return jobRequests;
    }

 //to get the completed jobs, of that specific department  (used in HOD interface)
    public List<WorkOrderRequestFormModel> getCompletedJobRequests(String departmentName) throws SQLException {
        List<WorkOrderRequestFormModel> jobRequests = new ArrayList<>();

        String query = "SELECT * FROM WORK_ORDER_REQUEST_FORM WHERE department = ? AND status_of_job = 'Completed'";

        try (PreparedStatement preparedStatement = dbconnection.prepareStatement(query)) {
            preparedStatement.setString(1, departmentName); // Set the department name parameter

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Retrieve data from the result set and create WorkOrderRequestFormModel objects
                    int jobOrder = resultSet.getInt("job_order_id");
                    String department = resultSet.getString("department");
                    String headOfDepartment = resultSet.getString("hod_name");
                    String jobDescription = resultSet.getString("job_description");
                    String dateOfRequest = resultSet.getString("date_of_request");
                    String natureOfJob = resultSet.getString("nature_of_job");
                    String estimatedCost = resultSet.getString("estimated_cost");
                    String urgency = resultSet.getString("urgency");
                    String statusOfJob = resultSet.getString("status_of_job");
                    String allocatedPerson = resultSet.getString("allocated_person");
                    String dateOfCompletion = resultSet.getString("date_of_completion");
                    String remarks = resultSet.getString("remarks");
                    String jobOrderId = Integer.toString(jobOrder);

                    // Create a WorkOrderRequestFormModel object
                    WorkOrderRequestFormModel jobRequest = new WorkOrderRequestFormModel(jobOrderId,
                            department, headOfDepartment, jobDescription, dateOfRequest,
                            natureOfJob, estimatedCost, urgency, statusOfJob, allocatedPerson,
                            dateOfCompletion, remarks);

                    // Add the job request to the list
                    jobRequests.add(jobRequest);
                }
            }
        }

        return jobRequests;
    }
    
  // to get the jobs of a specific department selected by user, and to show jobs whose status is in progress and allocated person is empty (used in GSO's interface)  
    public List<WorkOrderRequestFormModel> getInProgressUnallocatedJobs(String departmentName) throws SQLException {
        List<WorkOrderRequestFormModel> jobRequests = new ArrayList<>();

        String query = "SELECT * FROM WORK_ORDER_REQUEST_FORM WHERE department = ? AND status_of_job = 'In Progress' AND allocated_person IS NULL";

        try (PreparedStatement preparedStatement = dbconnection.prepareStatement(query)) {
            preparedStatement.setString(1, departmentName); // Set the department name parameter

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Retrieve data from the result set and create WorkOrderRequestFormModel objects
                    int jobOrder = resultSet.getInt("job_order_id");
                    String department = resultSet.getString("department");
                    String headOfDepartment = resultSet.getString("hod_name");
                    String jobDescription = resultSet.getString("job_description");
                    String dateOfRequest = resultSet.getString("date_of_request");
                    String natureOfJob = resultSet.getString("nature_of_job");
                    String estimatedCost = resultSet.getString("estimated_cost");
                    String urgency = resultSet.getString("urgency");
                    String statusOfJob = resultSet.getString("status_of_job");
                    String allocatedPerson = resultSet.getString("allocated_person");
                    String dateOfCompletion = resultSet.getString("date_of_completion");
                    String remarks = resultSet.getString("remarks");
                    String jobOrderId = Integer.toString(jobOrder);

                    // Create a WorkOrderRequestFormModel object
                    WorkOrderRequestFormModel jobRequest = new WorkOrderRequestFormModel(jobOrderId,
                            department, headOfDepartment, jobDescription, dateOfRequest,
                            natureOfJob, estimatedCost, urgency, statusOfJob, allocatedPerson,
                            dateOfCompletion, remarks);

                    // Add the job request to the list
                    jobRequests.add(jobRequest);
                }
            }
        }

        return jobRequests;
    }
//to get the completed jobs whose allocated person is filled of a specific department, (used in Gso screen)
    public List<WorkOrderRequestFormModel> getCompletedAllocatedJobs(String departmentName) throws SQLException {
        List<WorkOrderRequestFormModel> jobRequests = new ArrayList<>();

        String query = "SELECT * FROM WORK_ORDER_REQUEST_FORM WHERE department = ? AND status_of_job = 'Completed' AND allocated_person IS NOT NULL";

        try (PreparedStatement preparedStatement = dbconnection.prepareStatement(query)) {
            preparedStatement.setString(1, departmentName); // Set the department name parameter

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Retrieve data from the result set and create WorkOrderRequestFormModel objects
                    int jobOrder = resultSet.getInt("job_order_id");
                    String department = resultSet.getString("department");
                    String headOfDepartment = resultSet.getString("hod_name");
                    String jobDescription = resultSet.getString("job_description");
                    String dateOfRequest = resultSet.getString("date_of_request");
                    String natureOfJob = resultSet.getString("nature_of_job");
                    String estimatedCost = resultSet.getString("estimated_cost");
                    String urgency = resultSet.getString("urgency");
                    String statusOfJob = resultSet.getString("status_of_job");
                    String allocatedPerson = resultSet.getString("allocated_person");
                    String dateOfCompletion = resultSet.getString("date_of_completion");
                    String remarks = resultSet.getString("remarks");
                    String jobOrderId = Integer.toString(jobOrder);

                    // Create a WorkOrderRequestFormModel object
                    WorkOrderRequestFormModel jobRequest = new WorkOrderRequestFormModel(jobOrderId,
                            department, headOfDepartment, jobDescription, dateOfRequest,
                            natureOfJob, estimatedCost, urgency, statusOfJob, allocatedPerson,
                            dateOfCompletion, remarks);

                    // Add the job request to the list
                    jobRequests.add(jobRequest);
                }
            }
        }

        return jobRequests;
    }
//to get all jobs irrespective of anything (to be shown in director's screen
    public List<WorkOrderRequestFormModel> getAllJobs() throws SQLException {
        List<WorkOrderRequestFormModel> jobRequests = new ArrayList<>();

        String query = "SELECT * FROM WORK_ORDER_REQUEST_FORM";

        try (PreparedStatement preparedStatement = dbconnection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                // Retrieve data from the result set and create WorkOrderRequestFormModel objects
                int jobOrder = resultSet.getInt("job_order_id");
                String department = resultSet.getString("department");
                String headOfDepartment = resultSet.getString("hod_name");
                String jobDescription = resultSet.getString("job_description");
                String dateOfRequest = resultSet.getString("date_of_request");
                String natureOfJob = resultSet.getString("nature_of_job");
                String estimatedCost = resultSet.getString("estimated_cost");
                String urgency = resultSet.getString("urgency");
                String statusOfJob = resultSet.getString("status_of_job");
                String allocatedPerson = resultSet.getString("allocated_person");
                String dateOfCompletion = resultSet.getString("date_of_completion");
                String remarks = resultSet.getString("remarks");
                String jobOrderId = Integer.toString(jobOrder);

                // Create a WorkOrderRequestFormModel object
                WorkOrderRequestFormModel jobRequest = new WorkOrderRequestFormModel(jobOrderId,
                        department, headOfDepartment, jobDescription, dateOfRequest,
                        natureOfJob, estimatedCost, urgency, statusOfJob, allocatedPerson,
                        dateOfCompletion, remarks);

                // Add the job request to the list
                jobRequests.add(jobRequest);
            }
        }

        return jobRequests;
    }

}
