package spe.uoblibraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityLibrarySelect extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_scan_select_library);
        ListView listView = findViewById(R.id.listViewSelectLibrary);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.spinner_library)))));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(), listView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ActivityLibrarySelect.this, ActivityScanNFC.class);

                intent.putExtra("location", getLocationCode(i));
                //Toast.makeText(getApplicationContext(),getLocationCode(i).toString() ,Toast.LENGTH_LONG).show();

                startActivityForResult(intent, 2404);
                ActivityScanNFC nfc = new ActivityScanNFC();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 2404)
            if (data.hasExtra("ended"))
                    if (data.getExtras().getString("ended").contains("true"))
                        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private String getLocationCode(int i){
        String s = "";
        if(i == 0) s = "119061";
        if(i == 1) s = "119036";
        if(i == 2) s = "119038";
        if(i == 3) s = "119045";
        if(i == 4) s = "119049";
        if(i == 5) s = "119054";
        if(i == 6) s = "119058";
        if(i == 7) s = "119059";
        if(i == 8) s = "119060";

        return s;
    }
}
