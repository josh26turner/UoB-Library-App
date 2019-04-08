package spe.uoblibraryapp.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;

import java.io.IOException;
import java.util.Arrays;

import static spe.uoblibraryapp.nfc.Hex.getSystemInfoCommand;
import static spe.uoblibraryapp.nfc.Hex.readSingleBlockCommand;
import static spe.uoblibraryapp.nfc.Hex.setSecurityOff;
import static spe.uoblibraryapp.nfc.Hex.setSecurityOn;

/**
 * Provides a simple class to read and write to NfcV tags in the University of Bristol library
 */
public class NFC {
    private NfcV nfcTag;
    private byte []
            tagID,
            systemInformation,
            userBlocks;

    /**
     * The constructor for the class, always called with an intent.
     * @param intent - the intent with the tag
     * @throws NFCTechException - not the right type of tag
     * @throws IntentException - not the right type of intent
     * @throws IOException - can't communicate with the tag
     */
    public NFC(Intent intent) throws NFCTechException, IntentException, IOException, CheckedOutException {
        setNfcTag(intent);

        nfcTag.connect();

        userBlocks = readMultipleBlocks(5);
        systemInformation = getSystemInfo();


//        if (isCheckedOut()) {
//            nfcTag.close();
//            throw new CheckedOutException();
//        }

    }


    /**
     * Turns the intent into a tag
     * @param intent - intent that called the activity
     * @return - the tag in the book, or null
     * @throws IntentException - if there's no tag in the intent
     */
    private Tag tagFromIntent (Intent intent) throws IntentException {
        if (intent == null) throw new IntentException("Null pointer");

        String intentAction = intent.getAction();

        if (intentAction != null)
            if (intentAction.equals(NfcAdapter.ACTION_TECH_DISCOVERED)
                    || intentAction.equals(NfcAdapter.ACTION_TAG_DISCOVERED))
                return (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        throw new IntentException("No tag in intent");
    }

    /**
     * Should be called first, turns the intent into an NfcV object, via a tag
     * @param intent - intent that called the activity
     * @throws NFCTechException - not the right type of tag
     * @throws IntentException - tag not present in the intent
     */
    private void setNfcTag(Intent intent) throws NFCTechException, IntentException {
        Tag tag = tagFromIntent(intent);

        tagID = tag.getId();

        boolean techPresent = false;

        for (String tech : tag.getTechList())
            if (tech.equals(NfcV.class.getName())) {
                nfcTag = NfcV.get(tag);
                techPresent = true;
                break;
            }

        if (!techPresent) throw new NFCTechException("No ISO 15693 tag detected");
    }

    /**
     * Gets the AFI to see if a book has already been checked out
     * @return - if the book is checked out already
     */
    private boolean isCheckedOut() {
        return systemInformation[11] ==  (byte) 0xC2;
    }

    /**
     * Converts the parts of the user blocks to the form printed on the barcode
     * @return - the printed version of the barcode
     */
    public String getBarcode() throws BarcodeException {
        if (userBlocks.length >= 3) {
            if ((userBlocks[0] == 0x04) && (userBlocks[1] == 0x11))
                return new String(userBlocks, 2, userBlocks.length - 2);

            else if ((userBlocks[0] == 0x11) && (userBlocks[1] == 0x04))
                return Integer.toString(sum(Arrays.copyOfRange(userBlocks, 2, 6)));

            else if ((userBlocks[0] == 0x41) && (userBlocks[1] == 0x08))
                return xCheck(Arrays.copyOfRange(userBlocks, 2, 10));

            else throw new BarcodeException("This is a new tag");
        } else throw new BarcodeException("Error in the tag or reading the tag");
    }

    /**
     * Reads the bytes in the tag which are the book's UID
     * @return - the book ID
     * @throws IOException - if the tag can't be communicated with
     */
    private byte[] readMultipleBlocks(int numberOfBlocks) throws IOException {
        byte[] returnValue = new byte[4 * numberOfBlocks];

        for (int i = 0; i < numberOfBlocks; i++) {
            byte[] block = nfcTag.transceive(readSingleBlockCommand(i, tagID));

            if (block[0] != (byte) 0x00) throw new IOException();

            System.arraycopy(block, 1, returnValue, 4 * i, block.length - 1);
        }

        return returnValue;
    }

    /**
     *
     * @return - the tag info
     * @throws IOException - if the tag can't be communicated with
     */
    private byte[] getSystemInfo() throws IOException {
        return nfcTag.transceive(getSystemInfoCommand(tagID));
    }

    /**
     * Stops the alarm going off if you take a book through
     * @throws IOException - if the tag can't be communicated with
     */
    public void removeSecureSetting() throws IOException {
        nfcTag.transceive(setSecurityOff(tagID));
    }

    /**
     * Makes the alarm go off if you take a book through
     * @throws IOException - if the tag can't be communicated with
     */
    public void putSecureSetting() throws IOException {
        nfcTag.transceive(setSecurityOn(tagID));
    }

    /**
     * Takes a list of bytes and returns the decimal integer form of them concatenated
     * E.g. sum({0x11, 0x11}) = 0x1111 = 4369
     * @param bytes - the bytes to transform into number form
     * @return - the int value of all the hex digits
     */
    private int sum(byte[] bytes){
        int sum = 0;
        int len = bytes.length;
        for (int i = 0; i < len; i++)
            sum += (bytes[i] & 0xFF) * Math.pow(256,len - i - 1);

        return sum;
    }

    /**
     * For converting barcodes that end in X
     * @param bytes - the tag bytes
     * @return - the barcode
     */
    private String xCheck(byte[] bytes) {
        StringBuilder barcode = new StringBuilder();

        for (int i = 0; i < 6; i += 3){
            barcode.append(((0xFF & bytes[i]) - 0xC3) / 4);

            barcode.append(((0xFF & bytes[i+1]) & 0xF0) >> 4);

            barcode.append((((((0xFF & bytes[i+1]) & 0x0F) << 4) + (((0xFF & bytes[i+2]) & 0xF0) >> 4)) - 0xC3) / 4);

            barcode.append((0xFF & bytes[i+2]) & 0x0F);
        }

        barcode.append(((0xFF & bytes[6]) - 0xC1) / 4);

        barcode.append('X');

        return barcode.toString();
    }


    public void close() throws IOException{
        nfcTag.close();
    }
}
