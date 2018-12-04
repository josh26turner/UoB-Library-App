package spe.uoblibraryapp.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.util.Log;

import java.io.IOException;

import static spe.uoblibraryapp.nfc.Hex.*;


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
    public NFC(Intent intent) throws NFCTechException, IntentException, IOException {
        setNfcTag(intent);
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
     * @throws IOException - can't talk to the tag
     */
    private void setNfcTag(Intent intent) throws NFCTechException, IntentException, IOException {
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

        nfcTag.connect();

        //putSecureSetting();
        userBlocks = readMultipleBlocks(4);
        systemInformation = getSystemInfo();

        nfcTag.close();
    }

    /**
     * Get system information
     * @return - the information stored about the tag
     */
    public byte[] getSystemInformation() {
        return systemInformation;
    }

    /**
     * Converts the parts of the user blocks to the form printed on the barcode
     * @return - the printed version of the barcode
     */
    public String getBarcode() {

        if (userBlocks.length >= 3) {
            return new String(userBlocks, 2, userBlocks.length - 2);
        } else return "";
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
     *  Makes the alarmSYSTEM_INFO_COMMAND
     * @throws IOException - if the tag can't be communicated with
     */
    public void removeSecureSetting() throws IOException {
        nfcTag.transceive(setSecurityOff(tagID));
        //nfcTag.transceive(lockAFI(tagID));
    }

    /**
     *
     * @throws IOException - if the tag can't be communicated with
     */
    private void putSecureSetting() throws IOException {
        Log.d("NFC", bytesToHexString(nfcTag.transceive(setSecurityOn(tagID))));
        //Log.d("NFC", bytesToHexString(nfcTag.transceive(lockAFI(tagID))));
    }

    /**
     * turns an array of bytes into hex representation
     * @param src - bytes to turn into hex
     * @return - the hex representation of the bytes as a string
     */
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");

        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];

        for (byte aSrc : src) {
            buffer[0] = Character.forDigit((aSrc >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(aSrc & 0x0F, 16);

            stringBuilder.append(buffer);
        }
        return stringBuilder.toString().toUpperCase().replace('X','x');
    }
}