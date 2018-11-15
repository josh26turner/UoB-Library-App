package spe.uoblibraryapp.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;

public class NFC {
    private NfcV nfcTag;

    private Tag tagFromIntent (Intent intent){
        String intentAction = intent.getAction();
        if (intentAction.equals(NfcAdapter.ACTION_TECH_DISCOVERED)
                || intentAction.equals(NfcAdapter.ACTION_TAG_DISCOVERED)){
            return (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
        return null;
    }

    public void setNfcTag(Intent intent){
        Tag tag = tagFromIntent(intent);

        //if (tag )

        for (String tech : tag.getTechList()){
            if (tech.equals(NfcV.class.getName())){

            }
        }
    }

    public byte[] tagID(Tag tag){
        return tag.getId();
    }
}
