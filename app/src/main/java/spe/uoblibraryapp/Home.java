package spe.uoblibraryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MainListAdapter mainListAdapter = new MainListAdapter(getMealList());
        recyclerView.setAdapter(mainListAdapter);
    }

    public List<String> getMealList() {
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
