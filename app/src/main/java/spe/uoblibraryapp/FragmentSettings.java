package spe.uoblibraryapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import spe.uoblibraryapp.api.IntentActions;
import spe.uoblibraryapp.api.ncip.AuthService;

public class FragmentSettings extends android.support.v4.app.Fragment{

    private static final String TAG = "Settings Fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        String [] values =  {"Pink","Green"};
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner_theme);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String color = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        Button butt = v.findViewById(R.id.goto_signin);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthService.enqueueWork(getContext(), AuthService.class, 1001, new Intent(IntentActions.AUTH_LOGOUT));
            }
        });
        return v;


    }

    @Override
    public void onResume(){
        TextView refreshToken = getView().findViewById(R.id.RefreshToken);
        TextView refreshExpiryDate = getView().findViewById(R.id.RefreshExpiryDate);
        TextView accessToken = getView().findViewById(R.id.AccessToken);
        TextView accessExpiryDate = getView().findViewById(R.id.AccessTokenExpiry);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        refreshExpiryDate.setText(pref.getString("refreshTokenExpiry", "default"));
        refreshToken.setText(pref.getString("refreshToken", "default"));
        accessExpiryDate.setText(pref.getString("authorisationTokenExpiry", "default"));
        accessToken.setText(pref.getString("authorisationToken", "default"));

        super.onResume();
    }



}
