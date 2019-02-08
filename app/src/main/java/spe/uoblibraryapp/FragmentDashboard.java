package spe.uoblibraryapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import spe.uoblibraryapp.api.wmsobjects.WMSLoan;

public class FragmentDashboard extends android.support.v4.app.Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_loans, container, false);
        return view;
    }

    /*private void updateDash(List<WMSLoan> bookList){
        //Update Dashboard
        TextView loan_dash_description = view.findViewById(R.id.loan_dash_description);
        loan_dash_description.setText("You have borrowed "
                + bookList.size()
                + " out of 40 books. The first book is due on "
                + bookList.get(0).getDueDate()
                + ".");
    }
    */

}
