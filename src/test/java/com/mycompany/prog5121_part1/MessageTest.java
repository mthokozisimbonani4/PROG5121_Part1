package com.mycompany.prog5121_part1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the QuickChat Message class.
 */
public class MessageTest {

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
        assertEquals("Cell phone number successfully added.",
                message.checkRecipientCell());
    }

    @Test
    public void testInvalidRecipient() {
        Message message = new Message("0012345678", 0,
                "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.",
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
}
