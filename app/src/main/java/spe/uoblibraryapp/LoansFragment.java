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
    private WMSUserProfile mockUser;
    private String userID;
    private List<String> loanList = new ArrayList<>();
    private WMSNCIPController mockController = new WMSNCIPController(true);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loans, container, false);

        ListView mListView = (ListView) view.findViewById(R.id.listview);

        LoanBookEntry bookOne = new LoanBookEntry("Book 1 Title", "Author Unknown", "Overdue");
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

    public void setUserID(String s) {
        this.userID = s;
    }

    private void getUser() {
        try {
            mockUser = mockController.getUserDetails(userID);
        } catch (WMSException | WMSParseException e) {
            e.printStackTrace();
        }
    }

    private void setLoans() {
        List<WMSLoan> loans = mockUser.getLoans();
        for (WMSLoan loan : loans) {
            this.addItem(loan.getBook().getTitle());
        }
    }

    private void addItem(String s) {
        this.loanList.add(s);
    }

    public List<String> mockLoanList() {
        //MOCK RESULT
        loanList.add("Book 1");
        loanList.add("Book 2");
        loanList.add("Book 3");
        loanList.add("Book 4");
        loanList.add("Book 5");
        loanList.add("Book 6");
        loanList.add("Book 7");
        loanList.add("Book 8");
        loanList.add("Book 9");
        //MOCK RESULT
        return loanList;
    }
}