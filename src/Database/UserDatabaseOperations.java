package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Login.User;
import Login.UserRole;

public class UserDatabaseOperations {
    private final Connection dbConnection;

    public UserDatabaseOperations(Connection dbConnection) {
        this.dbConnection = dbConnection;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try {
            Statement statement = dbConnection.createStatement();
            String checkTableExistsSQL = "SELECT count(*) FROM user_tables WHERE table_name = 'USERS'";
            ResultSet resultSet = statement.executeQuery(checkTableExistsSQL);
            resultSet.next();
            int tableCount = resultSet.getInt(1);
            if (tableCount == 0) {
                String createTableSQL = "CREATE TABLE users (" +
                        "name VARCHAR2(255) NOT NULL," +
                        "password VARCHAR2(255) NOT NULL," +
                        "role VARCHAR2(50) NOT NULL," +
                        "department VARCHAR2(50)" +
                        ")";
                statement.execute(createTableSQL);
                
            } else {
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    
 // Retrieve a user by username
    public User getUserByUsername(String username) {
        User user = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM users WHERE name = ?";
            statement = dbConnection.prepareStatement(sql);
            statement.setString(1, username);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Retrieve user details from the result set
          
                String name = resultSet.getString("name"); // Replace "name" with your actual column name
                String password = resultSet.getString("password"); // Replace "password" with your actual column name
                String role = resultSet.getString("role"); // Replace "role" with your actual column name
                String department = resultSet.getString("department"); // Replace "department" with your actual column name

                // Create a User object with the retrieved details
                user = new User( name, password, UserRole.valueOf(role), department);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any database exceptions here
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return user; // Return the User object (or null if not found)
    }
    
    
    // Update user details
    public boolean updateUser(User user) {
        PreparedStatement statement = null;

        try {
            String sql = "UPDATE users SET name = ?, password = ? WHERE role = ?";
            statement = dbConnection.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name()); // Update based on role

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any database exceptions here
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
// to validate the login of user
    public boolean loginUser(String name, String password) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
            statement = dbConnection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            return resultSet.next(); // Return true if a matching user is found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
   }
 // Define a method to get the user's role by username
    public UserRole getUserRole(String username) {
        // Define the SQL query to retrieve the user's role based on their username
        String sqlQuery = "SELECT role FROM users WHERE name = ?";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    // Convert the role string to the UserRole enum (you might need to handle conversions better)
                    return UserRole.valueOf(role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately in your application
        }

        // Return a default role or handle the case when the user is not found
        return null; // You can define a custom default role in your enum
    }
 // Define a method to get the department by username
    public String getUserDepartment(String username) {
        // Define the SQL query to retrieve the department based on the username
        String sqlQuery = "SELECT department FROM users WHERE name = ?";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("department");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately in your application
        }

        // Return null or a default department value when the user is not found
        return null; // You can define a custom default department value
    }

}

