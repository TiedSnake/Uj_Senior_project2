package com.haircut.backend;

import com.google.firebase.database.PropertyName;

import java.util.EnumSet;

public class User {
    public static final EnumSet<UserType> validUserTypes = EnumSet.allOf(UserType.class);
    private String uuid;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isLoggedIn;
    private UserType userType;

    //Default constructor to give firebase the ability to serialize the user object
    public User() {
    }

    //Parameterized constructor to give firebase the ability to serialize the user object effectively.
    public User(String firstName, String lastName, String email, UserType... userType) {
//        this.uuid = String.randomString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userType = userType.length > 0 ? userType[0] : null;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @PropertyName("firstName")
    public String getFirstName() {
        return firstName;
    }

    @PropertyName("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @PropertyName("lastName")
    public String getLastName() {
        return lastName;
    }

    @PropertyName("lastName")
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

    @PropertyName("userType")
    public UserType getUserType() {
        return userType;
    }

    @PropertyName("email")
    public String getEmail() {
        return email;
    }

    @PropertyName("email")
    public void setEmail(String email) {
        this.email = email;
    }

    public enum UserType {
        CUSTOMER,
        BARBER,
        ADMIN,
        GUEST
    }

}
