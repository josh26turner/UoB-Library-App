package spe.uoblibraryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import spe.uoblibraryapp.api.wmsobjects.WMSBook;
import spe.uoblibraryapp.api.wmsobjects.WMSLoan;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;

public class Home extends AppCompatActivity {

//    ArrayList<WMSBook> loans = new ArrayList<>();
//    WMSUserProfile user = new WMSUserProfile();
//    loans = user.getLoans();
//    for(loan:loans){
//        loanList.add(loan.getBook().getTitle());
//    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MainListAdapter mainListAdapter = new MainListAdapter(getLoanList());
        recyclerView.setAdapter(mainListAdapter);
    }

    public List<String> getLoanList() {
        List<String> loanList = new ArrayList<>();
        loanList.add("Book 1");
        loanList.add("Book 2");
        loanList.add("Book 3");
        loanList.add("Book 4");
        loanList.add("Book 5");
        loanList.add("Book 6");
        loanList.add("Book 7");
        loanList.add("Book 8");
        loanList.add("Book 9");
        return loanList;
    }
}
