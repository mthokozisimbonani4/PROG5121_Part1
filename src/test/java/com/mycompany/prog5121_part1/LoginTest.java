/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.prog5121_part1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for the Login class.
 * These tests check valid and invalid usernames, passwords,
 * cellphone numbers and login details.
 *
 * @author Mthokozisi
 */

public class LoginTest {

    //tests a correct username
    @Test
    public void testValidUsername() {
        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976"
        );

        assertTrue(user.checkUserName());
    }
    //tests a incorrect username
    @Test
    public void testInvalidUsername() {
        Login user = new Login(
                "Kyle",
                "Smith",
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976"
        );

        assertFalse(user.checkUserName());
    }
    //tests a correct password
    @Test
    public void testValidPassword() {
        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976"
        );

        assertTrue(user.checkPasswordComplexity());
    }
    //tests a incorrect password
    @Test
    public void testInvalidPassword() {
        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "password",
                "+27838968976"
        );

        assertFalse(user.checkPasswordComplexity());
    }
    //tests a correct cellphone number
    @Test
    public void testValidCellPhoneNumber() {
        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976"
        );

        assertTrue(user.checkCellPhoneNumber());
    }
    //tests a incorrect cellphone number
    @Test
    public void testInvalidCellPhoneNumber() {
        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "08966553"
        );

        assertFalse(user.checkCellPhoneNumber());
    }
    //tests a successful login
    @Test
    public void testSuccessfulLogin() {
        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976"
        );

        user.setLoginDetails("kyl_1", "Ch&&sec@ke99!");

        assertTrue(user.loginUser());
    }
    //tests a unsuccessful logins
    @Test
    public void testFailedLogin() {
        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976"
        );

        user.setLoginDetails("kyle!!!!!!!", "password");

        assertFalse(user.loginUser());
    }
}