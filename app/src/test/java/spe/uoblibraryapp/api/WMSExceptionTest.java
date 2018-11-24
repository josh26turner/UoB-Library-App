package spe.uoblibraryapp.api;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the WMS Exception
 */
public class WMSExceptionTest {

    /**
     * This is a test to make sure the message contained in WMSException
     * if it is not initialised with a message.
     */
    @Test
    void testEmptyInitialisation(){
        Exception exception = new WMSException();
        assertEquals(
                "",
                exception.getMessage(),
                "Message is not empty when WMSException is initialised empty."
        );
    }


    /**
     * This is a test to ensure that WMSException contains the message it was given when
     * it was initialised.
     */
    @Test
    void testInitialisation(){
        Exception exception = new WMSException("This is a test");
        assertEquals(
                "This is a test",
                exception.getMessage(),
                "Message is not the message set when WMSException was initialised."
        );
    }
}
