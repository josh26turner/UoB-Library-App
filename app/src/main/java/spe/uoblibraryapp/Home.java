package spe.uoblibraryapp;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import spe.uoblibraryapp.nfc.NFC;

public class Home extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onNewIntent(Intent intent){
        String action = intent.getAction();
        if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)
                || action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)){
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NFC nfcReader = new NFC();
            byte [] id = nfcReader.tagID(tag);
        }
    }
}
