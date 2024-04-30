package Login;

import Login.UserRole;

public class User {
    private String name;
    private String password;
    private UserRole role;
    private String department;

    public User(String name, String password, UserRole role, String department) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.department=department;
    }
    
    public User(String name, String password, UserRole role) {
        this.name = name;
        this.password = password;
        this.role = role;
        
    }

    // Getters and setters for name, password, and role

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

	public String getDepartment() {
		// TODO Auto-generated method stub
		return department;
	}

	public void setDepartment(String department) {
		// TODO Auto-generated method stub
		this.department=department;
	}

    // Additional methods or functionality related to users can be added here
}
