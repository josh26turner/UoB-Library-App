package spe.uoblibraryapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoanBookListAdapter extends ArrayAdapter<LoanBookEntry> {

    private Context mContext;
    int mResource;

    public LoanBookListAdapter(Context context, int resource, ArrayList<LoanBookEntry> objects){
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String title = getItem(position).getTitle();
        String author = getItem(position).getAuthor();
        BookStatus status = getItem(position).getStatus();

//        LoanBookEntry book = new LoanBookEntry( title, author, status);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false );
        TextView textViewTitle = convertView.findViewById(R.id.txtTitle);
        TextView textViewAuthor = convertView.findViewById(R.id.txtAuthor);
        TextView textViewStatus = convertView.findViewById(R.id.txtStatus);
        textViewTitle.setText(title);
        textViewAuthor.setText(author);
        textViewStatus.setText(status.toString());

        if (status == BookStatus.OVERDUE)
            textViewStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOverdue));

        else if (status == BookStatus.RESERVATION)
            textViewStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.colorReservation));
        else
            textViewStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.colorLoan));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }


}
