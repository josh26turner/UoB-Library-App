package spe.uoblibraryapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.api.IntentActions;
import spe.uoblibraryapp.api.WMSException;
import spe.uoblibraryapp.api.WMSResponse;
import spe.uoblibraryapp.api.ncip.ConcreteWMSNCIPPatronService;
import spe.uoblibraryapp.api.ncip.WMSNCIPElement;
import spe.uoblibraryapp.api.ncip.WMSNCIPResponse;
import spe.uoblibraryapp.api.wmsobjects.WMSHold;
import spe.uoblibraryapp.api.wmsobjects.WMSLoan;
import spe.uoblibraryapp.api.wmsobjects.WMSParseException;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;


public class FragmentLoans extends android.support.v4.app.Fragment {
    private static final String TAG = "LoansFragment";
    public static final String BROADCAST_ACTION = "spe.uoblibraryapp.api.RESPONSE";
    private MyBroadCastReceiver myBroadCastReceiver;
    private boolean refreshing;
    private Date lastRefresh;
    private String lastResponse;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_loans, container, false);

        myBroadCastReceiver = new MyBroadCastReceiver();

        // Floating action button
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getContext(), "Scan New Book", Toast.LENGTH_SHORT);
                toast.show();
                Intent NFCActivity = new Intent(getActivity(), ActivityScanNFC.class);
                startActivity(NFCActivity);
            }
        });

        // Swipe to Refresh
        SwipeRefreshLayout swipeRefreshLoans = view.findViewById(R.id.swiperefresh);
        swipeRefreshLoans.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Pull to Refresh list
                swipeRefreshLoans.setRefreshing(true);
                Intent getUserProfileIntent = new Intent(IntentActions.LOOKUP_USER);
                ConcreteWMSNCIPPatronService.enqueueWork(getContext(), ConcreteWMSNCIPPatronService.class, 1000, getUserProfileIntent);
                refreshing = true;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerMyReceiver();
        // Refresh list here if fragment resumes and it hasn't been refreshed in 10 minutes
        if (lastRefresh != null){
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(lastRefresh);
            cal.add(Calendar.MINUTE, 10);
            if (!lastRefresh.before(cal.getTime())) {
                SwipeRefreshLayout swipeRefreshLoans = view.findViewById(R.id.swiperefresh);
                swipeRefreshLoans.setRefreshing(true);
                Intent getUserProfileIntent = new Intent(IntentActions.LOOKUP_USER);
                ConcreteWMSNCIPPatronService.enqueueWork(getContext(), ConcreteWMSNCIPPatronService.class, 1000, getUserProfileIntent);
                refreshing = true;
                Log.e(TAG, "Intent queued");
            } else{
                try {
                    fillListView(parseUserProfileResponse(lastResponse));
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        } else{
            SwipeRefreshLayout swipeRefreshLoans = view.findViewById(R.id.swiperefresh);
            swipeRefreshLoans.setRefreshing(true);
            Intent getUserProfileIntent = new Intent(IntentActions.LOOKUP_USER);
            ConcreteWMSNCIPPatronService.enqueueWork(getContext(), ConcreteWMSNCIPPatronService.class, 1000, getUserProfileIntent);
            refreshing = true;
            Log.e(TAG, "Intent queued");
        }
    }

    // If we want thumbnails this gives us an image link https://www.googleapis.com/books/v1/volumes?q=isbn:9780226467047

    public void fillListView(WMSUserProfile userProfile) {
        ListView mListView = view.findViewById(R.id.listview);
        ArrayList<LoanBookEntry> bookList = new ArrayList<>();
        Queue<LoanBookEntry> bookQueue = new LinkedList<>();

        // Overdue here
        Date date = new Date();
        for (WMSLoan loan : userProfile.getLoans()) {
            if (loan.getDueDate().before(date)) {
                // Add to list-view -> item overdue.
                bookList.add(new LoanBookEntry(loan.getBook().getTitle(), loan.getBook().getAuthor(), BookStatus.OVERDUE));
            } else {
                // Push to queue for re-entry later.
                bookQueue.add(new LoanBookEntry(loan.getBook().getTitle(), loan.getBook().getAuthor(), BookStatus.LOAN));
            }
        }

        // Reservations here
        ArrayList<WMSHold> reserveQueueList = new ArrayList<>();
        for (WMSHold hold : reserveQueueList)
            bookList.add(new LoanBookEntry(hold.getBook().getTitle(), hold.getBook().getAuthor(), BookStatus.RESERVATION));
        // Sample Reservations
        bookList.add(new LoanBookEntry("Test Reservation 1", "Test Author 1", BookStatus.RESERVATION));
        bookList.add(new LoanBookEntry("Test Reservation 2", "Test Author 2", BookStatus.RESERVATION));
        bookList.add(new LoanBookEntry("Test Reservation 3", "Test Author 3", BookStatus.RESERVATION));

        // Loans added here
        for (LoanBookEntry entry : bookQueue)
            bookList.add(entry);

        LoanBookListAdapter adapter = new LoanBookListAdapter(getContext(), R.layout.adapter_view_layout, bookList);
        mListView.setAdapter(adapter);

        mListView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);

    }

    /**
     * This method is responsible to register an action to BroadCastReceiver
     * */
    private void registerMyReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_ACTION);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(myBroadCastReceiver,  intentFilter);
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

    private WMSUserProfile parseUserProfileResponse(String xml) throws WMSException, WMSParseException { ;
        WMSResponse response = new WMSNCIPResponse(xml);

        if (response.didFail()) {
            throw new WMSException("There was an error retrieving the User Profile");
        }
        Document doc;
        try {
            doc = response.parse();
        } catch (IOException | SAXException | ParserConfigurationException e){
            throw new WMSException("There was an error Parsing the WMS response");
        }
        Node node = doc.getElementsByTagName("ns1:LookupUserResponse").item(0);
        return new WMSUserProfile(new WMSNCIPElement(node));
    }


    class MyBroadCastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.d(TAG, "onReceive() called");

                String xml = intent.getStringExtra("xml");

                WMSUserProfile userProfile = parseUserProfileResponse(xml);

                fillListView(userProfile);
                if (refreshing) {
                    SwipeRefreshLayout swipeRefreshLoans = view.findViewById(R.id.swiperefresh);
                    swipeRefreshLoans.setRefreshing(false);
                    Toast toast = Toast.makeText(getContext(), "Loans Updated", Toast.LENGTH_SHORT);
                    toast.show();
                    refreshing = false;
                    lastRefresh = new Date();
                }
                lastResponse = xml;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}