package com.example.haircut.backend;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

public class User {
    private final UUID uuid;
    private String token;
    private String firstName;
    private String lastName;
    private String email;
    private transient String password;
    private boolean isLoggedIn;
    private Date dob;
    private String user_type;

    public User(String firstName, String lastName, String email, String password, String... user_type) {
        this.uuid = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        //Pass the user type optionally upon user object creation.
        if (user_type.length > 0)
            this.user_type = user_type[0];
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

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public UUID getUuid() {
        return uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
