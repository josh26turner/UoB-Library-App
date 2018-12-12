package spe.uoblibraryapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import spe.uoblibraryapp.api.WMSException;
import spe.uoblibraryapp.api.ncip.WMSNCIPController;
import spe.uoblibraryapp.api.wmsobjects.WMSBook;
import spe.uoblibraryapp.api.wmsobjects.WMSHold;
import spe.uoblibraryapp.api.wmsobjects.WMSLoan;
import spe.uoblibraryapp.api.wmsobjects.WMSParseException;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;
import stanford.androidlib.SimpleActivity;


public class FragmentLoans extends android.support.v4.app.Fragment {
    private static final String TAG = "LoansFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loans, container, false);
        ListView mListView = view.findViewById(R.id.listview);
        WMSNCIPController wmsncipController = new WMSNCIPController();
        ArrayList<LoanBookEntry> bookList = new ArrayList<>();
        Queue<LoanBookEntry> bookQueue = new LinkedList<>();
        WMSUserProfile userProfile;

        /* Calls WMS a maximum of 3 times before fatal error occurs */
        int exceptionCount = 0;
        while (true) {
            if (exceptionCount<3) {
                try {
                    userProfile = wmsncipController.getUserDetails("0");
                    break;
                } catch (WMSException e) {
                    Toast.makeText(getActivity(), "ERROR: Cannot get user details, trying again. ", Toast.LENGTH_SHORT).show();
                    exceptionCount++;
                } catch (WMSParseException e) {
                    Toast.makeText(getActivity(), "ERROR: Cannot get user details, trying again. ", Toast.LENGTH_SHORT).show();
                    exceptionCount++;
                }
            }
            else Toast.makeText(getActivity(), "FATAL ERROR OCCURRED - Try again later.", Toast.LENGTH_LONG);
        }

        /* Overdue here */
        Date date = new Date();
        for (WMSLoan loan : userProfile.getLoans()) {
            if (loan.getDueDate().before(date)) {
                //add to list-view -> item overdue.
                bookList.add(new LoanBookEntry(loan.getBook().getTitle(), loan.getBook().getAuthor(), BookStatus.OVERDUE));
            } else {
                //push to queue for re-entry later.
                bookQueue.add(new LoanBookEntry(loan.getBook().getTitle(), loan.getBook().getAuthor(), BookStatus.LOAN));
            }
        }

        /* Reservations here */
        ArrayList<WMSHold> reserveQueueList = new ArrayList<>();
        for (WMSHold hold : reserveQueueList)
            bookList.add(new LoanBookEntry(hold.getBook().getTitle(), hold.getBook().getAuthor(), BookStatus.RESERVATION));
        /* Sample Reservations */
        bookList.add(new LoanBookEntry("Test Reservation 1", "Test Author 1", BookStatus.RESERVATION));
        bookList.add(new LoanBookEntry("Test Reservation 2", "Test Author 2", BookStatus.RESERVATION));
        bookList.add(new LoanBookEntry("Test Reservation 3", "Test Author 3", BookStatus.RESERVATION));

        /*Loans added here */
        for (LoanBookEntry entry : bookQueue)
            bookList.add(entry);

        LoanBookListAdapter adapter = new LoanBookListAdapter(getContext(), R.layout.adapter_view_layout, bookList);
        mListView.setAdapter(adapter);
        ListView listview = view.findViewById(R.id.listview);
        listview.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
        return view;
    }
}