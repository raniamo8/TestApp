package com.example.testapp;

import com.example.testappcustomer.Recipient;
import android.graphics.Bitmap;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * The is the test section for the class Recipient.
 */
public class RecipientTest {

    private Recipient recipient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        recipient = new Recipient("slimshady");
    }

    /**
     * Checks if the getName method returns the correct name of the recipient.
     */
    @Test
    public void getNameTest() {
        String name = recipient.getName();
        assertEquals("slimshady", name);
    }

    /**
     * Checks if the setName method sets the name of the recipient correctly.
     */
    @Test
    public void setNameTest() {
        recipient.setName("Springer");
        String name = recipient.getName();
        assertEquals("Springer", name);
    }

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


}
