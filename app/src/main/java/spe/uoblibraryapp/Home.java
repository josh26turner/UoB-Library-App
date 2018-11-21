package spe.uoblibraryapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button buttonNFC = findViewById(R.id.btnNFC);
        buttonNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NFCActivity = new Intent(getApplicationContext(), HomeNFC.class);
                startActivity(NFCActivity);
            }
        });

    }

}