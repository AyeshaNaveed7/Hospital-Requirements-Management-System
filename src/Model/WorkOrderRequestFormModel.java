package Model;



public class WorkOrderRequestFormModel {
    private String department;
    private String headOfDepartment;
    private String jobOrder;
    private String jobDescription;
    private String dateOfRequest;
    private String natureOfJob;
    private String estCost;
    private String urgency;
    private String statusOfJob;
    private String allocatedPerson;
    private String dateOfCompletion;
    private String remarks;

    // Constructors

    public WorkOrderRequestFormModel(String jobOrder, String department, String headOfDepartment, String jobDescription,
            String dateOfRequest, String natureOfJob, String estCost, String urgency,
            String statusOfJob, String allocatedPerson, String dateOfCompletion, String remarks) {
this.department = department;
this.headOfDepartment = headOfDepartment;
this.jobOrder = jobOrder;
this.jobDescription = jobDescription;
this.dateOfRequest = dateOfRequest;
this.natureOfJob = natureOfJob;
this.estCost = estCost;
this.urgency = urgency;
this.statusOfJob = statusOfJob;
this.allocatedPerson = allocatedPerson;
this.dateOfCompletion = dateOfCompletion;
this.remarks = remarks;
}


    // Constructors with parameters

    // Getters and setters

    public WorkOrderRequestFormModel(String JobOrder, String department, String headOfDepartment, 
			String jobDescription, String dateOfRequest, String natureOfJob, String estCost, String urgency,
			String statusOfJob) {
    	this.department = department;
    	this.headOfDepartment = headOfDepartment;
    	this.jobOrder=jobOrder;
    	this.jobDescription = jobDescription;
    	this.dateOfRequest = dateOfRequest;
    	this.natureOfJob = natureOfJob;
    	this.estCost = estCost;
    	this.urgency = urgency;
    	this.statusOfJob = statusOfJob;
	}


	public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(String headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

    public String getJobOrder() {
        return jobOrder;
    }

    public void setJobOrder(String jobOrder) {
        this.jobOrder = jobOrder;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getDateOfRequest() {
        return dateOfRequest;
    }

    public void setDateOfRequest(String dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    public String getNatureOfJob() {
        return natureOfJob;
    }

    public void setNatureOfJob(String natureOfJob) {
        this.natureOfJob = natureOfJob;
    }

    public String getEstCost() {
        return estCost;
    }

    public void setEstCost(String estCost) {
        this.estCost = estCost;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        urgency = urgency;
    }

    public String getStatusOfJob() {
        return statusOfJob;
    }

    public void setStatusOfJob(String statusOfJob) {
        this.statusOfJob = statusOfJob;
    }

    public String getAllocatedPerson() {
        return allocatedPerson;
    }

    public void setAllocatedPerson(String allocatedPerson) {
        this.allocatedPerson = allocatedPerson;
    }

    public String getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(String dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // Override toString method as mentioned in the previous answer
    public String toString() {
        return "WorkOrderRequest{" +
                "department='" + department + '\'' +
                ", headOfDepartment='" + headOfDepartment + '\'' +
                ", jobOrder='" + jobOrder + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", dateOfRequest='" + dateOfRequest + '\'' +
                ", natureOfJob='" + natureOfJob + '\'' +
                ", estCost='" + estCost + '\'' +
                ", isUrgent=" + urgency +
                ", statusOfJob='" + statusOfJob + '\'' +
                ", allocatedPerson='" + allocatedPerson + '\'' +
                ", dateOfCompletion='" + dateOfCompletion + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}

