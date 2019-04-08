package spe.uoblibraryapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import spe.uoblibraryapp.api.wmsobjects.WMSLoan;

public class LoanBookListAdapter extends ArrayAdapter<WMSLoan> {

    private Context mContext;
    private int mResource;

    public LoanBookListAdapter(Context context, int resource, List<WMSLoan> objects){
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View newView = inflater.inflate(mResource, parent, false );
        String title = getItem(position).getBook().getTitle();
        String author = getItem(position).getBook().getAuthor();
        Boolean overdue = getItem(position).isOverdue();

        Date dueDate = getItem(position).getDueDate();

        TextView textViewTitle = newView.findViewById(R.id.txtTitle);
        TextView textViewAuthor = newView.findViewById(R.id.txtAuthor);
        TextView textViewStatus = newView.findViewById(R.id.txtStatus);

        textViewTitle.setText(title);
        textViewAuthor.setText(author);

        if (overdue) {
            textViewStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOverdue));
            textViewStatus.setText("Overdue");
        } else {
            textViewStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.colorReservation));
            Date dateToday = new Date();
            int bookDueDate = daysBetween(dateToday, dueDate);
            if (bookDueDate == 0) textViewStatus.setText("Due today");
            else if (bookDueDate == 1) textViewStatus.setText("Due tomorrow");
            else textViewStatus.setText(String.format(Locale.ENGLISH, "Due in %d days", bookDueDate));
        }
        return newView;
    }

    private int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

}
