package spe.uoblibraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import spe.uoblibraryapp.api.wmsobjects.WMSBook;
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

//        TextView tv_1 = $TV(R.id.book_name_confirm);
//        tv_1.setText(toBorrow.getTitle());
//        TextView tv_2 = $TV(R.id.book_author_confirm);
//        tv_2.setText(toBorrow.getAuthor());


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
