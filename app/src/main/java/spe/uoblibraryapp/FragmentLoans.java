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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import spe.uoblibraryapp.api.ncip.WMSNCIPService;
import spe.uoblibraryapp.api.wmsobjects.WMSLoan;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;


public class FragmentLoans extends android.support.v4.app.Fragment {
    private static final String TAG = "LoansFragment";
    private View view;
    private MyBroadCastReceiver myBroadCastReceiver;
    private CacheManager cacheManager;
    public static ArrayAdapter listViewAdapter;
    private enum sort{ AZ, ZA, dueDateAZ, dueDateZA }
    private sort currentSort = sort.AZ;
    public List<WMSLoan> loanList;

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

            // Avoids a double refresh
            if (!cacheManager.getRefreshing()){
                cacheManager.setRefreshing(true);
                swipeRefreshLoans.setRefreshing(true);
                Intent getUserProfileIntent = new Intent(Constants.IntentActions.LOOKUP_USER);
                WMSNCIPService.enqueueWork(getContext(), WMSNCIPService.class, WMSNCIPService.jobId, getUserProfileIntent);
            }

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
//                fillListView(cacheManager.getUserProfile());


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ListView mListView=view.findViewById(R.id.loansListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FragmentLoans.ViewDialog alert = new FragmentLoans.ViewDialog();
                alert.showDialog(getActivity(), loanList.get(position)); }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerMyReceiver();
        if (cacheManager.isExpired()) {
            if(!cacheManager.getRefreshing()) {
                cacheManager.setRefreshing(true);
                SwipeRefreshLayout swipeRefreshLoans = view.findViewById(R.id.swiperefresh);
                swipeRefreshLoans.setRefreshing(true);
                Intent getUserProfileIntent = new Intent(Constants.IntentActions.LOOKUP_USER);
                WMSNCIPService.enqueueWork(getContext(), WMSNCIPService.class, WMSNCIPService.jobId, getUserProfileIntent);
            }
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

    public void fillListView(WMSUserProfile userProfile) {
        ListView mListView = view.findViewById(R.id.loansListView);
        List<WMSLoan> bookList = new ArrayList<>(userProfile.getLoans());

        if (bookList.isEmpty()) {
            return;
        }

        switch (currentSort) {
            case AZ:
                Collections.sort(bookList, new customComparatorAZ());
                break;
            case ZA:
                Collections.sort(bookList, new customComparatorAZ());
                Collections.reverse(bookList);
                break;
            case dueDateAZ:
                Collections.sort(bookList, new SortCustomComparatorDueDate());
                break;
            case dueDateZA:
                Collections.sort(bookList, new SortCustomComparatorDueDate());
                Collections.reverse(bookList);
                break;
            default:
                //this is not possible, added for the automatic code review.
                break;
        }
        loanList = bookList;
        ArrayAdapter adapter = new LoanBookListAdapter(getContext(), R.layout.adapter_view_layout, bookList);
        mListView.setAdapter(adapter);
        mListView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
        listViewAdapter = adapter;
    }



    /**
     * This method is responsible to register an action to BroadCastReceiver
     */
    private void registerMyReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.IntentActions.USER_PROFILE_RESPONSE);
            intentFilter.addAction(Constants.IntentActions.LOOKUP_USER_ERROR);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(myBroadCastReceiver, intentFilter);
            Log.d(TAG, "Receiver Registered");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(myBroadCastReceiver);
        super.onPause();
    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.d(TAG, "onReceive() called");
                if (Constants.IntentActions.USER_PROFILE_RESPONSE.equals(intent.getAction())){
                    WMSUserProfile userProfile = cacheManager.getUserProfile();
                    fillListView(userProfile);
                } else {
                    Toast toast = Toast.makeText(getContext(), "Refresh Failed",Toast.LENGTH_LONG);
                    toast.show();
                }

                SwipeRefreshLayout swipeRefreshLoans = view.findViewById(R.id.swiperefresh);
                swipeRefreshLoans.setRefreshing(false);
                cacheManager.setRefreshing(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Extra Loan Information Dialog
    public class ViewDialog {

        public void showDialog(Activity activity, WMSLoan loan){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_loans_layout);

            ((TextView) dialog.findViewById(R.id.txt_bookname)).setText(loan.getBook().getTitle());
            ((TextView) dialog.findViewById(R.id.txt_author)).setText(loan.getBook().getAuthor());

            dialog.show();
        }
    }

}