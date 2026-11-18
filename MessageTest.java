package com.mycompany.prog5121_part1;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the QuickChat Message class.
 */
public class MessageTest {

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {

        Message.resetForTests();

        Message.setStorageFile(
                tempDir.resolve("test_stored_messages.json"));
    }

    // ORIGINAL PART 1 AND PART 2 TESTS

    @Test
    public void testValidMessageID() {
        Message message = new Message("1234567890", 0,
                "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(message.checkMessageID());
    }

    @Test
    public void testInvalidMessageID() {
        Message message = new Message("12345678901", 0,
                "+27718693002", "Hi Mike");
        assertFalse(message.checkMessageID());
    }

    @Test
    public void testValidRecipient() {
        Message message = new Message("0012345678", 0,
                "+27718693002", "Hi Mike");
        assertEquals("Cell phone number successfully captured.",
                message.checkRecipientCell());
    }

    @Test
    public void testInvalidRecipient() {
        Message message = new Message("0012345678", 0,
                "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message.checkRecipientCell());
    }

    @Test
    public void testMessageLengthSuccess() {
        Message message = new Message("0012345678", 0,
                "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", message.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        String longMessage = "A".repeat(255);
        Message message = new Message("0012345678", 0,
                "+27718693002", longMessage);
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.",
                message.checkMessageLength());
    }

    @Test
    public void testMessageHash() {
        Message message = new Message("0012345678", 0,
                "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    @Test
    public void testSendMessageOption() {
        Message message = new Message("0012345678", 0,
                "+27718693002", "Hi Mike");
        assertEquals("Message successfully sent.", message.SentMessage(1));
    }

    @Test
    public void testDisregardMessageOption() {
        Message message = new Message("0012345678", 0,
                "+27718693002", "Hi Mike");
        assertEquals("Press 0 to delete the message.", message.SentMessage(2));
    }

    @Test
    public void testStoreMessageOption() {
        Message message = new Message("0012345678", 0,
                "+27718693002", "Hi Mike");
        assertEquals("Message successfully stored.", message.SentMessage(3));
    }

    @Test
    public void testGeneratedMessageID() {
        String id = Message.generateMessageID();
        assertEquals(10, id.length());
        assertTrue(id.matches("[0-9]{10}"));
    }

    @Test
    public void testGeneratedIDsAreDifferent() {
        String first = Message.generateMessageID();
        String second = Message.generateMessageID();
        assertNotEquals(first, second);
    }

    @Test
    public void testOtherMessageHashes() {
        String[] texts = {
            "Hello, how are you today!",
            "Thanks for your help."
        };

        String[] expected = {
            "12:1:HELLOTODAY",
            "12:2:THANKSHELP"
        };

        for (int i = 0; i < texts.length; i++) {
            Message message = new Message("1234567890", i + 1,
                    "+27718693002", texts[i]);
            assertEquals(expected[i], message.createMessageHash());
        }
    }

    @Test
    public void testMessageLengthBoundary() {
        Message message = new Message("1234567890", 1,
                "+27718693002", "A".repeat(250));
        assertEquals("Message ready to send.", message.checkMessageLength());
    }

    // PART 3 TESTS

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {

        Message firstMessage = new Message(
                "1111111111", 1,
                "+27834557896", "Did you get the cake?");

        Message fourthMessage = new Message(
                "0838884567", 4,
                "+27834557896", "It is dinner time!");

        firstMessage.createMessageHash();
        fourthMessage.createMessageHash();

        firstMessage.SentMessage(1);
        fourthMessage.SentMessage(1);

        String[] expected = {
            "Did you get the cake?",
            "It is dinner time!"
        };

        assertArrayEquals(expected, Message.getSentMessageTexts());
    }

    @Test
    public void testDisplayLongestStoredMessage() {

        Message firstStored = new Message(
                "2222222222", 2,
                "Sender", "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");

        Message secondStored = new Message(
                "5555555555", 5,
                "Sender", "+27838884567",
                "Ok, I am leaving without you.");

        firstStored.createMessageHash();
        secondStored.createMessageHash();

        assertEquals("Message successfully stored.",
                firstStored.storeMessage());

        assertEquals("Message successfully stored.",
                secondStored.storeMessage());

        Message.readStoredMessages();

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                Message.getLongestStoredMessageText());
    }

    @Test
    public void testSearchMessageByID() {

        Message fourthMessage = new Message(
                "0838884567", 4,
                "+27834557896", "It is dinner time!");

        fourthMessage.createMessageHash();
        fourthMessage.SentMessage(1);

        assertEquals(
                "It is dinner time!",
                Message.getMessageTextByID("0838884567"));
    }

    @Test
    public void testSearchMessagesByRecipient() {

        Message firstStored = new Message(
                "2222222222", 2,
                "Sender", "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");

        Message secondStored = new Message(
                "5555555555", 5,
                "Sender", "+27838884567",
                "Ok, I am leaving without you.");

        firstStored.createMessageHash();
        secondStored.createMessageHash();

        firstStored.storeMessage();
        secondStored.storeMessage();

        Message.readStoredMessages();

        String[] expected = {
            "Where are you? You are late! I have asked you to be on time.",
            "Ok, I am leaving without you."
        };

        assertArrayEquals(
                expected,
                Message.getMessagesForRecipient("+27838884567"));
    }

    @Test
    public void testDeleteMessageUsingHash() throws Exception {

        Message firstStored = new Message(
                "2222222222", 2,
                "Sender", "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");

        Message secondStored = new Message(
                "5555555555", 5,
                "Sender", "+27838884567",
                "Ok, I am leaving without you.");

        firstStored.createMessageHash();
        secondStored.createMessageHash();

        firstStored.storeMessage();
        secondStored.storeMessage();

        Message.readStoredMessages();

        assertTrue(Message.removeStoredMessageByHash(
                firstStored.createMessageHash()));

        Message.readStoredMessages();

        assertEquals(
                "Ok, I am leaving without you.",
                Message.getLongestStoredMessageText());

        String json = Files.readString(
                tempDir.resolve("test_stored_messages.json"));

        assertFalse(json.contains(
                "Where are you? You are late!"));
    }

    @Test
    public void testDisplayStoredMessagesReport() {

        Message storedMessage = new Message(
                "2222222222", 2,
                "Sender", "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");

        storedMessage.createMessageHash();
        storedMessage.storeMessage();

        Message.readStoredMessages();

        String report = Message.buildStoredMessagesReport();

        assertTrue(report.contains("Message ID: 2222222222"));
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient: +27838884567"));
        assertTrue(report.contains(
                "Where are you? You are late! I have asked you to be on time."));
    }

    @Test
    public void testReadStoredMessagesFromJSON() {

        Message storedMessage = new Message(
                "2222222222", 2,
                "Sender", "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");

        storedMessage.createMessageHash();
        storedMessage.storeMessage();

        // Clear the in-memory arrays to simulate a fresh application load
        Message.resetForTests();

        Message.readStoredMessages();

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                Message.getLongestStoredMessageText());
    }
}