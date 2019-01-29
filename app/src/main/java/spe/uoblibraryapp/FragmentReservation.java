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
import spe.uoblibraryapp.api.ncip.WMSNCIPService;
import spe.uoblibraryapp.api.ncip.WMSNCIPElement;
import spe.uoblibraryapp.api.ncip.WMSNCIPResponse;
import spe.uoblibraryapp.api.wmsobjects.WMSHold;
import spe.uoblibraryapp.api.wmsobjects.WMSParseException;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;


public class FragmentReservation extends android.support.v4.app.Fragment {
    private static final String TAG = "ReservationFragment";
    private MyBroadCastReceiver myBroadCastReceiver;
    private boolean refreshing;
    private Date lastRefresh;
    private String lastResponse;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reservations, container, false);

        myBroadCastReceiver = new MyBroadCastReceiver();

        // Swipe to Refresh
        SwipeRefreshLayout swipeRefreshResv = view.findViewById(R.id.swiperefresh2);
        swipeRefreshResv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Pull to Refresh list
                swipeRefreshResv.setRefreshing(true);
                Intent getUserProfileIntent = new Intent(IntentActions.LOOKUP_USER);
                WMSNCIPService.enqueueWork(getContext(), WMSNCIPService.class, 1000, getUserProfileIntent);
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
                SwipeRefreshLayout swipeRefreshResv = view.findViewById(R.id.swiperefresh2);
                swipeRefreshResv.setRefreshing(true);
                Intent getUserProfileIntent = new Intent(IntentActions.LOOKUP_USER);
                WMSNCIPService.enqueueWork(getContext(), WMSNCIPService.class, 1000, getUserProfileIntent);
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
            SwipeRefreshLayout swipeRefreshResv = view.findViewById(R.id.swiperefresh2);
            swipeRefreshResv.setRefreshing(true);
            Intent getUserProfileIntent = new Intent(IntentActions.LOOKUP_USER);
            WMSNCIPService.enqueueWork(getContext(), WMSNCIPService.class, 1000, getUserProfileIntent);
            refreshing = true;
            Log.e(TAG, "Intent queued");
        }
    }


    public void fillListView(WMSUserProfile userProfile) {
        ListView mListView = view.findViewById(R.id.listview2);
        ArrayList<ResvBookEntry> bookList = new ArrayList<>();
        Queue<ResvBookEntry> bookQueue = new LinkedList<>();


        // Reservations here
        ArrayList<WMSHold> reserveQueueList = new ArrayList<>();
        for (WMSHold hold : userProfile.getOnHold())
            bookList.add(new ResvBookEntry (hold.getBook().getTitle(),
                                            hold.getBook().getAuthor(),
                                            hold.getHoldQueuePosition(),
                                            hold.getHoldQueueLength(),
                                            hold.getPickupLocation(),
                                            hold.isReadyToCollect(),
                                            hold.getRequestStatusType()));

        //Testing Reservations
        bookList.add(new ResvBookEntry("The amazing Jezza", "Jerry Kress", 1, 5, "Wills Memorial", Boolean.TRUE, "Available for Collection"));

        // Reservation added here
        for (ResvBookEntry entry : bookQueue)
            bookList.add(entry);

        ResvBookListAdapter adapter = new ResvBookListAdapter(getContext(), R.layout.adapter_view_layout_resv, bookList);
        mListView.setAdapter(adapter);

        mListView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
    }


    /**
     * This method is responsible to register an action to BroadCastReceiver
     * */
    private void registerMyReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(IntentActions.USER_PROFILE_RESPONSE);
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
                    SwipeRefreshLayout swipeRefreshResv = view.findViewById(R.id.swiperefresh2);
                    swipeRefreshResv.setRefreshing(false);
                    Toast toast = Toast.makeText(getContext(), "Reservations Updated", Toast.LENGTH_SHORT);
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