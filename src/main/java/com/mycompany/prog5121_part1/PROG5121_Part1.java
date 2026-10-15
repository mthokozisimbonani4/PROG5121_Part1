/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.prog5121_part1;

import java.util.Scanner;

public class PROG5121_Part1 {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // Get registration details
        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        System.out.print("Enter your username: ");
        String username = input.nextLine();

        System.out.print("Enter your password: ");
        String password = input.nextLine();

        System.out.print("Enter your South African cell phone number: ");
        String cellPhoneNumber = input.nextLine();

        // Create a Login object
        Login user = new Login(firstName, lastName, username,
                password, cellPhoneNumber);

        System.out.println();
        System.out.println(user.registerUser());

        // Continue only if registration details are valid
        if (user.checkUserName()
                && user.checkPasswordComplexity()
                && user.checkCellPhoneNumber()) {

            System.out.println();
            System.out.println("Registration successful!");

            // Get login details
            System.out.print("Enter your username to login: ");
            String loginUsername = input.nextLine();

            System.out.print("Enter your password to login: ");
            String loginPassword = input.nextLine();

            user.setLoginDetails(loginUsername, loginPassword);

            System.out.println();
            System.out.println(user.returnLoginStatus());

            // Only allow QuickChat if login succeeds
            if (user.loginUser()) {

                System.out.println();
                System.out.println("Welcome to QuickChat.");

                // Ask how many messages the user wants to send
                System.out.print("How many messages would you like to enter? ");
                int maxMessages = readNumber(input, true);

                // Counts how many messages have been created
                int messagesEntered = 0;
                Message lastMessage = null;
                int choice = 0;

                // Keep showing the menu until the user quits
                while (choice != 3) {

                    System.out.println();
                    System.out.println("===== QUICKCHAT MENU =====");
                    System.out.println("1. Send Messages");
                    System.out.println("2. Show recently sent messages");
                    System.out.println("3. Quit");

                    System.out.print("Choose an option: ");
                    choice = readNumber(input, false);

                    
    if (choice == 1) {

    // Check if the user has reached the message limit
    if (messagesEntered >= maxMessages) {

        System.out.println("Message limit reached.");

    } else {

        // Get the recipient's cellphone number
        System.out.print("Enter recipient number: ");
        String recipient = input.nextLine();

        // Get the message
        System.out.print("Enter your message: ");
        String messageText = input.nextLine();

        // Generate the message ID automatically
        String messageID = Message.generateMessageID();

        // Increase the number of entered messages
        messagesEntered++;

        // Create the message object
        Message message = new Message(
                messageID,
                messagesEntered,
                recipient,
                messageText
        );
        lastMessage = message;

        // Validate the recipient
        String recipientResult = message.checkRecipientCell();

        // Validate the message length
        String lengthResult = message.checkMessageLength();

        System.out.println(recipientResult);
        System.out.println(lengthResult);

        // Invalid messages can be discarded, but never sent or stored.
        boolean validMessage = recipientResult.equals(
                "Cell phone number successfully captured.")
                && lengthResult.equals("Message ready to send.")
                && message.checkMessageID()
                && !messageText.trim().isEmpty();

        String hash = "";
        if (validMessage) {
            hash = message.createMessageHash();
            System.out.println();
            System.out.println("Message ID: " + messageID);
            System.out.println("Message Hash: " + hash);
        } else {
            System.out.println("Message cannot be sent or stored until valid.");
        }

        // Show actions even if the recipient is invalid, so Discard remains available.
        System.out.println();
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message");
        System.out.print("Choose an option: ");
        int messageChoice = readNumber(input, false);

        if (messageChoice == 2) {
            System.out.println(message.SentMessage(2));
            System.out.print("Enter 0 to delete: ");
            String deleteChoice = input.nextLine();
            if (deleteChoice.equals("0")) {
                System.out.println("Message disregarded.");
            }
        } else if (!validMessage && (messageChoice == 1 || messageChoice == 3)) {
            System.out.println("Message could not be processed.");
        } else if (messageChoice == 1) {
            System.out.println(message.SentMessage(1));
            System.out.println();
            System.out.println("Message ID: " + messageID);
            System.out.println("Message Hash: " + hash);
            System.out.println("Recipient: " + recipient);
            System.out.println("Message: " + messageText);
        } else if (messageChoice == 3) {
            System.out.println(message.storeMessage());
        } else {
            System.out.println("Invalid option selected.");
        }
    }

                    } else if (choice == 2) {

                        System.out.println("Coming Soon.");

                    } else if (choice == 3) {

                        int totalSent = 0;
                        if (lastMessage != null) {
                            totalSent = lastMessage.returnTotalMessagess();
                        }
                        System.out.println("Total messages sent: " + totalSent);
                        System.out.println("Thank you for using QuickChat.");

                    } else {

                        System.out.println("Invalid option. Please try again.");
                    }
                }
            }
        }

        input.close();
    }
    // Reads a number without crashing if the user types letters
    private static int readNumber(Scanner input, boolean mustBePositive) {
        while (true) {
            String answer = input.nextLine().trim();

            try {
                int number = Integer.parseInt(answer);
                if (!mustBePositive || number > 0) {
                    return number;
                }
                System.out.print("Please enter a number greater than zero: ");
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid whole number: ");
            }
        }
    }

}
