package spe.uoblibraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import spe.uoblibraryapp.nfc.IntentException;
import spe.uoblibraryapp.nfc.NFC;
import spe.uoblibraryapp.nfc.NFCTechException;

public class Home extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onNewIntent(Intent intent){
        NFC nfcScanner = new NFC();
        try {
            nfcScanner.setNfcTag(intent);
        } catch (IOException e){
            //Do stuff
        } catch (NFCTechException e) {
            //Handle this too please
        } catch (IntentException e) {
            //Yep and this
        }


        /*
        Some more code, calling getBookID and stuff
         */

        nfcScanner.removeSecureSetting();
    }
}
