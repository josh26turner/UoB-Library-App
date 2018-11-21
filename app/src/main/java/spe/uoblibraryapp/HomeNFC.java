package spe.uoblibraryapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import spe.uoblibraryapp.nfc.NFC;

public class HomeNFC extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    TextView txtContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nfc);
        txtContent = findViewById(R.id.txtContent);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null && nfcAdapter.isEnabled()){
        }
        else{
            Toast.makeText(this, "No NFC Detected", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Toast.makeText(this, "NFC Intent", Toast.LENGTH_SHORT)
                .show();
        //write parsing here.

        NFC classNFC = new NFC();
        try{
            txtContent.setText( classNFC.getSystemInfo().toString()  );
        }
        catch(IOException e){
            txtContent.setText( "Error: Exception Thrown" );
        }



    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, NFC.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter,null);
        super.onResume();
    }

    @Override
    protected void onPause() {
        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }
}
