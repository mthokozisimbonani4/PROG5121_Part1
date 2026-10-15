/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121_part1;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashSet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

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
// Keeps track of generated IDs to avoid duplicates
private static HashSet<String> usedIDs = new HashSet<>();

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
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }
    
    // Creates a unique hash using the message information
    
    // Creates a message hash using the ID and message words
    public String createMessageHash() {

        // Get the first two digits of the message ID
        String firstTwoDigits = messageID.substring(0, 2);

        // Split the message into words
        String[] words = messageText.trim().split("\\s+");

        // Get the first and last words
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        // Remove punctuation from the first and last words
        firstWord = firstWord.replaceAll("[^a-zA-Z0-9]", "");
        lastWord = lastWord.replaceAll("[^a-zA-Z0-9]", "");

        // Generate the hash in uppercase
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

    // Generates a random 10-digit message ID
    public static String generateMessageID() {

        Random random = new Random();
        String newID;

        do {
            // Generate the first digit (1 to 9)
            int firstDigit = random.nextInt(9) + 1;

            // Generate the remaining nine digits
            int remainingDigits = random.nextInt(1000000000);

            newID = firstDigit
                    + String.format("%09d", remainingDigits);

        } while (usedIDs.contains(newID));

        // Remember the ID so it cannot be reused
        usedIDs.add(newID);

        return newID;
    }
    
    // Makes text safe to save in JSON
    private String escapeJSON(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Stores a message in a JSON file
    public String storeMessage() {

        try {
            Path file = Paths.get("stored_messages.json");

            // Create the JSON information for this message
            String jsonMessage = "  {\n"
                    + "    \"messageID\": \"" + escapeJSON(messageID) + "\",\n"
                    + "    \"messageNumber\": " + messageNumber + ",\n"
                    + "    \"recipient\": \"" + escapeJSON(recipient) + "\",\n"
                    + "    \"message\": \"" + escapeJSON(messageText) + "\",\n"
                    + "    \"messageHash\": \"" + escapeJSON(messageHash) + "\"\n"
                    + "  }";

            String content;

            if (Files.exists(file)) {

                // Read messages already stored in the file
                content = Files.readString(file, StandardCharsets.UTF_8).trim();

                if (content.equals("[]") || content.isEmpty()) {
                    content = "[\n" + jsonMessage + "\n]";
                } else if (content.startsWith("[") && content.endsWith("]")) {

                    // Add the new message before the closing bracket
                    content = content.substring(0, content.length() - 1).trim()
                            + ",\n" + jsonMessage + "\n]";
                } else {
                    return "Unable to store message: invalid JSON file.";
                }

            } else {

                // Create a new JSON array for the first message
                content = "[\n" + jsonMessage + "\n]";
            }

            // Save the updated information
            Files.writeString(file, content, StandardCharsets.UTF_8);

            return "Message successfully stored.";

        } catch (IOException e) {
            return "Error storing message: " + e.getMessage();
        }
    }

}
