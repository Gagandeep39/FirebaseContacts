package com.gagandeep.nuvococontacts;

public class User {

    public boolean selected = false;
    // 15 String fields defined...
    private String employeeId;
    private String sapId;
    private String name;    // Rename (firstName)
    private String location;
    private String department;
    private String designation;
    private String email1;
    private String email2;
    private String phoneno_1;
    private String phoneno_2;
    private String profileCacheUri;
    private String adminRights;
    private String deskNumber;
    private String emergencyNumber;
    private String profileUri;
    private String division;            //  Added instead of private String userId;

    /**
     * SapID
     * name
     * department
     * location
     * dsignation
     * division
     * employeeid
     * email1
     * email2
     * phone1
     * phone2
     * profilecacheuri
     * adminright
     * desknumber
     * emergencynumber
     * profile uri
     */

    public User(String sapId, String name, String department, String location, String designation, String division, String employeeId, String email1, String email2, String phoneno_1, String phoneno_2, String adminRights, String deskNumber, String emergencyNumber, String profileCacheUri, String profileUri) {
        this.sapId = sapId;
        this.name = name;
        this.department = department;
        this.location = location;
        this.designation = designation;
        this.division = division;
        this.email1 = email1;
        this.email2 = email2;
        this.phoneno_1 = phoneno_1;
        this.phoneno_2 = phoneno_2;
        this.profileCacheUri = profileCacheUri;
        this.adminRights = adminRights;
        this.deskNumber = deskNumber;
        this.emergencyNumber = emergencyNumber;
        this.profileUri = profileUri;
        this.employeeId = employeeId;
    }


    public User() {
    }

public boolean setSelected(boolean selected) {
    this.selected = selected;
    return selected;
}

    public String getEmployeeId() {
        return employeeId;
    }

    public String getSapId() {
        return sapId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getLocation() {
        return location;
    }

    public String getDesignation() {
        return designation;
    }

    public String getDivision() {
        return division;
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

    public String getProfileCacheUri() {
        return profileCacheUri;
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
}
