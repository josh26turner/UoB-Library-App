package spe.uoblibraryapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class LoansFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "LoansFragment";

    private Button btnReservations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loans, container, false);
        btnReservations = view.findViewById(R.id.btnReservations);
        btnReservations.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getActivity(), "Going to Reservations!", Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).setViewPager("Reservations");
            }
        });

        return view;
    }
}
