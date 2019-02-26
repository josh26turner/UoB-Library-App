package spe.uoblibraryapp.nfc;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertArrayEquals;


public class HexTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void test() {
        byte [] id = {0x0, 0x1,0x2,0x3, 0x4, 0x5, 0x6, 0x7};

        byte [] setSecurityOff = {0x20, 0x27, 0x0, 0x1,0x2,0x3, 0x4, 0x5, 0x6, 0x7, (byte) 0xC2};
        assertArrayEquals(Hex.setSecurityOff(id), setSecurityOff);

        byte [] setSecurityOn = {0x20, 0x27, 0x0, 0x1,0x2,0x3, 0x4, 0x5, 0x6, 0x7, (byte) 0x07};
        assertArrayEquals(Hex.setSecurityOn(id), setSecurityOn);

        byte [] getSystemInfo = {0x20, 0x2B, 0x0, 0x1,0x2,0x3, 0x4, 0x5, 0x6, 0x7};
        assertArrayEquals(Hex.getSystemInfoCommand(id), getSystemInfo);

        byte [] readSingleBlock = {0x20, 0x20, 0x0, 0x1,0x2,0x3, 0x4, 0x5, 0x6, 0x7, 0x1};
        assertArrayEquals(Hex.readSingleBlockCommand(1, id), readSingleBlock);

        exceptionRule.expect(NumberFormatException.class);
        Hex.readSingleBlockCommand(128, id);

        exceptionRule.expect(NumberFormatException.class);
        Hex.readSingleBlockCommand(-1, id);
    }
}
