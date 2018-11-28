package spe.uoblibraryapp.nfctesting;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class IntentTestingPassing extends Intent {
    @Override
    public String getAction(){
        return NfcAdapter.ACTION_TAG_DISCOVERED;
    }

    @Override
    public Tag getParcelableExtra(String name) {
        Intent intent = new Intent();
        intent.putExtra(NfcAdapter.EXTRA_TAG, TagTestPass.mockTag());
        if (intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) != null)
            System.out.println("WOOOO");
        else
            System.out.println("BOOOO");

        return null;
    }

}