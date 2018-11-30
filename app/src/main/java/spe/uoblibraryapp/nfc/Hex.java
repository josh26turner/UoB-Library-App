package spe.uoblibraryapp.nfc;

class Hex {

    /**
     * This sets values for the ISO 15693 commands,
     * as detailed in http://www.ti.com/lit/an/sloa141/sloa141.pdf
     * It provides simple to use and easy reading commands for transmission to NfcV tags
     */

    private static final byte GET_SYSTEM_INFO = (byte) 0x2B;
    private static final byte READ_SINGLE_BLOCK = (byte) 0x20;
    private static final byte FLAGS = (byte) 0x20;
    private static final byte WRITE_AFI = (byte) 0x27;
    private static final byte AFI_CHECKED_OUT = (byte) 0xC2;
    private static final byte AFI_CHECKED_IN = (byte) 0xDA;

    /**
     *
     * @param id - the ID of the tag
     * @return - the constructed addressed command for turning off security
     */
    static byte [] setSecurityOff(byte[] id) {
        return addressedCommand(WRITE_AFI, AFI_CHECKED_OUT, id);
    }

    /**
     *
     * @param id - the ID of the tag
     * @return - the constructed addressed command for turning on security
     */
    static byte [] setSecurityOn(byte[] id) {
        return addressedCommand(WRITE_AFI, AFI_CHECKED_IN, id);
    }


    /**
     *
     * @param id - the ID of the tag
     * @return - the constructed addressed command for reading system informatio
     */
    static byte [] getSystemInfoCommand(byte [] id) {
        byte [] command = new byte[10];

        command[0] = FLAGS;
        command[1] = GET_SYSTEM_INFO;

        System.arraycopy(id, 0, command, 2, 8);

        return command;
    }

    /**
     *
     * @param offset - start of reading
     * @return - an addressed read block command for reading a single block
     * @throws NumberFormatException - if the integers aren't in range
     */
    static byte [] readSingleBlockCommand(int offset, byte [] id) throws NumberFormatException {
        return addressedCommand(READ_SINGLE_BLOCK, toByte(offset), id);
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

    /**
     * Constructing an addressed command with a parameter
     * @param command - the byte value of the command
     * @param parameter - the parameter to the byte
     * @param id - the ID of the tag
     * @return - a well constructed addressed command
     */
    private static byte[] addressedCommand(byte command, byte parameter, byte[] id){
        byte [] addressedCommand = new byte[11];

        addressedCommand[0] = FLAGS;
        addressedCommand[1] = command;
        addressedCommand[10] = parameter;

        System.arraycopy(id, 0, addressedCommand, 2, 8);

        return addressedCommand;
    }
}
