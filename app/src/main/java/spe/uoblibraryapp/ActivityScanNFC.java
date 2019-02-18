package spe.uoblibraryapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import spe.uoblibraryapp.api.ncip.WMSNCIPService;
import spe.uoblibraryapp.nfc.BarcodeException;
import spe.uoblibraryapp.nfc.IntentException;
import spe.uoblibraryapp.nfc.NFC;
import spe.uoblibraryapp.nfc.NFCTechException;

public class ActivityScanNFC extends AppCompatActivity {
    private static final String TAG = "Scan NFC Activity";

    private TextView txtContentSysInfo;
    private TextView txtBarcode;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private String[][] techList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nfc);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (!(nfcAdapter != null && nfcAdapter.isEnabled())){
            //Toast.makeText(this, "No NFC Detected", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Settings.ACTION_NFC_SETTINGS);
            startActivity(i);
            finish();
        } else {

            Intent pnd = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, pnd, 0);
            // Setup a tech list for NfcV tag.
            techList = new String[][]{ new String[]{NfcV.class.getName()} };

        }
        Activity myAct = this;

        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_anim);
        TextView txt = (TextView) findViewById(R.id.textView2);
        txt.startAnimation(shake);

        Button butt = findViewById(R.id.btnProblemReport2);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityScanNFC.ViewDialog alert = new ActivityScanNFC.ViewDialog();
                //TODO: Fix me, this call for showDialog needs changing.
                alert.showDialog(myAct);
            }
        });


    }

    /**
     * Called when an intent gets parsed to the activity,
     * hopefully an NFC tag.
     * @param scanIntent - an intent sent to the activity, hopefully a tag
     */
    @Override
    protected void onNewIntent(Intent scanIntent) {
        Toast.makeText(this, "NFC Intent", Toast.LENGTH_SHORT).show();

        try {
            NFC nfc = new NFC(scanIntent);
            String sysInfo = bytesToHexString(nfc.getSystemInformation());


            // TODO Denis -> Change screen to show the book has been scanned and is now loading.

//            txtContentSysInfo.setText(sysInfo.substring(24, 26));
//            txtBarcode.setText(nfc.getBarcode());

            // TODO End


            // Send intent to WMSNCIPService with itemId
            Intent checkoutIntent = new Intent(Constants.IntentActions.CHECKOUT_BOOK);
            checkoutIntent.putExtra("itemId", nfc.getBarcode());
            WMSNCIPService.enqueueWork(getApplicationContext(), WMSNCIPService.class, 1000, checkoutIntent);


            // When checkout is complete the confirm activity is started by the WMSNCIPService.

        } catch (NFCTechException e) {
            e.printStackTrace();
            Log.d(TAG, "Not the right NFC/RFID type");
        } catch (IntentException e) {
            e.printStackTrace();
            Log.d(TAG, "No tag in the intent");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Can't connect to the tag");
        } catch (BarcodeException e) {
            e.printStackTrace();
            Log.d(TAG, "There was a problem with the tag");
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

    public void onBackPressed(){
        Intent intent = new Intent(ActivityScanNFC.this, ActivityHome.class);
        startActivity(intent);
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

    //TODO: Document this ViewDialog.
    public class ViewDialog {

        public void showDialog(Activity activity){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_problems_scanning_layout);

            dialog.show();
        }
    }
}