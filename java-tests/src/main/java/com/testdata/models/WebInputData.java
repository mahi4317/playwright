package com.testdata.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for web input test data
 */
public class WebInputData {
    
    @JsonProperty("testName")
    private String testName;
    
    @JsonProperty("inputText")
    private String inputText;
    
    @JsonProperty("expectedValue")
    private String expectedValue;
    
    @JsonProperty("description")
    private String description;
    
    // Constructors
    public WebInputData() {
    }
    
    public WebInputData(String testName, String inputText, String expectedValue) {
        this.testName = testName;
        this.inputText = inputText;
        this.expectedValue = expectedValue;
    }
    
    // Getters and Setters
    public String getTestName() {
        return testName;
    }
    
    public void setTestName(String testName) {
        this.testName = testName;
    }
    
    public String getInputText() {
        return inputText;
    }
    
    public void setInputText(String inputText) {
        this.inputText = inputText;
    }
    
    public String getExpectedValue() {
        return expectedValue;
    }
    
    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "WebInputData{" +
                "testName='" + testName + '\'' +
                ", inputText='" + inputText + '\'' +
                ", expectedValue='" + expectedValue + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
