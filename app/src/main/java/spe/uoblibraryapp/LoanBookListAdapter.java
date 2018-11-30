package spe.uoblibraryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        String status = getItem(position).getStatus();

        LoanBookEntry book = new LoanBookEntry( title, author, status);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false );

        TextView textViewTitle = convertView.findViewById(R.id.txtTitle);
        TextView textViewAuthor = convertView.findViewById(R.id.txtAuthor);
        TextView textViewStatus = convertView.findViewById(R.id.txtStatus);
        textViewTitle.setText(title);
        textViewAuthor.setText(author);
        textViewStatus.setText(status);

        return convertView;
    }
}
