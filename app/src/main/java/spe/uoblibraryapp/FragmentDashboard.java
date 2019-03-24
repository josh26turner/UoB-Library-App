package spe.uoblibraryapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Date;
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

        ActivityHome mHome = (ActivityHome) getActivity();

        ((CardView) view.findViewById(R.id.loan_card_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHome.setViewPager("Loans");
            }
        });
        ((CardView) view.findViewById(R.id.resv_card_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHome.setViewPager("Reservation");
            }
        });

        return view;
    }

    /**
     * This method is responsible to register an action to BroadCastReceiver
     */
    private void registerMyReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.IntentActions.LOOKUP_USER_RESPONSE);
            intentFilter.addAction(Constants.IntentActions.LOOKUP_USER_ACCOUNT_RESPONSE);
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
            updateAccountBlocked();
        }
    }


    private void updateDashboardLoans(){
        //Update Dashboard
        TextView loan_dash_description = view.findViewById(R.id.loan_dash_description);
        String output;
        List<WMSLoan> bookList = cacheManager.getUserProfile().getLoans();
        SharedPreferences prefs = getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);

        if (!bookList.isEmpty()) {
            Collections.sort(bookList, new SortCustomComparatorDueDate());
            Date mostRecentDueDate = bookList.get(0).getDueDate();
            Date dateToday = new Date();

            int bookDueDate = daysBetween(dateToday, mostRecentDueDate);
            if (bookDueDate <= 7)
                if (bookDueDate == 0)
                    output = String.format(
                            "You have borrowed %s out of %s books. The book %s is due back today.",
                            cacheManager.getUserProfile().getLoans().size(),
                            Constants.LibraryDetails.borrowerCategories.get(prefs.getString("borrowerCategory", "")),
                            bookList.get(0).getBook().getTitle()
                    );
                else if (bookDueDate == 1)
                    output = String.format(
                            "You have borrowed %s out of %s books. The book %s is due back in 1 day.",
                            cacheManager.getUserProfile().getLoans().size(),
                            Constants.LibraryDetails.borrowerCategories.get(prefs.getString("borrowerCategory", "")),
                            bookList.get(0).getBook().getTitle()
                    );
                else
                    output = String.format(
                            "You have borrowed %s out of %s books. The book %s is due back in %s days.",
                            cacheManager.getUserProfile().getLoans().size(),
                            Constants.LibraryDetails.borrowerCategories.get(prefs.getString("borrowerCategory", "")),
                            bookList.get(0).getBook().getTitle(), bookDueDate
                    );

            else
                output = String.format(
                        "You have borrowed %s out of %s books.",
                        cacheManager.getUserProfile().getLoans().size(),
                        Constants.LibraryDetails.borrowerCategories.get(prefs.getString("borrowerCategory", ""))
                );
        }
        else output = "Currently you have no loans.";

        loan_dash_description.setText(output);
    }

    private void updateDashboardReservations(){
        //Updating Reservation Dashboard
        Log.d(TAG, "Updating Reservation Dash");
        TextView tv = view.findViewById(R.id.resv_dash_description);
        int reservationSize = cacheManager.getUserProfile().getOnHold().size();
        if (reservationSize==0) {
            tv.setText("Currently, you have no reservations.");
        }
        else {
            String output = String.format("You have %s reservations, %s of which are ready to collect", cacheManager.getUserProfile().getOnHold().size(), readyCollectCount(cacheManager.getUserProfile()));
            tv.setText(output);
        }
    }

    private void updateAccountBlocked(){
        View accountCard = view.findViewById(R.id.blocked_account_card_view);
        SharedPreferences prefs = getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        Boolean accountBlocked = prefs.getBoolean("accountBlocked", true);
        if (accountBlocked){
            accountCard.setVisibility(View.VISIBLE);
        } else{
            accountCard.setVisibility(View.GONE);
        }
    }


    public int readyCollectCount(WMSUserProfile profile){
        int c = 0;
        List<WMSHold> holdList = profile.getOnHold();

        for (WMSHold hold  : holdList){
            if (hold.isReadyToCollect())
                c++;
        }

        return c;
    }

    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }


    class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.d(TAG, "onReceive() called");
                if (Constants.IntentActions.LOOKUP_USER_RESPONSE.equals(intent.getAction())){
                    updateDashboardLoans();
                    updateDashboardReservations();
                } else if (Constants.IntentActions.LOOKUP_USER_ACCOUNT_RESPONSE.equals(intent.getAction())) {
                    updateAccountBlocked();
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
