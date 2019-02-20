package spe.uoblibraryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.api.WMSException;
import spe.uoblibraryapp.api.WMSResponse;
import spe.uoblibraryapp.api.ncip.WMSNCIPElement;
import spe.uoblibraryapp.api.ncip.WMSNCIPResponse;
import spe.uoblibraryapp.api.wmsobjects.WMSCheckout;
import stanford.androidlib.SimpleActivity;

public class ActivityConfirm extends SimpleActivity {
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
            WMSResponse response = new WMSNCIPResponse(xml);

            if (response.didFail()) {
                throw new WMSException("There was an error retrieving the User Profile");
            }
            Document doc;
            try {
                doc = response.parse();
            } catch (IOException | SAXException | ParserConfigurationException e) {
                throw new WMSException("There was an error Parsing the WMS response");
            }
            Node node = doc.getElementsByTagName("ns1:CheckOutItemResponse").item(0);
            WMSCheckout checkout = new WMSCheckout(new WMSNCIPElement(node), userId);


              ((TextView) findViewById(R.id.txt_bookName)).setText(checkout.getBook().getTitle());
            //  ((TextView) findViewById(R.id.txt_success)).setText("New addition to your library:");

            Button closeButton = findButton(R.id.close_button);

            closeButton.setOnClickListener((view) -> {
                finish();
            });



        } catch (Exception ex){
            // TODO fix this
            // Cry
            Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
        }






    }

    @Override
    public void onBackPressed () {
        finish();
    }
}
