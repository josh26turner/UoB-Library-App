package spe.uoblibraryapp.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;

import java.io.IOException;

import static spe.uoblibraryapp.nfc.Hex.*;


public class NFC {
    private NfcV nfcTag = null;

    /**
     * Turns the intent into a tag
     * @param intent - intent that called the activity
     * @return - the tag in the book, or null
     */
    private Tag tagFromIntent (Intent intent) {
        String intentAction = intent.getAction();

        if (intentAction.equals(NfcAdapter.ACTION_TECH_DISCOVERED)
                || intentAction.equals(NfcAdapter.ACTION_TAG_DISCOVERED))
            return (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


        return null;
    }

    /**
     * Should be called first, turns the intent into an NfcV object, via a tag
     * @param intent - intent that called the activity
     * @throws NFCTechException - not the right type of tag
     * @throws IntentException - tag not present in the intent
     * @throws IOException - can't talk to the tag
     */
    public void setNfcTag(Intent intent) throws NFCTechException, IntentException, IOException {
        Tag tag = tagFromIntent(intent);

        if (tag == null) throw new IntentException("No tag in intent");

        boolean techPresent = false;

        for (String tech : tag.getTechList())
            if (tech.equals(NfcV.class.getName())) {
                nfcTag = NfcV.get(tag);
                techPresent = true;
                break;
            }

        if (!techPresent) throw new NFCTechException("No ISO 15693 tag detected");

        nfcTag.connect();
    }

    /**
     * Reads the bytes in the tag which are the book's UID
     * @return - the book ID
     * @throws IOException - if the tag can't be communicated with
     * @throws IntentException - if the tag hasn't been set
     */
    public byte[] getBookID() throws IOException, IntentException {
        if (nfcTag == null) throw new IntentException("Intent not set yet!");

        byte[] commands = readMultipleBlocksCommand(2, 1);

        return nfcTag.transceive(commands);
    }

    /**
     *
     * @return - the tag info
     * @throws IOException - if the tag can't be communicated with
     * @throws IntentException - if the tag hasn't been set
     */
    public byte[] getSystemInfo() throws IOException, IntentException {
        if (nfcTag == null) throw new IntentException("Intent not set yet!");
        else return nfcTag.transceive(SYSTEM_INFO_COMMAND);
    }

    /**
     *
     * @throws IOException - if the tag can't be communicated with
     * @throws IntentException - if the tag hasn't been set
     */
    public void removeSecureSetting() throws IOException, IntentException {
        if (nfcTag == null) throw new IntentException("Intent not set yet!");

        if (!isSecured()) {
            //NFC Transceive to turn off security
        }
    }

    /**
     * Reads the tag to see if security is on or off
     * @return - if the security is on return true
     * @throws IOException - if the tag can't be communicated with
     */
    private boolean isSecured() throws IOException{
        //Read the security block
        return false; //For now
    }
}
