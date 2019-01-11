package com.gagandeep.nuvococontacts;

public class User {


    private String userId;
    private String name;
    private String designation;
    private String location;
    private String email1;
    private String email2;
    private String phoneno_1;
    private String phoneno_2;
    private String phoneno_3;

    private String profileUri;

    public User(String userId, String name, String designation, String location, String email1, String email2, String phoneno_1, String phoneno_2, String phoneno_3, String department, String profileUri) {
        this.userId = userId;
        this.name = name;
        this.designation = designation;
        this.location = location;
        this.email1 = email1;
        this.email2 = email2;
        this.phoneno_1 = phoneno_1;
        this.phoneno_2 = phoneno_2;
        this.phoneno_3 = phoneno_3;
        this.department = department;
        this.profileUri = profileUri;
    }

    private String department;

    public String getProfileUri() {
        return profileUri;
    }

    User() {

    }

    public String getDepartment() {
        return department;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
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
