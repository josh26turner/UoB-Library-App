package spe.uoblibraryapp.nfctesting;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class IntentTestingPassing extends Intent {
    @Override
    public String getAction(){
        return NfcAdapter.ACTION_TAG_DISCOVERED;
    }

    @Override
    public <T extends Parcelable> T getParcelableExtra(String name) {
        return (T) TagTestPass.mockTag();
    }

}
