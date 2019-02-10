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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    private enum sort{ AZ, ZA, dueDateAZ, dueDateZA }
    private sort currentSort = sort.AZ;

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
        spinner.setSelected(false);  // Prevents onItemSelected from running during init.
        spinner.setSelection(0,true);  //read above, do NOT remove.


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Toast toast = Toast.makeText(getContext(), "Sorting by "+ spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT);
                toast.show();

                if (spinner.getSelectedItemId() == 0) currentSort = sort.AZ;
                else if (spinner.getSelectedItemId() == 1) currentSort = sort.ZA;
                else if (spinner.getSelectedItemId() == 2) currentSort = sort.dueDateAZ;
                else if (spinner.getSelectedItemId() == 3) currentSort = sort.dueDateZA;

                //Update View here.
                //TODO: DISABLE THIS WHILE REFRESHING.
                fillListView(cacheManager.getUserProfile());


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

    public class customComparatorAZ implements Comparator<WMSLoan> {
        public int compare(WMSLoan object1, WMSLoan object2) {
            if (object1.getBook().getTitle().compareTo(object2.getBook().getTitle()) < 0 )
                return -1;
            else if (object1.getBook().getTitle().compareTo(object2.getBook().getTitle()) > 0 )
                return 1;
            else
                return 0;
        }
    }
    public class customComparatorDueDate implements Comparator<WMSLoan> {
        public int compare(WMSLoan object1, WMSLoan object2) {
            return (object1.getDueDate().compareTo(object2.getDueDate()));
        }
    }

    public void fillListView(WMSUserProfile userProfile) {
        ListView mListView = view.findViewById(R.id.listview);
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
        if (bookList.isEmpty()) return; //TODO: TEST ME.

        switch (currentSort) {
            case AZ:
                Collections.sort(bookList, new customComparatorAZ());
                break;
            case ZA:
                Collections.sort(bookList, new customComparatorAZ());
                Collections.reverse(bookList);
                break;
            case dueDateAZ:
                Collections.sort(bookList, new customComparatorDueDate());
                break;
            case dueDateZA:
                Collections.sort(bookList, new customComparatorDueDate());
                Collections.reverse(bookList);
                break;
        }

        LoanBookListAdapter adapter = new LoanBookListAdapter(getContext(), R.layout.adapter_view_layout, bookList);
        mListView.setAdapter(adapter);
        mListView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
    }


    //TODO: Send to Jerry.
    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
    //TODO: Send to Jerry.
    public int getLeastDueBook(List<WMSLoan> bookList){
        if (bookList.isEmpty()) return -1;
        Collections.sort(bookList, new customComparatorDueDate());
        bookList.get(0).getDueDate();
        Date date = new Date();


        int days = daysBetween(bookList.get(0).getDueDate(), date);

       // long diff = (Calendar.getInstance().getTime()) - Date.parse(bookList.get(0).getDueDate()))
       // long diffDays = diff / (24 * 60 * 60 * 1000);
        return days;
    }

    /**
     * This method is responsible to register an action to BroadCastReceiver
     */
    private void registerMyReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(IntentActions.USER_PROFILE_RESPONSE);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(myBroadCastReceiver, intentFilter);
            Log.d(TAG, "Receiver Registered");
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