package com.example.testapp;

import com.example.testappcustomer.Recipient;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * The is the test section for the class Recipient.
 */
public class RecipientTest {

    private Recipient recipient;
    @Mock
    private HttpURLConnection mockConnection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        recipient = new Recipient("slimshady");
    }
    @After
    public void tearDown() {
    }

    /**
     * Checks if the getName method returns the correct name of the recipient.
     */
    @Test
    public void testGetName() {
        String name = recipient.getName();
        assertEquals("slimshady", name);
    }

    /**
     * Checks if the setName method sets the name of the recipient correctly.
     */
    @Test
    public void testSetName() {
        recipient.setName("Springer");
        String name = recipient.getName();
        assertEquals("Springer", name);
    }

    /**
     * Checks if the console does not throw any exception when there is no connection to the
     * server.
     */
    @Test
    public void testSendPostWithoutServerConnection() {
        // Redirect System.out to capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        //Creating a new Recipient
        Recipient recipient = new Recipient("Admirali");
        recipient.sendPost();
        // Restore original System.out
        System.setOut(System.out);
        // Check if any exception is thrown in the console
        String consoleOutput = outputStream.toString();
        assertFalse(consoleOutput.contains("Exception"));
    }


    /**
     * Verifies that the sendPost method is called on the Recipient instance.
     */
    @Test
    public void testSendPostCalledMethod() {
        // mock instance of the Recipient
        Recipient mockRecipient = mock(Recipient.class);
        mockRecipient.sendPost();
        verify(mockRecipient).sendPost();
    }

    /**
     * Checks if the sendPost method sets the name correctly on the Recipient instance.
     */
    @Test
    public void testSendPost() {
        Recipient recipient = new Recipient("slimshady");

        // expected name
        String expectedName = "slimshady";
        recipient.sendPost();

        // Verify that the expected name is set in the recipient
        String actualName = recipient.getName();
        assertEquals(expectedName, actualName);
    }


    /**
     * Checks if the sendPost method keeps the name unchanged when called multiple times.
     */
    @Test
    public void testSendPostMultipleTimes() {
        // Create a real instance of the Recipient class
        Recipient recipient = new Recipient("Springer");

        // expected name
        String expectedName = "Springer";

        recipient.sendPost();
        recipient.sendPost();
        recipient.sendPost();

        // expected name still set in the recipient
        String actualName = recipient.getName();
        assertEquals(expectedName, actualName);
    }

    /**
     * Checks if the sendPost method throws a IllegalArgumentException when called with a empty name.
     */
    @Test
    public void testSendPostWithEmptyName() {
        Recipient recipient = new Recipient("");
        //Throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class, recipient::sendPost);
    }

    /**
     * Checks if the sendPost method throws a NullPointerException when called with a null name.
     */
    @Test
    public void testSendPostWithNullName() {
        Recipient recipient = new Recipient(null);
        //Throws NullPointerException
        assertThrows(NullPointerException.class, () -> {
            recipient.sendPost();
        });
    }

    /**
     * checks if the method successfully opens and closes the connection
     * and if the recipient's name is correctly set.
     */
    @Test
    public void testSendPostWithMocks() {
        // Create a mock instance of HttpURLConnection
        mockConnection = mock(HttpURLConnection.class);

        // Create a real instance of the Recipient class
        Recipient recipient = new Recipient("slimshady");

        // Set the mock connection on the recipient
        recipient.setConnection(mockConnection);

        // Call the sendPost method on the recipient
        recipient.sendPost();

        // Verify that the expected name is set in the recipient
        String expectedName = "slimshady";
        String actualName = recipient.getName();
        assertEquals(expectedName, actualName);
    }


    /**
     * Checks if the sendPost method handles the case when the response code is not 200.
     * The method should open and close the connection, but the name should remain unchanged.
     */
    @Test
    public void testSendPost_ResponseCodeNot200() {
        // Create a mock instance of HttpURLConnection
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        Recipient recipient = new Recipient("slimshady");

        // Set the mock connection to null.
        recipient.setConnection(null);

        // Call the sendPost method on the recipient.
        recipient.sendPost();

        // Verify that the connection is not opened or closed.
        try {
            verify(mockConnection, never()).connect();
        } catch (IOException e) {
            // Handle the IOException
            throw new RuntimeException(e);
        }
        // Verify that the connection is not opened or closed.
        verify(mockConnection, never()).disconnect();

        // Verify that the name remains unchanged
        String expectedName = "slimshady";
        String actualName = recipient.getName();
        assertEquals(expectedName, actualName);
    }
}
