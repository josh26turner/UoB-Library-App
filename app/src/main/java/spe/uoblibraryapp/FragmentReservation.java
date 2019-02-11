package spe.uoblibraryapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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

import android.widget.AdapterView.OnItemClickListener;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
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
import spe.uoblibraryapp.api.wmsobjects.WMSHold;
import spe.uoblibraryapp.api.wmsobjects.WMSLoan;
import spe.uoblibraryapp.api.wmsobjects.WMSParseException;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;


public class FragmentReservation extends android.support.v4.app.Fragment {
    private static final String TAG = "ReservationFragment";
    private MyBroadCastReceiver myBroadCastReceiver;
    View view;
    private CacheManager cacheManager;
    public List<WMSHold> resvlist;
    ListView mListView;


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
                swipeRefreshResv.setRefreshing(true);
                Intent getUserProfileIntent = new Intent(IntentActions.LOOKUP_USER);
                WMSNCIPService.enqueueWork(getContext(), WMSNCIPService.class, 1000, getUserProfileIntent);
            }
        });

   mListView=view.findViewById(R.id.listview2);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //TODO: MODIFY ME TO SHOW ACTUAL VALUES.
                ViewDialog alert = new ViewDialog();
                alert.showDialog(getActivity(), resvlist.get(position)); }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerMyReceiver();
        // Refresh list here if fragment resumes and it hasn't been refreshed in 10 minutes
        if (cacheManager.isExpired()) {
            SwipeRefreshLayout swipeRefreshResv = view.findViewById(R.id.swiperefresh2);
            swipeRefreshResv.setRefreshing(true);
            Intent getUserProfileIntent = new Intent(IntentActions.LOOKUP_USER);
            WMSNCIPService.enqueueWork(getContext(), WMSNCIPService.class, 1000, getUserProfileIntent);
        } else {
            fillListView(cacheManager.getUserProfile());
        }


    }

    public void fillListView(WMSUserProfile userProfile) {
        List<WMSHold> bookList = new ArrayList<>(userProfile.getOnHold());
        bookList.add(new WMSHold());
        bookList.add(new WMSHold());
        bookList.add(new WMSHold());
        bookList.add(new WMSHold());
        bookList.add(new WMSHold());
        bookList.add(new WMSHold());
        bookList.add(new WMSHold());
        bookList.add(new WMSHold());
        bookList.add(new WMSHold());
        resvlist=bookList;
        ResvBookListAdapter adapter = new ResvBookListAdapter(getContext(), R.layout.adapter_view_layout_resv, bookList);
        mListView.setAdapter(adapter);
        mListView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);

        //updateDash(userProfile.getLoans());

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

                SwipeRefreshLayout swipeRefreshResv = view.findViewById(R.id.swiperefresh2);
                swipeRefreshResv.setRefreshing(false);

                cacheManager.setUserProfile(userProfile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }



    //TODO: Document this ViewDialog.
    public class ViewDialog {

        public void showDialog(Activity activity, WMSHold reservation){
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
                }
            });

            dialog.show();
        }
    }


}