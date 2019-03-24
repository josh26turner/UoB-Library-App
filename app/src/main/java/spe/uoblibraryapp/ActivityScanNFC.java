package spe.uoblibraryapp;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import spe.uoblibraryapp.api.ncip.WMSNCIPService;
import spe.uoblibraryapp.nfc.BarcodeException;
import spe.uoblibraryapp.nfc.CheckedOutException;
import spe.uoblibraryapp.nfc.IntentException;
import spe.uoblibraryapp.nfc.NFC;
import spe.uoblibraryapp.nfc.NFCTechException;

public class ActivityScanNFC extends AppCompatActivity {
    private static final String TAG = "Scan NFC Activity";

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private String[][] techList;
    private Dialog scanDialog;
    private MyBroadCastReceiver myBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        myBroadCastReceiver = new MyBroadCastReceiver();
        setContentView(R.layout.activity_home_nfc);
        setTitle("Checkout a New Book");
        Activity myAct = this;

        //if (nfcAdapter != null) handled by ActivityHome.
        if (nfcAdapter.isEnabled()) {
            //Perform check in case user turns off NFC while in-app.

            SharedPreferences pref = getApplicationContext().getSharedPreferences("spinnerSelection", Context.MODE_PRIVATE);
            Intent pnd = new Intent(myAct, myAct.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(myAct, 0, pnd, 0);
            // Setup a tech list for NfcV tag.
            techList = new String[][]{new String[]{NfcV.class.getName()}};

            pref.getInt("spinnerInt", 0); //TODO: USE SELECTED ITEM

        }
        else{
            //disabled.
            Toast.makeText(this, "Please enable NFC & try again.", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Settings.ACTION_NFC_SETTINGS);
            startActivity(i);
            finish();
        }


        SharedPreferences pref = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        pref.edit().putString("lastSelectedLocation", getIntent().getStringExtra("location")).apply();
        Toast.makeText(getApplicationContext(), getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("lastSelectedLocation",""),Toast.LENGTH_LONG).show();

        Button butt = findViewById(R.id.btnShowMeHow);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityScanNFC.ViewDialog alert = new ActivityScanNFC.ViewDialog();
                alert.showDialog(myAct, R.layout.dialog_problems_scanning_layout, true);
            }
        });
    }

    /**
     * Called when an intent gets passed to the activity,
     * hopefully an NFC tag.
     * @param scanIntent - an intent sent to the activity, hopefully a tag
     */
    @Override
    protected void onNewIntent(Intent scanIntent) {
        ProgressDialog nDialog;
        nDialog = new ProgressDialog(this);
        nDialog.setMessage("Loading...");
        nDialog.setTitle("Checkout in progress");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(false);
        nDialog.show();
        scanDialog = nDialog;

        ActivityScanNFC activityScanNFC = this;


        try {
            NFC nfc = new NFC(scanIntent);

            //TODO: Perform Internet check.
            if (!isNetworkConnected())
                //Not connected to Wifi!!!
                throw new NetworkErrorException();

            if (!isInternetWorking()){
                //No internet.
                Toast.makeText(activityScanNFC, "Sneaky.", Toast.LENGTH_LONG).show();
                finish();

            }



            Intent checkoutIntent = new Intent(Constants.IntentActions.CHECKOUT_BOOK);
            checkoutIntent.putExtra("itemId", nfc.getBarcode());
            // Send intent to WMSNCIPService with itemId
            WMSNCIPService.enqueueWork(getApplicationContext(), WMSNCIPService.class, 1000, checkoutIntent);


        } catch (NFCTechException e) {
            e.printStackTrace();
            nDialog.setMessage("Not the right NFC/RFID type");
            Log.d(TAG, "Not the right NFC/RFID type");
        } catch (IntentException e) {
            e.printStackTrace();
            Log.d(TAG, "No tag in the intent");
        } catch (IOException e) {
            e.printStackTrace();
            nDialog.setMessage("There was a problem with the tag. Hold phone still over tag.");
            Log.d(TAG, "Can't connect to the tag");
        } catch (BarcodeException e) {
            e.printStackTrace();
            nDialog.setMessage("There was a problem reading the tag");
            Log.d(TAG, "There was a problem with the tag");
        } catch (CheckedOutException e) {
            Log.e(TAG, "Book checked out");
            Toast.makeText(activityScanNFC, "Book checked out already", Toast.LENGTH_LONG).show();
            nDialog.cancel();
        } catch (NetworkErrorException e){
            Toast.makeText(this, "Please connect to the internet & try again.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, techList);
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.IntentActions.CHECKOUT_BOOK_RESPONSE);
            registerReceiver(myBroadCastReceiver, intentFilter);
            Log.d(TAG, "Receiver Registered");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        nfcAdapter.disableForegroundDispatch(this);
        if (scanDialog != null){
            scanDialog.dismiss();
        }
        unregisterReceiver(myBroadCastReceiver);
        super.onPause();
        Intent data = new Intent();
        data.putExtra("ended", "true");
        // Activity finished return ok, return the data
        setResult(RESULT_OK, data);
    }

    public void onBackPressed(){

        Intent data = new Intent();
        data.putExtra("ended", "true");
        // Activity finished return ok, return the data
        setResult(RESULT_OK, data);
        finish();
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    private boolean isInternetWorking() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.d(TAG, "onReceive() called");

                String xml = intent.getStringExtra("xml");
                Intent confirmIntent = new Intent(getApplicationContext(), ActivityConfirm.class);
                confirmIntent.putExtra("xml", xml);
                startActivity(confirmIntent);
                finish();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Problems Scanning Dialog
    public class ViewDialog {

        public void showDialog(Activity activity, int layoutResID, boolean setCanceledOnTouchOutside){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(setCanceledOnTouchOutside);
            dialog.setContentView(layoutResID);
            dialog.show();

        }




    }


}