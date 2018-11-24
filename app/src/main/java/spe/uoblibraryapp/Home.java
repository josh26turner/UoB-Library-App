package spe.uoblibraryapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import spe.uoblibraryapp.api.WMSException;
import spe.uoblibraryapp.api.ncip.WMSNCIPController;
import spe.uoblibraryapp.api.wmsobjects.WMSBook;
import spe.uoblibraryapp.api.wmsobjects.WMSLoan;
import spe.uoblibraryapp.api.wmsobjects.WMSParseException;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;

public class Home extends AppCompatActivity {

    private WMSUserProfile mockUser;
    private String userID;
    private List<String> loanList = new ArrayList<>();
    private WMSNCIPController mockController = new WMSNCIPController(true);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    ////////////////////////////

    public void setUserID(String s){
        this.userID = s;
    }

    private void getUser(){
        try {
            mockUser = mockController.getUserDetails(userID);
        } catch (WMSException | WMSParseException e) {
            e.printStackTrace();
        }
    }

    private void setLoans(){
        List<WMSLoan> loans = mockUser.getLoans();
        for(WMSLoan loan : loans){
            this.addItem(loan.getBook().getTitle());
        }
    }


    private void addItem(String s){
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
