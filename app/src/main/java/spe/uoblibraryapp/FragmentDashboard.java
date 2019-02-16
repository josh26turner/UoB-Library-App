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
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.api.WMSException;
import spe.uoblibraryapp.api.WMSResponse;
import spe.uoblibraryapp.api.ncip.WMSNCIPElement;
import spe.uoblibraryapp.api.ncip.WMSNCIPResponse;
import spe.uoblibraryapp.api.ncip.WMSNCIPService;
import spe.uoblibraryapp.api.wmsobjects.WMSHold;
import spe.uoblibraryapp.api.wmsobjects.WMSParseException;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;

public class FragmentDashboard extends android.support.v4.app.Fragment {
    private static final String TAG = "DashboardFragment";
    private FragmentDashboard.MyBroadCastReceiver myBroadCastReceiver;
    private CacheManager cacheManager;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        myBroadCastReceiver = new MyBroadCastReceiver();

        cacheManager = CacheManager.getInstance();

        // Swipe to Refresh
        SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.swiperefresh_dash);
        swipeRefresh.setOnRefreshListener(() -> {
            // Pull to Refresh list
            swipeRefresh.setRefreshing(true);
            Log.d(TAG, "Dashboard SwipeRefresh true");
            Intent getUserProfileIntent = new Intent(Constants.IntentActions.LOOKUP_USER);
            WMSNCIPService.enqueueWork(getContext(), WMSNCIPService.class, 1000, getUserProfileIntent);
        });



        return view;
    }

    /**
     * This method is responsible to register an action to BroadCastReceiver
     */
    private void registerMyReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.IntentActions.USER_PROFILE_RESPONSE);
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

    @Override
    public void onResume() {
        super.onResume();
        registerMyReceiver();

        // Refresh list here if fragment resumes and it hasn't been refreshed in 10 minutes
        if (cacheManager.isExpired()) {
            SwipeRefreshLayout swipeRefreshResv = view.findViewById(R.id.swiperefresh_dash);
            swipeRefreshResv.setRefreshing(true);
            Intent getUserProfileIntent = new Intent(Constants.IntentActions.LOOKUP_USER);
            WMSNCIPService.enqueueWork(getContext(), WMSNCIPService.class, 1000, getUserProfileIntent);
        } else {
            updateDashboardLoans();
            updateDashboardReservations();
        }
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

    private void updateDashboardLoans(){
        //Update Dashboard
        TextView loan_dash_description = view.findViewById(R.id.loan_dash_description);
        loan_dash_description.setText("You have borrowed "
                + cacheManager.getUserProfile().getLoans().size()
                + " out of 40 books. The first book is due on "
                + 0 //TODO: Solve due date issue
                + ".");
    }

    private void updateDashboardReservations(){
        //Updating Reservation Dashboard
        Log.d(TAG, "Updating Reservation Dash");
        TextView tv = view.findViewById(R.id.resv_dash_description);
        tv.setText(   "You have "
                + cacheManager.getUserProfile().getOnHold().size()
                + " reservations, "
                + readyCollectCount(cacheManager.getUserProfile())
                + " of which are ready to collect.");
    }
    public int readyCollectCount(WMSUserProfile profile){
        int c = 0;
        int i = 0;
        List<WMSHold> holds = profile.getOnHold();
        for(i=0; i < holds.size(); i++){
            if(holds.get(i).isReadyToCollect())
                c++;
        }
        return c;
    }


    class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.d(TAG, "onReceive() called");

                String xml = intent.getStringExtra("xml");

                WMSUserProfile userProfile = parseUserProfileResponse(xml);

                updateDashboardLoans();
                updateDashboardReservations();

                SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.swiperefresh_dash);
                swipeRefresh.setRefreshing(false);

                cacheManager.setUserProfile(userProfile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


}
