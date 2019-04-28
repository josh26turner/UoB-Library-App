package spe.uoblibraryapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.api.XMLParser;
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
    private ProgressDialog scanDialog;
    private MyBroadCastReceiver myBroadCastReceiver;

    private NFC nfcTag;
    private boolean secondScan = false;
    private String barcode;
    private String xmlCheckoutResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        myBroadCastReceiver = new MyBroadCastReceiver();
        setContentView(R.layout.activity_home_nfc);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        setTitle("Checkout a New Book");

        //load animation
        AnimationDrawable nfcAnimation;
        ImageView imageView = (ImageView) findViewById(R.id.imageView_NFC);
        imageView.setBackgroundResource(R.drawable.animation_nfc);
        nfcAnimation = (AnimationDrawable) imageView.getBackground();
        nfcAnimation.start();


        Activity myAct = this;
        //if (nfcAdapter != null) handled by ActivityHome.
        if (nfcAdapter.isEnabled()) {

            if (!isNetworkConnected()) {
                // Not connected to Wifi!!!
                Toast.makeText(this, "Please connect to the internet & try again.", Toast.LENGTH_LONG).show();
                finish();
            }
            if (!isInternetWorking()) {
                //No internet.
                Toast.makeText(this, "Cannot connect to the internet.", Toast.LENGTH_LONG).show();
                finish();

            }
        }

        //Perform check in case user turns off NFC while in-app.
        if (nfcAdapter.isEnabled()) {
            Intent pnd = new Intent(myAct, myAct.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(myAct, 0, pnd, 0);
            // Setup a tech list for NfcV tag.
            techList = new String[][]{new String[]{NfcV.class.getName()}};
            SharedPreferences pref = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
            pref.edit().putString("lastSelectedLocation", getIntent().getStringExtra("location")).apply();
        } else{
            //disabled.
            Toast.makeText(this, "Please enable NFC & try again.", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Settings.ACTION_NFC_SETTINGS);
            startActivity(i);
            finish();
        }


        SharedPreferences pref = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);

        pref.edit().putString("lastSelectedLocation", getIntent().getStringExtra("location")).apply();
//        Toast.makeText(getApplicationContext(), getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("lastSelectedLocation",""),Toast.LENGTH_LONG).show();
    }

    /**
     * Called when an intent gets passed to the activity,
     * hopefully an NFC tag.
     * @param scanIntent - an intent sent to the activity, hopefully a tag
     */
    @Override
    protected void onNewIntent(Intent scanIntent) {
        ProgressDialog nDialog;
        nDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        nDialog.setMessage("Loading...");
        nDialog.setTitle("Checkout in progress");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(false);
        nDialog.show();


        scanDialog = nDialog;
        if (secondScan) {
            try {
                NFC nfc = new NFC(scanIntent);
                if (nfc.getBarcode().equals(barcode)){
                    nfc.removeSecureSetting();
                    nfc.close();
                    Intent confirmIntent = new Intent(getApplicationContext(), ActivityConfirm.class);
                    confirmIntent.putExtra("xml", xmlCheckoutResponse);

                    startActivity(confirmIntent);
                    finish();
                } else {
                    nfc.close();
                    scanDialog.setMessage("Different book detected, scan the original book now.");
                }

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
                Toast.makeText(this, "Book checked out already", Toast.LENGTH_LONG).show();
                nDialog.cancel();
            }
        } else {
            try {
                if (!isNetworkConnected()) {
                    // Not connected to Wifi!!!
                    Toast.makeText(this, "Please connect to the internet & try again.", Toast.LENGTH_LONG).show();
                    finish();
                }
                nfcTag = new NFC(scanIntent);
                barcode = nfcTag.getBarcode();


                Intent checkoutIntent = new Intent(Constants.IntentActions.CHECKOUT_BOOK);

                checkoutIntent.putExtra("itemId", barcode);

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
                Toast.makeText(this, "Book checked out already", Toast.LENGTH_LONG).show();
                nDialog.cancel();
            }


//            Intent checkoutIntent = new Intent(Constants.IntentActions.CHECKOUT_BOOK);
//            checkoutIntent.putExtra("itemId", barcode);
//            // Send intent to WMSNCIPService with itemId
//            WMSNCIPService.enqueueWork(getApplicationContext(), WMSNCIPService.class, 1000, checkoutIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, techList);
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.IntentActions.CHECKOUT_BOOK_RESPONSE);
            intentFilter.addAction(Constants.IntentActions.CHECKOUT_BOOK_ERROR);
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


    private boolean didCheckoutFail(String xml){
        Document doc;
        try {
            doc = XMLParser.parse(xml);
        } catch (IOException | ParserConfigurationException | SAXException ex){
            Toast.makeText(getApplicationContext(), "This book is confined to the library", Toast.LENGTH_LONG).show();
            return true;
        }

        NodeList problem = doc.getElementsByTagName("ns1:Problem");
        if (problem.getLength() == 0) return false;

        NodeList problemTypes = doc.getElementsByTagName("ns1:ProblemType");

        if (problemTypes.getLength() == 1) {
            if ("Unknown Item".equals(problemTypes.item(0).getTextContent()))
                Toast.makeText(getApplicationContext(), "This book is confined to the library", Toast.LENGTH_LONG).show();
            else Toast.makeText(getApplicationContext(), "Unknown Error checking book out", Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(getApplicationContext(), "Unknown Error checking book out", Toast.LENGTH_LONG).show();

        return true;
    }


    class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.d(TAG, "onReceive() called");
                if (Constants.IntentActions.CHECKOUT_BOOK_RESPONSE.equals(intent.getAction())) {
                    xmlCheckoutResponse = intent.getStringExtra("xml");

                    if (didCheckoutFail(xmlCheckoutResponse)) {
                        Log.e(TAG, "erro222222222r1");
                        scanDialog.dismiss();
                    }
                    else {
                        try {
                            nfcTag.removeSecureSetting();
                            nfcTag.close();

                            Intent confirmIntent = new Intent(getApplicationContext(), ActivityConfirm.class);
                            confirmIntent.putExtra("xml", xmlCheckoutResponse);
                            startActivity(confirmIntent);
                            finish();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            secondScan = true;
                            scanDialog.setMessage("Please scan the book again.");
                            Log.d(TAG, "waiting for second scan.");

                        }
                    }
                } else if (Constants.IntentActions.CHECKOUT_BOOK_ERROR.equals(intent.getAction())) {
                    scanDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Unknown Error checking book out", Toast.LENGTH_LONG).show();

                }


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