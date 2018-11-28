package spe.uoblibraryapp.nfc;

public class Hex {

    /**
     * This sets values for the ISO 15693 commands,
     * as detailed in http://www.ti.com/lit/an/sloa141/sloa141.pdf
     * It provides simple to use and easy reading commands for transmission to NfcV tags
     */


    private static final byte GET_SYSTEM_INFO = (byte) 0x2B;
    private static final byte READ_MULTIPLE_BLOCKS = (byte) 0x23;
    private static final byte FLAGS = (byte) 0x00;
    private static final byte WRITE_AFI = (byte) 0x27;
    private static final byte AFI_CHECKED_OUT = (byte) 0xC2;

    static final byte AFI_CHECKED_IN = (byte) 0xDA;

    static final byte [] SET_SECURITY_OFF = new byte[]{FLAGS, WRITE_AFI, AFI_CHECKED_OUT};
    static final byte [] SET_SECURITY_ON = new byte[]{FLAGS, WRITE_AFI, AFI_CHECKED_IN};
    static byte [] SYSTEM_INFO_COMMAND = new byte[]{FLAGS, GET_SYSTEM_INFO};

    /**
     *
     * @param offset - start of reading
     * @param numberOfBlocks - how far to read
     * @return - what was in those blocks
     * @throws NumberFormatException - if the integers aren't in range
     */
    public static byte [] readMultipleBlocksCommand(int offset, int numberOfBlocks) throws NumberFormatException {
        return new byte[] {FLAGS, READ_MULTIPLE_BLOCKS, toByte(offset), toByte(numberOfBlocks)};
    }

    /**
     *
     * @param i - the number to convert
     * @return - the number in bytes
     * @throws NumberFormatException - if not between -128 and 127
     */
    private static byte toByte(int i) throws NumberFormatException {
        if ((i <= 127) && (i >= -128)) return (byte) i;
        else throw new NumberFormatException();
    }
}
