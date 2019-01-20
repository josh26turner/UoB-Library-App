package spe.uoblibraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import stanford.androidlib.SimpleActivity;

public class ActivityConfirm extends SimpleActivity {
    private String mBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        Intent intent = getIntent();
        String barcode = intent.getStringExtra("key");
        mBarcode = barcode;


        Button butt = $B(R.id.borrow_button);
        butt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirmBorrow();
            }
        });

    }

        @Override
        public void onBackPressed () {
            super.onBackPressed();
        }


        private void confirmBorrow () {
            //TODO: send borrow request here
        }


}
