package spe.uoblibraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class ActivityLibrarySelect extends AppCompatActivity {
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_scan_select_library);
        ListView listView = findViewById(R.id.listViewSelectLibrary);
        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                new ArrayList<>(Constants.LibraryDetails.libraryBranches.values())
        );
        listView.setAdapter(
                arrayAdapter
        );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(), listView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ActivityLibrarySelect.this, ActivityScanNFC.class);

                intent.putExtra("location", getLocationCode(i));
                Toast.makeText(getApplicationContext(),getLocationCode(i),Toast.LENGTH_LONG).show();

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
        Log.e("Library Select", arrayAdapter.getItem(i));
        for (Map.Entry branch : Constants.LibraryDetails.libraryBranches.entrySet()){
            if (branch.getValue().equals(arrayAdapter.getItem(i))) return (String) branch.getKey();
        }

        return "";
    }
}
