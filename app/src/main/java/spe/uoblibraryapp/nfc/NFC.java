package spe.uoblibraryapp.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;

import java.io.IOException;

public class NFC {
    private NfcV nfcTag;

    /**
     *
     * @param intent - intent that called the activity
     * @return - the tag in the book, or null
     */
    private Tag tagFromIntent (Intent intent){
        String intentAction = intent.getAction();

        if (intentAction.equals(NfcAdapter.ACTION_TECH_DISCOVERED)
                || intentAction.equals(NfcAdapter.ACTION_TAG_DISCOVERED))
                return (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


        return null;
    }

    /**
     *
     * @param intent - intent that called the activity
     * @throws NFCTechException - not the right type of tag
     * @throws IntentException - tag not present in the intent
     * @throws IOException - can't talk to the tag
     */
    public void setNfcTag(Intent intent) throws NFCTechException, IntentException, IOException {
        Tag tag = tagFromIntent(intent);

        if (tag == null) throw new IntentException();

        boolean techPresent = false;

        for (String tech : tag.getTechList()) {
            if (tech.equals(NfcV.class.getName())) {
                nfcTag = NfcV.get(tag);
                techPresent = true;
            }
            if (techPresent) break;
        }

        if (!techPresent) throw new NFCTechException();

        nfcTag.connect();
    }

    //public byte[] getBookID() throws IOException{

   // }

    /**
     *
     */
    public void removeSecureSetting(){
        //NFC Transceive to turn off security
    }
}
