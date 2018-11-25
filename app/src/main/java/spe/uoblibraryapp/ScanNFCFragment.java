package spe.uoblibraryapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static android.content.ContentValues.TAG;

import spe.uoblibraryapp.nfc.IntentException;
import spe.uoblibraryapp.nfc.NFC;
import spe.uoblibraryapp.nfc.NFCTechException;

public class ScanNFCFragment extends AppCompatActivity {
    private static final String TAG = "Scan NFC Fragment";

    private TextView txtContent;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private String[][] techList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nfc);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (!(nfcAdapter != null && nfcAdapter.isEnabled())){
            Toast.makeText(this, "No NFC Detected", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Settings.ACTION_NFC_SETTINGS);
            startActivity(i);
            finish();
        } else {
            txtContent = findViewById(R.id.txtContent);
            Intent pnd = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, pnd, 0);
            // Setup a tech list for NfcV tag.
            techList = new String[][]{ new String[]{NfcV.class.getName()} };
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Toast.makeText(this, "NFC Intent", Toast.LENGTH_SHORT).show();

        try {
            NFC nfc = new NFC(intent);

            try{
                String s = bytesToHexString(nfc.getBookID());
                txtContent.setText(s);
                Log.d(TAG, s);
            } catch(IOException e){
                Log.d(TAG, "Can't connect to the tag");
            } catch (IntentException e) {
                Log.d(TAG, "Intent not set");
            }
        } catch (NFCTechException e) {
            Log.d(TAG, "Not the right NFC/RFID type");
        } catch (IntentException e) {
            Log.d(TAG, "No tag in the intent");
        } catch (IOException e) {
            Log.d(TAG, "Can't connect to the tag");
        }


    }


    @Override
    protected void onResume() {
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, techList);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    /**
     * turns an array of bytes into hex representation
     * @param src - bytes to turn into hex
     * @return - the hex representation of the bytes as a string
     */
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");

        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];

        for (byte aSrc : src) {
            buffer[0] = Character.forDigit((aSrc >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(aSrc & 0x0F, 16);

            stringBuilder.append(buffer);
        }
        return stringBuilder.toString().toUpperCase();
    }


}