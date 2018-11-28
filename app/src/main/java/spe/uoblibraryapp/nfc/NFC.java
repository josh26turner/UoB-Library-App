package spe.uoblibraryapp.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.util.Log;

import java.io.IOException;

import static spe.uoblibraryapp.nfc.Hex.*;


public class NFC {
    private NfcV nfcTag = null;
    private byte afi = (byte) 0x00;
    private final String TAG = "NFC Class";

    public NFC(Intent intent) throws NFCTechException, IntentException, IOException {
        setNfcTag(intent);
    }

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
    private void setNfcTag(Intent intent) throws NFCTechException, IntentException, IOException {

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

        byte[] commands = readMultipleBlocksCommand(0, 8);

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
        else {
            byte[] sysInfo = nfcTag.transceive(SYSTEM_INFO_COMMAND);

            afi = sysInfo[11];
            Log.d(TAG, String.format("%02X ", afi));

            return sysInfo;
        }
    }

    /**
     *
     * @throws IOException - if the tag can't be communicated with
     * @throws IntentException - if the tag hasn't been set
     */
    public void removeSecureSetting() throws IOException, IntentException {
        if (nfcTag == null) throw new IntentException("Intent not set yet!");

        if (isSecured()) nfcTag.transceive(SET_SECURITY_OFF);
        else Log.d(TAG, "Security already off.");
    }

    /**
     *
     * @throws IOException - if the tag can't be communicated with
     * @throws IntentException - if the tag hasn't been set
     */
    public void putSecureSetting() throws IOException, IntentException {
        if (nfcTag == null) throw new IntentException("Intent not set yet!");

        if (!isSecured()) nfcTag.transceive(SET_SECURITY_ON);
        else Log.d(TAG, "Security already on.");
    }

    /**
     * Reads the tag to see if security is on or off
     * @return - if the security is on return true
     * @throws IOException - if the tag can't be communicated with
     */
    private boolean isSecured() throws IntentException, IOException{
        if (nfcTag == null) throw new IntentException("Intent not set yet!");

        byte [] sysInfo = nfcTag.transceive(SYSTEM_INFO_COMMAND);

        afi = sysInfo[11];

        return (afi == AFI_CHECKED_IN);
    }
}
