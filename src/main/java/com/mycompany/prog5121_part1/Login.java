/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121_part1;
/**
 * The Login class handles user registration and login.
 * It checks the username, password and South African cellphone number
 * to make sure that they meet the required format.
 *
 * @author Mthokozisi
 */

public class Login {

    // User information
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;

    // Login information
    private String loginUsername;
    private String loginPassword;

    // Constructor
    public Login(String firstName, String lastName, String username,
                 String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }

    // Checks if the username is correctly formatted
    public Boolean checkUserName() {
        return username != null
                && username.contains("_")
                && username.length() <= 5;
    }

    // Checks if the password meets the requirements
    public Boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapitalLetter = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasCapitalLetter = true;
            } else if (Character.isDigit(character)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasCapitalLetter && hasNumber && hasSpecialCharacter;
    }

    /*
    * Regular expression reference:
    * Oracle (2026). Pattern (Java Platform SE API).
    * Used as a reference for Java regular expression pattern matching.
    */
    // Checks if the South African cellphone number is correctly formatted
    public Boolean checkCellPhoneNumber() {
        if (cellPhoneNumber == null) {
            return false;
        }

        String phoneRegex = "^\\+27[0-9]{9,10}$";
        return cellPhoneNumber.matches(phoneRegex);
    }

    // Registers the user
    public String registerUser() {
        String result = "";

        if (checkUserName()) {
            result += "Username successfully captured.\n";
        } else {
            result += "Username is not correctly formatted; please ensure "
                    + "that your username contains an underscore and is no "
                    + "more than five characters in length.\n";
        }

        if (checkPasswordComplexity()) {
            result += "Password successfully captured.\n";
        } else {
            result += "Password is not correctly formatted; please ensure "
                    + "that the password contains at least eight characters, "
                    + "a capital letter, a number and a special character.\n";
        }

        if (checkCellPhoneNumber()) {
            result += "Cell phone number successfully added.";
        } else {
            result += "Cell phone number incorrectly formatted or does not "
                    + "contain international code.";
        }

        return result;
    }

    // Stores the username and password entered during login
    public void setLoginDetails(String loginUsername, String loginPassword) {
        this.loginUsername = loginUsername;
        this.loginPassword = loginPassword;
    }

    // Checks whether the login details match the registered details
    public Boolean loginUser() {
        return username.equals(loginUsername)
                && password.equals(loginPassword);
    }

    // Returns the login status message
    public String returnLoginStatus() {
        if (loginUser()) {
            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}