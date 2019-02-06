package spe.uoblibraryapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.api.IntentActions;
import spe.uoblibraryapp.api.WMSException;
import spe.uoblibraryapp.api.WMSResponse;
import spe.uoblibraryapp.api.ncip.WMSNCIPElement;
import spe.uoblibraryapp.api.ncip.WMSNCIPResponse;
import spe.uoblibraryapp.api.ncip.WMSNCIPService;
import spe.uoblibraryapp.api.wmsobjects.WMSLoan;
import spe.uoblibraryapp.api.wmsobjects.WMSParseException;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;


public class FragmentLoans extends android.support.v4.app.Fragment {
    private static final String TAG = "LoansFragment";
    View view;
    private MyBroadCastReceiver myBroadCastReceiver;
    private CacheManager cacheManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_loans, container, false);

        myBroadCastReceiver = new MyBroadCastReceiver();

        cacheManager = CacheManager.getInstance();

        // Swipe to Refresh
        SwipeRefreshLayout swipeRefreshLoans = view.findViewById(R.id.swiperefresh);
        swipeRefreshLoans.setOnRefreshListener(() -> {
            // Pull to Refresh list
            swipeRefreshLoans.setRefreshing(true);
            Intent getUserProfileIntent = new Intent(IntentActions.LOOKUP_USER);
            WMSNCIPService.enqueueWork(getContext(), WMSNCIPService.class, 1000, getUserProfileIntent);
        });


        //Spinner creation
        Spinner spinner = view.findViewById(R.id.loan_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.loan_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Toast toast = Toast.makeText(getContext(), "Sorting by "+ spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerMyReceiver();
        if (cacheManager.isExpired()) {
            SwipeRefreshLayout swipeRefreshLoans = view.findViewById(R.id.swiperefresh);
            swipeRefreshLoans.setRefreshing(true);
            Intent getUserProfileIntent = new Intent(IntentActions.LOOKUP_USER);
            WMSNCIPService.enqueueWork(getContext(), WMSNCIPService.class, 1000, getUserProfileIntent);
        } else {
            fillListView(cacheManager.getUserProfile());
        }
    }

    // If we want thumbnails this gives us an image link https://www.googleapis.com/books/v1/volumes?q=isbn:9780226467047

    public void fillListView(WMSUserProfile userProfile) {
        NonScrollListView mListView = view.findViewById(R.id.listview);
        List<WMSLoan> bookList = new ArrayList<>(userProfile.getLoans());
        bookList.add(new WMSLoan()); // Just for testing
        bookList.add(new WMSLoan()); // Just for testing
        bookList.add(new WMSLoan()); // Just for testing
        bookList.add(new WMSLoan()); // Just for testing
        bookList.add(new WMSLoan()); // Just for testing
        bookList.add(new WMSLoan()); // Just for testing
        bookList.add(new WMSLoan()); // Just for testing
        bookList.add(new WMSLoan()); // Just for testing
        bookList.add(new WMSLoan()); // Just for testing

        LoanBookListAdapter adapter = new LoanBookListAdapter(getContext(), R.layout.adapter_view_layout, bookList);
        mListView.setAdapter(adapter);

        mListView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);

        updateDash(bookList);
    }

    private void updateDash(List<WMSLoan> bookList){
        //Update Dashboard
        TextView loan_dash_description = view.findViewById(R.id.loan_dash_description);
        loan_dash_description.setText("You have borrowed "
                + bookList.size()
                + " out of 40 books. The first book is due on "
                + bookList.get(0).getDueDate()
                + ".");
    }

    /**
     * This method is responsible to register an action to BroadCastReceiver
     */
    private void registerMyReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(IntentActions.USER_PROFILE_RESPONSE);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(myBroadCastReceiver, intentFilter);
            Log.d(TAG, "Reciever Registered");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(myBroadCastReceiver);
    }

    private WMSUserProfile parseUserProfileResponse(String xml) throws WMSException, WMSParseException {
        WMSResponse response = new WMSNCIPResponse(xml);

        if (response.didFail()) {
            throw new WMSException("There was an error retrieving the User Profile");
        }
        Document doc;
        try {
            doc = response.parse();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new WMSException("There was an error Parsing the WMS response");
        }
        Node node = doc.getElementsByTagName("ns1:LookupUserResponse").item(0);
        return new WMSUserProfile(new WMSNCIPElement(node));
    }


    class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.d(TAG, "onReceive() called");

                String xml = intent.getStringExtra("xml");

                WMSUserProfile userProfile = parseUserProfileResponse(xml);

                fillListView(userProfile);


                SwipeRefreshLayout swipeRefreshLoans = view.findViewById(R.id.swiperefresh);
                swipeRefreshLoans.setRefreshing(false);

                cacheManager.setUserProfile(userProfile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}