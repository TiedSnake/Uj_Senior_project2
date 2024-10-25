package com.example.haircut.backend;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

public class User {
    private UUID uuid;
    private String token;
    private String firstName;
    private String lastName;
    private String email;
    //    private transient String password;
    private boolean isLoggedIn;
    private Date dob;
    private String userType;

    public User(String firstName, String lastName, String email, String... userType) {
//        this.uuid = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
//        this.password = password;
        //Pass the user type optionally upon user object creation.
        if (userType.length > 0)
            this.userType = userType[0];
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getUserType() {
        return userType;
    }

    /*public String getPassword() {
        return password;
    }*/

    /*public void setPassword(String password) {
        this.password = password;
    }*/

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsLoggedIn(boolean status) {
        this.isLoggedIn = status;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }


}
