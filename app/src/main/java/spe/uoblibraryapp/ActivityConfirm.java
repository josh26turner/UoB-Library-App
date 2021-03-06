package spe.uoblibraryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.api.WMSException;
import spe.uoblibraryapp.api.XMLParser;
import spe.uoblibraryapp.api.ncip.WMSNCIPElement;
import spe.uoblibraryapp.api.wmsobjects.WMSCheckout;

public class ActivityConfirm extends AppCompatActivity {
    private static final String TAG = "Confirm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        SharedPreferences preferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String userId = preferences.getString("principalID", "");


        Intent intent = getIntent();

        String xml = intent.getStringExtra("xml");

        try {
            Document doc;
            try {
                doc = XMLParser.parse(xml);
            } catch (IOException | SAXException | ParserConfigurationException e) {
                throw new WMSException("There was an error Parsing the WMS response");
            }
            Node node = doc.getElementsByTagName("ns1:CheckOutItemResponse").item(0);
            WMSCheckout checkout = new WMSCheckout(new WMSNCIPElement(node));

            ((TextView) findViewById(R.id.txt_bookName)).setText(checkout.getBook().getTitle());

            Button buttonClose = findViewById(R.id.close_button);
            buttonClose.setOnClickListener((view) -> {
                startActivity(new Intent(this, ActivityHome.class));
                finish();
            });
        } catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Fatal Error occurred! Try again later.", Toast.LENGTH_LONG);
            Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
        }

    }

    @Override
    public void onBackPressed () {
        startActivity(new Intent(this, ActivityHome.class));
    }
}
