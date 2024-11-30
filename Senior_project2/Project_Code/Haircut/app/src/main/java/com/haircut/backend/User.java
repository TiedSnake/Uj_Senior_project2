package com.haircut.backend;

import com.google.firebase.database.PropertyName;

import java.util.EnumSet;

public class User {
    private String uuid;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isLoggedIn;
    private UserType userType;

    public enum UserType {
        CUSTOMER,
        BARBER,
        ADMIN,
        GUEST
    }

    public static final EnumSet<UserType> validUserTypes = EnumSet.allOf(UserType.class);

    //Default constructor to give firebase the ability to serialize the user object
    public User() {
    }

    //Parameterized constructor to give firebase the ability to serialize the user object effectively.
    public User(String firstName, String lastName, String email, UserType... userType) {
//        this.uuid = String.randomString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userType = userType.length>0 ? userType[0] : null;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @PropertyName("isLoggedIn")
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    @PropertyName("isLoggedIn")
    public void setIsLoggedIn(boolean status) {
        this.isLoggedIn = status;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
