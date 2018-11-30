package spe.uoblibraryapp.api;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the WMS Exception
 */
public class WMSExceptionTest {

    /**
     * This is a test to make sure the message contained in WMSException
     * if it is not initialised with a message.
     */
    @Test
    public void testEmptyInitialisation(){
        Exception exception = new WMSException();
        assertEquals(
                "Message is not empty when WMSException is initialised empty.",
                "",
                exception.getMessage()

        );
    }


    /**
     * This is a test to ensure that WMSException contains the message it was given when
     * it was initialised.
     */
    @Test
    public void testInitialisation(){
        Exception exception = new WMSException("This is a test");
        assertEquals(
                "Message is not the message set when WMSException was initialised.",
                "This is a test",
                exception.getMessage()
        );
    }
}
