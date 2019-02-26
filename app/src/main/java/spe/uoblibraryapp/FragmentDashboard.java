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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import spe.uoblibraryapp.api.ncip.WMSNCIPService;
import spe.uoblibraryapp.api.wmsobjects.WMSHold;
import spe.uoblibraryapp.api.wmsobjects.WMSLoan;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;

public class FragmentDashboard extends android.support.v4.app.Fragment {
    private static final String TAG = "DashboardFragment";
    private MyBroadCastReceiver myBroadCastReceiver;
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


    private void updateDashboardLoans(){
        //Update Dashboard
        TextView loan_dash_description = view.findViewById(R.id.loan_dash_description);
        String output;
        List<WMSLoan> bookList = cacheManager.getUserProfile().getLoans();
        if (!bookList.isEmpty()) {
            Collections.sort(bookList, new SortCustomComparatorDueDate());
            output = String.format("You have borrowed %s out of %s books. The first book is due back on %s", cacheManager.getUserProfile().getLoans().size(), 40, bookList.get(0).getDueDate());
        }
        else output = "Currently you have no loans :)";



        loan_dash_description.setText(output);
    }

    private void updateDashboardReservations(){
        //Updating Reservation Dashboard
        Log.d(TAG, "Updating Reservation Dash");
        TextView tv = view.findViewById(R.id.resv_dash_description);
        int reservationSize = cacheManager.getUserProfile().getOnHold().size();
        if (reservationSize==0) {
            tv.setText("Currently you have no reservations :)");
        }
        else {
            String output = String.format("You have %s reservations, %s of which are ready to collect", cacheManager.getUserProfile().getOnHold().size(), readyCollectCount(cacheManager.getUserProfile()));
            tv.setText(output);
        }
    }
    public int readyCollectCount(WMSUserProfile profile){
        int c = 0;
        List<WMSHold> holds = profile.getOnHold();
        for(int i=0; i < holds.size(); i++){
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
                if (Constants.IntentActions.USER_PROFILE_RESPONSE.equals(intent.getAction())){
                    updateDashboardLoans();
                    updateDashboardReservations();
                } else {
                    Toast toast = Toast.makeText(getContext(), "Refresh Failed",Toast.LENGTH_LONG);
                    toast.show();
                }

                SwipeRefreshLayout swipeRefreshLoans = view.findViewById(R.id.swiperefresh_dash);
                swipeRefreshLoans.setRefreshing(false);
                cacheManager.setRefreshing(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


}
