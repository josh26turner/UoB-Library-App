package spe.uoblibraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FragmentScan extends android.support.v4.app.Fragment {

    private static final String TAG = "Fines Fragment";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        Button butt = view.findViewById(R.id.btnScan);
        butt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            Intent NFCActivity = new Intent(getActivity(), ActivityScanNFC.class);
            startActivity(NFCActivity);
            }
        });

        return view;
    }
}
