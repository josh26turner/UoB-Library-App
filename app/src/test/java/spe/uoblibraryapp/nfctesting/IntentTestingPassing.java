package spe.uoblibraryapp.nfctesting;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

public class IntentTestingPassing extends Intent {
    @Override
    public String getAction(){
        return NfcAdapter.ACTION_TAG_DISCOVERED;
    }

//    @Override
//    public <T extends Parcelable> T getParcelableExtra(String name) {
//        return TagTestPass.mockTag();
//    }
}
