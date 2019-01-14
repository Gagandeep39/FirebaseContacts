package com.gagandeep.nuvococontacts;

public class User {


    private String userId;
    private String firstName;
    private String lastName;
    private String designation;
    private String location;
    private String email1;
    private String email2;
    private String phoneno_1;
    private String phoneno_2;
    private String phoneno_3;
    private String profileCacheUri;
    private String department;
    private String employeeId;
    private String adminRights;
    private String deskNumber;
    private String emergencyNumber;
    private String sapId;


    private String profileUri;

    public User(String userId
            , String firstName
            , String lastName
            , String designation
            , String location
            , String email1
            , String email2
            , String phoneno_1
            , String phoneno_2
            , String phoneno_3
            , String emergencyNumber
            , String department
            , String profileUri
            , String profileCacheUri
            , String employeeId
            , String adminRights
            , String deskNumber
            , String sapId) {
        this.userId = userId;
        this.firstName = firstName;
        this.designation = designation;
        this.location = location;
        this.email1 = email1;
        this.email2 = email2;
        this.phoneno_1 = phoneno_1;
        this.phoneno_2 = phoneno_2;
        this.phoneno_3 = phoneno_3;
        this.department = department;
        this.profileUri = profileUri;
        this.profileCacheUri = profileCacheUri;
        this.emergencyNumber = emergencyNumber;
        this.employeeId = employeeId;
        this.adminRights = adminRights;
        this.deskNumber = deskNumber;
        this.lastName = lastName;
        this.sapId = sapId;
    }

    User() {

    }

    public String getSapId() {
        return sapId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getAdminRights() {
        return adminRights;
    }

    public String getDeskNumber() {
        return deskNumber;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public String getProfileUri() {
        return profileUri;
    }


    public String getProfileCacheUri() {
        return profileCacheUri;
    }

    public String getDepartment() {
        return department;
    }

    public String getUserId() {
        return userId;
    }


    public String getDesignation() {
        return designation;
    }

    public String getLocation() {
        return location;
    }

    public String getEmail1() {
        return email1;
    }

    public String getEmail2() {
        return email2;
    }

    public String getPhoneno_1() {
        return phoneno_1;
    }

    public String getPhoneno_2() {
        return phoneno_2;
    }

    public String getPhoneno_3() {
        return phoneno_3;
    }
}
