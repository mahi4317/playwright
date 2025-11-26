package com.testdata.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for login user test data
 */
public class LoginUser {
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("expectedUrl")
    private String expectedUrl;
    
    @JsonProperty("expectedError")
    private String expectedError;
    
    // Constructors
    public LoginUser() {
    }
    
    public LoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getExpectedUrl() {
        return expectedUrl;
    }
    
    public void setExpectedUrl(String expectedUrl) {
        this.expectedUrl = expectedUrl;
    }
    
    public String getExpectedError() {
        return expectedError;
    }
    
    public void setExpectedError(String expectedError) {
        this.expectedError = expectedError;
    }
    
    @Override
    public String toString() {
        return "LoginUser{" +
                "username='" + username + '\'' +
                ", expectedUrl='" + expectedUrl + '\'' +
                ", expectedError='" + expectedError + '\'' +
                '}';
    }
}
