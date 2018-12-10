package spe.uoblibraryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import stanford.androidlib.SimpleActivity;


public class ActivitySignIn extends SimpleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button butt = $B(R.id.signin_butt);
        butt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toast("User Sign In");
            }
        });
    }
}
