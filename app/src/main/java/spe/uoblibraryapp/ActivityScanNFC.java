package spe.uoblibraryapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import spe.uoblibraryapp.nfc.IntentException;
import spe.uoblibraryapp.nfc.NFC;
import spe.uoblibraryapp.nfc.NFCTechException;

public class ActivityScanNFC extends AppCompatActivity {
    private static final String TAG = "Scan NFC Fragment";

    private TextView txtContentUID;
    private TextView txtContentSysInfo;
    private TextView txtBarcode;

    private Button on;
    private Button off;

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
            txtContentUID = findViewById(R.id.txtContentUID);
            txtContentSysInfo = findViewById(R.id.txtContentSysInfo);
            txtBarcode = findViewById(R.id.Barcode);

            on = findViewById(R.id.button);
            off = findViewById(R.id.button2);

            Intent pnd = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, pnd, 0);
            // Setup a tech list for NfcV tag.
            techList = new String[][]{ new String[]{NfcV.class.getName()} };
        }
    }

    /**
     * Called when an intent gets parsed to the activity,
     * hopefully an NFC tag.
     * @param intent - an intent sent to the activity, hopefully a tag
     */
    @Override
    protected void onNewIntent(Intent intent) {
        Toast.makeText(this, "NFC Intent", Toast.LENGTH_SHORT).show();

        try {
            NFC nfc = new NFC(intent);
            try{
                byte[] bytes = nfc.getBookID();
                String sysInfo = bytesToHexString(nfc.getSystemInfo());
                String UID = bytesToHexString(bytes);

                txtContentUID.setText(UID);
                txtContentSysInfo.setText(sysInfo);

                ActivityScanNFC thisClass = this;

                on.setOnClickListener(v -> {
                    try {
                        Log.d(TAG, "SECURE ONNNNNNNN");
                        nfc.putSecureSetting();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, "Can't connect to tag.");
                        Toast.makeText(thisClass, "Can't connect to tag.", Toast.LENGTH_SHORT).show();
                    }
                });

                off.setOnClickListener(v -> {
                    try {
                        nfc.removeSecureSetting();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, "Can't connect to tag.");
                        Toast.makeText(thisClass, "Can't connect to tag.", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.d(TAG, UID);
                Log.d(TAG, sysInfo);

                if (bytes.length >= 4 + 17) {
                    String barcode = new String(bytes, 4, 17);
                    txtBarcode.setText(barcode);
                    Log.d(TAG, barcode);
                }
            } catch(IOException e){
                e.printStackTrace();
                Log.d(TAG, "Can't connect to the tag");
            }
        } catch (NFCTechException e) {
            e.printStackTrace();
            Log.d(TAG, "Not the right NFC/RFID type");
        } catch (IntentException e) {
            e.printStackTrace();
            Log.d(TAG, "No tag in the intent");
        } catch (IOException e) {
            e.printStackTrace();
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
        return stringBuilder.toString().toUpperCase().replace('X','x');
    }

}