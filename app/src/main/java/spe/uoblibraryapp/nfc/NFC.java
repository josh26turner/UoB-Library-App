package spe.uoblibraryapp.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;

import java.io.IOException;

import static spe.uoblibraryapp.nfc.Hex.*;


public class NFC {
    private NfcV nfcTag;

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
     */
    public byte[] getBookID() throws IOException {
        byte[] commands = readMultipleBlocksCommand(0, 8);

        return nfcTag.transceive(commands);
    }

    /**
     *
     * @return - the tag info
     * @throws IOException - if the tag can't be communicated with
     */
    public byte[] getSystemInfo() throws IOException {
        return nfcTag.transceive(SYSTEM_INFO_COMMAND);
    }

    /**
     *  Makes the alarm
     * @throws IOException - if the tag can't be communicated with
     */
    public void removeSecureSetting() throws IOException {
        nfcTag.transceive(SET_SECURITY_OFF);
    }

    /**
     *
     * @throws IOException - if the tag can't be communicated with
     */
    public void putSecureSetting() throws IOException {
        nfcTag.transceive(SET_SECURITY_ON);
    }
}
