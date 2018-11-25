package spe.uoblibraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ScanFragment extends android.support.v4.app.Fragment{

    private static final String TAG = "Scan Fragment";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);


        super.onCreate(savedInstanceState);

        Button buttonNFC = view.findViewById(R.id.btnNFC);
        buttonNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NFCActivity = new Intent(getActivity().getApplicationContext(), ScanNFCFragment.class);
                startActivity(NFCActivity);
            }
        });


        return view;
    }
}
