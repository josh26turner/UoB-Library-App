package spe.uoblibraryapp;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import spe.uoblibraryapp.api.ncip.WMSNCIPService;
import spe.uoblibraryapp.api.wmsobjects.WMSHold;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;


public class FragmentReservation extends android.support.v4.app.Fragment {
    private static final String TAG = "ReservationFragment";
    private MyBroadCastReceiver myBroadCastReceiver;
    private View view;
    private CacheManager cacheManager;
    private ResvBookListAdapter resvBookListAdapter;
    public List<WMSHold> resvList;
    private ListView mListView;
    private ViewDialog openDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reservations, container, false);

        myBroadCastReceiver = new MyBroadCastReceiver();

        cacheManager = CacheManager.getInstance();

        // Swipe to Refresh
        SwipeRefreshLayout swipeRefreshResv = view.findViewById(R.id.swiperefresh2);
        swipeRefreshResv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Pull to Refresh list
                if (!cacheManager.getRefreshing()) {
                    cacheManager.setRefreshing(true);
                    swipeRefreshResv.setRefreshing(true);
                    Intent getUserProfileIntent = new Intent(Constants.IntentActions.LOOKUP_USER);
                    WMSNCIPService.enqueueWork(getContext(), WMSNCIPService.class, WMSNCIPService.jobId, getUserProfileIntent);
                }
            }
        });

        mListView=view.findViewById(R.id.listview2);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ViewDialog alert = new ViewDialog();
                alert.showDialog(getActivity(), resvList.get(position));
                openDialog = alert;
            }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerMyReceiver();
        // Refresh list here if fragment resumes and it hasn't been refreshed in 10 minutes
        if (cacheManager.isExpired()) {
            if(!cacheManager.getRefreshing()) {
                cacheManager.setRefreshing(true);
                SwipeRefreshLayout swipeRefreshResv = view.findViewById(R.id.swiperefresh2);
                swipeRefreshResv.setRefreshing(true);
                Intent getUserProfileIntent = new Intent(Constants.IntentActions.LOOKUP_USER);
                WMSNCIPService.enqueueWork(getContext(), WMSNCIPService.class, WMSNCIPService.jobId, getUserProfileIntent);
            }
        } else {
            fillListView(cacheManager.getUserProfile());
        }


    }

    public void fillListView(WMSUserProfile userProfile) {
        List<WMSHold> bookList = new ArrayList<>(userProfile.getOnHold());
        resvlist=bookList;

        if (bookList.isEmpty()) return;

        ResvBookListAdapter adapter = new ResvBookListAdapter(getContext(), R.layout.adapter_view_layout_resv, bookList);
        mListView.setAdapter(adapter);
        mListView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
        resvBookListAdapter = adapter;
        //updateDash(userProfile.getLoans());

    }

    /**
     * This method is responsible to register an action to BroadCastReceiver
     */
    private void registerMyReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.IntentActions.USER_PROFILE_RESPONSE);
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


    class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.d(TAG, "onReceive() called");

                if (Constants.IntentActions.USER_PROFILE_RESPONSE.equals(intent.getAction())){
                    WMSUserProfile userProfile = cacheManager.getUserProfile();
                    fillListView(userProfile);
                } else if(Constants.IntentActions.CANCEL_RESERVATION_RESPONSE.equals(intent.getAction())){
                    if (openDialog != null){
                        openDialog.closeDialog();
                    }
                }else {
                    Toast toast = Toast.makeText(getContext(), "Refresh Failed",Toast.LENGTH_LONG);
                    toast.show();
                }

                SwipeRefreshLayout swipeRefreshResv = view.findViewById(R.id.swiperefresh2);
                swipeRefreshResv.setRefreshing(false);
                cacheManager.setRefreshing(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }



    //Extra Reservation Information Dialog
    public class ViewDialog {
        Dialog dialog;
        String reservationId;
        void showDialog(Activity activity, WMSHold reservation){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_reservations_layout);

            ((TextView) dialog.findViewById(R.id.txt_pickuplocation)).setText(reservation.getPickupLocation());
            ((TextView) dialog.findViewById(R.id.txt_bookname)).setText(reservation.getBook().getTitle());
            ((TextView) dialog.findViewById(R.id.txt_author)).setText(reservation.getBook().getAuthor());

            ((Button) dialog.findViewById(R.id.btn_dialog)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Add cancel reservation function here
                    Intent intent = new Intent(Constants.IntentActions.CANCEL_RESERVATION);
                    intent.putExtra("reservationId", reservation.getRequestId());
                    WMSNCIPService.enqueueWork(getContext(), WMSNCIPService.class, WMSNCIPService.jobId, intent);
                    Toast.makeText(getContext(), "Clicked.", Toast.LENGTH_LONG).show();
                }
            });
            dialog.show();
            this.dialog = dialog;
        }

        void closeDialog(){
            if (dialog != null) dialog.dismiss();
        }
    }


}