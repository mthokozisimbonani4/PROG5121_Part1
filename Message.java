package com.mycompany.prog5121_part1;

import java.util.Arrays;
import java.util.Random;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Handles message creation, validation, storage and reporting.
 */
public class Message {

    private String messageID;
    private int messageNumber;
    private String sender;
    private String recipient;

    @SerializedName("message")
    private String messageText;

    private String messageHash;

    // Arrays required for the Part 3 assignment
    private static Message[] sentMessages = new Message[0];
    private static Message[] disregardedMessages = new Message[0];
    private static Message[] storedMessages = new Message[0];
    private static String[] messageHashes = new String[0];
    private static String[] messageIDs = new String[0];

    private boolean sent = false;

    private static HashSet<String> usedIDs = new HashSet<>();

    // Allows tests to use a separate JSON file
    private static Path storageFile = Paths.get("stored_messages.json");

    private static final Gson gson = new Gson();

    // Adds a message to a Message array
    private static Message[] addMessageToArray(
            Message[] messages, Message message) {

        Message[] updatedMessages =
                Arrays.copyOf(messages, messages.length + 1);

        updatedMessages[updatedMessages.length - 1] = message;

        return updatedMessages;
    }

    // Adds a value to a String array
    private static String[] addStringToArray(
            String[] values, String value) {

        String[] updatedValues =
                Arrays.copyOf(values, values.length + 1);

        updatedValues[updatedValues.length - 1] = value;

        return updatedValues;
    }

    // Tracks unique message IDs and hashes
    private static void trackMessage(Message message) {

        if (message.messageID != null
                && !Arrays.asList(messageIDs).contains(message.messageID)) {

            messageIDs = addStringToArray(
                    messageIDs, message.messageID);
        }

        if (message.messageHash != null
                && !Arrays.asList(messageHashes).contains(message.messageHash)) {

            messageHashes = addStringToArray(
                    messageHashes, message.messageHash);
        }
    }

    // Rebuilds the parallel arrays from all message categories
    private static void rebuildTrackingArrays() {

        messageIDs = new String[0];
        messageHashes = new String[0];

        for (Message message : sentMessages) {
            trackMessage(message);
        }

        for (Message message : disregardedMessages) {
            trackMessage(message);
        }

        for (Message message : storedMessages) {
            trackMessage(message);
        }
    }

    // Original constructor retained for existing tests
    public Message(String messageID, int messageNumber,
            String recipient, String messageText) {

        this(messageID, messageNumber, "", recipient, messageText);
    }

    // Constructor including sender details
    public Message(String messageID, int messageNumber,
            String sender, String recipient, String messageText) {

        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.sender = sender;
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

        if (recipient != null
                && recipient.matches("^\\+27[0-9]{9}$")) {

            return "Cell phone number successfully captured.";

        } else {

            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // Creates a message hash using the ID and message words
    public String createMessageHash() {

        if (messageID == null || messageID.length() < 2
                || messageText == null || messageText.trim().isEmpty()) {

            messageHash = "";
            return messageHash;
        }

        String firstTwoDigits = messageID.substring(0, 2);

        String[] words = messageText.trim().split("\\s+");

        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        firstWord = firstWord.replaceAll("[^a-zA-Z0-9]", "");
        lastWord = lastWord.replaceAll("[^a-zA-Z0-9]", "");

        messageHash = (firstTwoDigits + ":" + messageNumber
                + ":" + firstWord + lastWord).toUpperCase();

        return messageHash;
    }

    // Checks that the message is not longer than 250 characters
    public String checkMessageLength() {

        if (messageText != null && messageText.length() <= 250) {
            return "Message ready to send.";
        }

        int extraCharacters = messageText.length() - 250;

        return "Message exceeds 250 characters by "
                + extraCharacters
                + "; please reduce the size.";
    }

    // Handles the user's choice to send, disregard or store a message
    public String SentMessage(int choice) {

        if (choice == 1) {

            if (sent) {
                return "Message already sent.";
            }

            sentMessages = addMessageToArray(sentMessages, this);
            trackMessage(this);
            sent = true;

            return "Message successfully sent.";

        } else if (choice == 2) {

            disregardedMessages =
                    addMessageToArray(disregardedMessages, this);

            trackMessage(this);

            return "Press 0 to delete the message.";

        } else if (choice == 3) {

            return "Message successfully stored.";

        } else {

            return "Invalid option selected.";
        }
    }

    // Returns the details of all sent messages
    public String printMessages() {

        if (sentMessages.length == 0) {
            return "No messages have been sent.";
        }

        StringBuilder result = new StringBuilder();

        for (Message message : sentMessages) {

            result.append("Message ID: ")
                    .append(message.messageID).append("\n");

            result.append("Message Hash: ")
                    .append(message.messageHash).append("\n");

            result.append("Recipient: ")
                    .append(message.recipient).append("\n");

            result.append("Message: ")
                    .append(message.messageText).append("\n\n");
        }

        return result.toString();
    }

    // Returns the total number of sent messages
    public int returnTotalMessagess() {
        return sentMessages.length;
    }

    // Generates a unique random 10-digit message ID
    public static String generateMessageID() {

        Random random = new Random();
        String newID;

        do {
            int firstDigit = random.nextInt(9) + 1;
            int remainingDigits = random.nextInt(1000000000);

            newID = firstDigit
                    + String.format("%09d", remainingDigits);

        } while (usedIDs.contains(newID));

        usedIDs.add(newID);

        return newID;
    }

    // Stores a message in the JSON file
    public String storeMessage() {

        try {

            List<Message> messages = new ArrayList<>();

            if (Files.exists(storageFile)) {

                String existingJSON = Files.readString(
                        storageFile, StandardCharsets.UTF_8).trim();

                if (!existingJSON.isEmpty()) {

                    Message[] existingMessages =
                            gson.fromJson(existingJSON, Message[].class);

                    if (existingMessages != null) {
                        messages.addAll(Arrays.asList(existingMessages));
                    }
                }
            }

            // Avoid storing the same message twice
            for (Message existing : messages) {

                if (existing.messageID.equals(messageID)) {
                    return "Message is already stored.";
                }
            }

            messages.add(this);

            String updatedJSON = gson.toJson(messages);

            Files.writeString(
                    storageFile, updatedJSON, StandardCharsets.UTF_8);

            storedMessages = messages.toArray(new Message[0]);

            trackMessage(this);

            return "Message successfully stored.";

        } catch (IOException | RuntimeException e) {

            return "Error storing message: " + e.getMessage();
        }
    }

    // Reads stored messages from the JSON file into an array
    public static void readStoredMessages() {

        try {

            if (!Files.exists(storageFile)) {
                storedMessages = new Message[0];
                rebuildTrackingArrays();
                return;
            }

            String jsonContent = Files.readString(
                    storageFile, StandardCharsets.UTF_8);

            Message[] messages =
                    gson.fromJson(jsonContent, Message[].class);

            storedMessages = messages == null
                    ? new Message[0] : messages;

            rebuildTrackingArrays();

        } catch (IOException | RuntimeException e) {

            System.out.println(
                    "Error reading stored messages: " + e.getMessage());
        }
    }

    // Displays all stored messages
    public static void displayStoredMessages() {

        if (storedMessages.length == 0) {
            System.out.println("No stored messages found.");
            return;
        }

        System.out.println("\n--- STORED MESSAGES ---");

        for (Message message : storedMessages) {

            System.out.println("Sender: " + message.sender);
            System.out.println("Recipient: " + message.recipient);
            System.out.println("Message ID: " + message.messageID);
            System.out.println("Message: " + message.messageText);
            System.out.println("Message Hash: " + message.messageHash);
            System.out.println("-----------------------");
        }
    }

    // Returns the longest stored message
    static String getLongestStoredMessageText() {

        if (storedMessages.length == 0) {
            return null;
        }

        Message longestMessage = storedMessages[0];

        for (Message message : storedMessages) {

            if (message.messageText.length()
                    > longestMessage.messageText.length()) {

                longestMessage = message;
            }
        }

        return longestMessage.messageText;
    }

    // Displays the longest stored message
    public static void displayLongestStoredMessage() {

        String longestMessage = getLongestStoredMessageText();

        if (longestMessage == null) {
            System.out.println("No stored messages found.");
            return;
        }

        for (Message message : storedMessages) {

            if (message.messageText.equals(longestMessage)) {

                System.out.println("\n--- LONGEST STORED MESSAGE ---");
                System.out.println("Recipient: " + message.recipient);
                System.out.println("Message: " + message.messageText);
                System.out.println("Length: "
                        + message.messageText.length() + " characters");
                break;
            }
        }
    }

    // Finds a message by ID across the message arrays
    private static Message findMessageByID(String searchID) {

        for (Message message : sentMessages) {
            if (message.messageID.equals(searchID)) {
                return message;
            }
        }

        for (Message message : storedMessages) {
            if (message.messageID.equals(searchID)) {
                return message;
            }
        }

        for (Message message : disregardedMessages) {
            if (message.messageID.equals(searchID)) {
                return message;
            }
        }

        return null;
    }

    // Returns the message text for a given ID
    static String getMessageTextByID(String searchID) {

        Message message = findMessageByID(searchID);

        return message == null ? null : message.messageText;
    }

    // Searches for a message using its ID
    public static void searchStoredMessageByID(String searchID) {

        Message message = findMessageByID(searchID);

        if (message == null) {

            System.out.println("No message found with ID: " + searchID);

        } else {

            System.out.println("\n--- MESSAGE FOUND ---");
            System.out.println("Recipient: " + message.recipient);
            System.out.println("Message: " + message.messageText);
        }
    }

    // Returns sent and stored messages for a particular recipient
    static String[] getMessagesForRecipient(String searchRecipient) {

        List<String> results = new ArrayList<>();

        for (Message message : sentMessages) {

            if (message.recipient.equals(searchRecipient)) {
                results.add(message.messageText);
            }
        }

        for (Message message : storedMessages) {

            if (message.recipient.equals(searchRecipient)) {
                results.add(message.messageText);
            }
        }

        return results.toArray(new String[0]);
    }

    // Searches for all sent or stored messages for a recipient
    public static void searchStoredMessagesByRecipient(
            String searchRecipient) {

        String[] results = getMessagesForRecipient(searchRecipient);

        if (results.length == 0) {

            System.out.println(
                    "No messages found for: " + searchRecipient);

            return;
        }

        for (String messageText : results) {

            System.out.println("\n--- MATCHING MESSAGE ---");
            System.out.println("Recipient: " + searchRecipient);
            System.out.println("Message: " + messageText);
        }
    }

    // Deletes a stored message using its hash
    static boolean removeStoredMessageByHash(String searchHash) {

        int foundIndex = -1;

        for (int i = 0; i < storedMessages.length; i++) {

            if (storedMessages[i].messageHash.equals(searchHash)) {
                foundIndex = i;
                break;
            }
        }

        if (foundIndex == -1) {
            return false;
        }

        Message[] updatedMessages =
                new Message[storedMessages.length - 1];

        int index = 0;

        for (int i = 0; i < storedMessages.length; i++) {

            if (i != foundIndex) {
                updatedMessages[index] = storedMessages[i];
                index++;
            }
        }

        try {

            String updatedJSON = gson.toJson(updatedMessages);

            Files.writeString(
                    storageFile, updatedJSON, StandardCharsets.UTF_8);

            storedMessages = updatedMessages;

            rebuildTrackingArrays();

            return true;

        } catch (IOException | RuntimeException e) {

            System.out.println(
                    "Error updating stored messages: " + e.getMessage());

            return false;
        }
    }

    // Displays the result of deleting a stored message
    public static void deleteStoredMessageByHash(String searchHash) {

        Message deletedMessage = null;

        for (Message message : storedMessages) {

            if (message.messageHash.equals(searchHash)) {
                deletedMessage = message;
                break;
            }
        }

        if (deletedMessage == null) {
            System.out.println("No stored message found with that hash.");
            return;
        }

        if (removeStoredMessageByHash(searchHash)) {

            System.out.println(
                    "Message: \"" + deletedMessage.messageText
                    + "\" successfully deleted.");
        }
    }

    // Builds a complete report of stored messages
    static String buildStoredMessagesReport() {

        if (storedMessages.length == 0) {
            return "No stored messages available for the report.";
        }

        StringBuilder report = new StringBuilder();

        report.append("\n========== STORED MESSAGES REPORT ==========\n");

        for (int i = 0; i < storedMessages.length; i++) {

            Message message = storedMessages[i];

            report.append("\nMessage ").append(i + 1).append("\n");
            report.append("Message ID: ").append(message.messageID).append("\n");
            report.append("Message Hash: ").append(message.messageHash).append("\n");
            report.append("Sender: ").append(message.sender).append("\n");
            report.append("Recipient: ").append(message.recipient).append("\n");
            report.append("Message: ").append(message.messageText).append("\n");
            report.append("--------------------------------------------\n");
        }

        report.append("========== END OF REPORT ==========");

        return report.toString();
    }

    // Displays the stored messages report
    public static void displayStoredMessagesReport() {
        System.out.println(buildStoredMessagesReport());
    }

    // Package-private helpers used to isolate automated tests
    static void setStorageFile(Path file) {
        storageFile = file;
    }

    static String[] getSentMessageTexts() {

        String[] results = new String[sentMessages.length];

        for (int i = 0; i < sentMessages.length; i++) {
            results[i] = sentMessages[i].messageText;
        }

        return results;
    }

    static void resetForTests() {

        sentMessages = new Message[0];
        disregardedMessages = new Message[0];
        storedMessages = new Message[0];
        messageHashes = new String[0];
        messageIDs = new String[0];
        usedIDs = new HashSet<>();
    }
}