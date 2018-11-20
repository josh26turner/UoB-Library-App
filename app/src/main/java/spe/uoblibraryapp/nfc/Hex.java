package spe.uoblibraryapp.nfc;

class Hex {
    private static final byte GET_SYSTEM_INFO = (byte) 0x2B;
    private static final byte READ_MULTIPLE_BLOCKS = (byte) 0x23;
    private static final byte FLAGS = (byte) 0x00;

    static byte [] SYSTEM_INFO_COMMAND = new byte[]{FLAGS, GET_SYSTEM_INFO};

    /**
     *
     * @param offset - start of reading
     * @param numberOfBlocks - how far to read
     * @return - what was in those blocks
     * @throws NumberFormatException - if the integers aren't in range
     */
    static byte [] readMultipleBlocksCommand(int offset, int numberOfBlocks) throws NumberFormatException{
        return new byte[] {FLAGS, READ_MULTIPLE_BLOCKS, toByte(offset), toByte(numberOfBlocks)};
    }

    /**
     * Slightly obfuscated but just sees if the int is in range and converts it
     * @param i - the number to convert
     * @return - the number in bytes
     * @throws NumberFormatException - if not between -128 and 127
     */
    private static byte toByte(int i) throws NumberFormatException{
        int orBy = Integer.MAX_VALUE - Byte.MAX_VALUE;
        if ((i | orBy) != 0) {
            return (byte) i;
        } else {
            throw new NumberFormatException();
        }
    }
}
