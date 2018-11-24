package spe.uoblibraryapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import spe.uoblibraryapp.api.WMSException;
import spe.uoblibraryapp.api.ncip.WMSNCIPController;
import spe.uoblibraryapp.api.wmsobjects.WMSBook;
import spe.uoblibraryapp.api.wmsobjects.WMSLoan;
import spe.uoblibraryapp.api.wmsobjects.WMSParseException;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;


public class LoansFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "LoansFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loans, container, false);

        ListView mListView = (ListView) view.findViewById(R.id.listview);

        LoanBookEntry bookOne = new LoanBookEntry("Book 1 Title", "Author 1", "Overdue");
        LoanBookEntry bookTwo = new LoanBookEntry("Book 2 Title", "Author 2", "Reservation");
        LoanBookEntry bookThree = new LoanBookEntry("Book 3 Title", "Author 3", "Loan");


        ArrayList<LoanBookEntry> bookList = new ArrayList<>();
        bookList.add(bookOne);
        bookList.add(bookTwo);
        bookList.add(bookThree);
        LoanBookListAdapter adapter = new LoanBookListAdapter(getContext(), R.layout.adapter_view_layout, bookList);
        mListView.setAdapter(adapter);
        return view;
    }
}