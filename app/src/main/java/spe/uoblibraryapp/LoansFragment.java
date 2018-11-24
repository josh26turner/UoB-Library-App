package spe.uoblibraryapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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