package spe.uoblibraryapp.nfc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NfcTest {
    @Test
    public void test() {
        byte[] sum1 = {0x11, 0x11};
        assertEquals(4369, NFC.sum(sum1));

        byte[] sum2 = {0x20, 0x27, 0x0, 0x1,0x2,0x3, 0x4, 0x5, 0x6, 0x7, (byte) 0xC2};
        assertEquals(2147483647, NFC.sum(sum2));

        byte[] xCheck = {(byte) 0xC7, 0x5C, 0x74, (byte) 0xE3, 0x7E, 0x31, (byte) 0xDD, (byte) 0x88};
        assertEquals("151487817X", NFC.xCheck(xCheck));
    }
}
