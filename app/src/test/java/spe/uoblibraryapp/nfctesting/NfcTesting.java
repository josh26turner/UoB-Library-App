package spe.uoblibraryapp.nfctesting;

import org.junit.Test;

import java.io.IOException;

import spe.uoblibraryapp.nfc.IntentException;
import spe.uoblibraryapp.nfc.NFC;
import spe.uoblibraryapp.nfc.NFCTechException;

public class NfcTesting {

    @Test
    public void NfcTesting() {
        IntentTestingPassing intent = new IntentTestingPassing();

        try {
            NFC nfc = new NFC(intent);
        } catch (NFCTechException | IntentException | IOException e) {
            e.printStackTrace();
        }
    }
}
