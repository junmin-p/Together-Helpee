package com.example.junmp.togetherhelpee.domain.user;


public class User {
    private String userPhone;
    private String userNumber;
    private String userId;
    private String name;
    private String gender;
    public String getId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public boolean isEmpty() {
        return userId != null;
    }
}
