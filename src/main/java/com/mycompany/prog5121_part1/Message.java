/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121_part1;
import java.util.ArrayList;

/**
 * This class handles the messaging features of QuickChat.
 * It stores message information and will be used
 * to validate, send and store messages.
 */
public class Message {

    // Information about each message
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    // Stores all successfully sent messages
private static ArrayList<Message> sentMessages = new ArrayList<>();

// Prevents the same message from being counted twice
private boolean sent = false;

    // Constructor
    public Message(String messageID, int messageNumber,
                   String recipient, String messageText) {

        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = "";
    }
    
    // Checks that the message ID is no more than 10 characters
    public Boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // Checks that the recipient number has the correct format
    public String checkRecipientCell() {

        if (recipient != null && recipient.matches("^\\+27[0-9]{9}$")) {
            return "Cell phone number successfully added.";
        } else {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
    }
    
    // Creates a unique hash using the message information
    public String createMessageHash() {

        // Get the first two digits of the message ID
        String firstTwoDigits = messageID.substring(0, 2);

        // Split the message into separate words
        String[] words = messageText.trim().split("\\s+");

        // Get the first and last words
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        // Create the hash and convert it to uppercase
        messageHash = (firstTwoDigits + ":" + messageNumber
                + ":" + firstWord + lastWord).toUpperCase();

        return messageHash;
    }
    
    // Checks that the message is not longer than 250 characters
    public String checkMessageLength() {

        if (messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int extraCharacters = messageText.length() - 250;

            return "Message exceeds 250 characters by "
                    + extraCharacters
                    + "; please reduce the size.";
        }
    }

    
    // Handles the user's choice to send, disregard or store a message
    public String SentMessage(int choice) {

        if (choice == 1) {

            if (sent) {
                return "Message already sent.";
            }

            sentMessages.add(this);
            sent = true;

            return "Message successfully sent.";

        } else if (choice == 2) {

            return "Press 0 to delete the message.";

        } else if (choice == 3) {

            return "Message successfully stored.";

        } else {

            return "Invalid option selected.";
        }
        }
    }

    // Returns the details of all sent messages
    public String printMessages() {

        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }

        String result = "";

        for (Message message : sentMessages) {

            result += "Message ID: " + message.messageID + "\n";
            result += "Message Hash: " + message.messageHash + "\n";
            result += "Recipient: " + message.recipient + "\n";
            result += "Message: " + message.messageText + "\n\n";
        }

        return result;
    }

    // Returns the total number of sent messages
    public int returnTotalMessagess() {
        return sentMessages.size();
    }

}
